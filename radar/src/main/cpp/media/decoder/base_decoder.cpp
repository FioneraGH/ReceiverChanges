//
// Created by fionera on 2023/4/20.
//

#include "base_decoder.h"
#include "logger.h"
#include "timer.c"

BaseDecoder::BaseDecoder(JNIEnv *env, jstring path, bool for_synthesizer)
        : m_for_synthesizer(for_synthesizer) {
    initDecoder(env, path);
    createDecodeThread();
}

void BaseDecoder::initDecoder(JNIEnv *env, jstring path) {
    m_path_ref = env->NewGlobalRef(path);
    m_path = env->GetStringUTFChars(path, nullptr);
    env->GetJavaVM(&m_jvm_for_thread);
}

BaseDecoder::~BaseDecoder() {
    delete m_format_ctx;
    delete m_codec_ctx;
    delete m_frame;
    delete m_packet;
}

void BaseDecoder::createDecodeThread() {
    // intelligence ptr
    std::shared_ptr<BaseDecoder> pointer(this);
    std::thread t(decode, pointer);
    t.detach();
}

void BaseDecoder::decode(const std::shared_ptr<BaseDecoder> &that) {
    JNIEnv *env;

    if (that->m_jvm_for_thread->AttachCurrentThread(&env, nullptr) != JNI_OK) {
        LOG_ERROR(that->TAG, that->LogSpec(), "Fail to init decode thread")
        return;
    }

    that->initFfmpegDecoder(env);
    that->allocFrameBuffer();
    that->prepare(env);
    that->loopDecode();
    that->doneDecode(env);
    that->m_jvm_for_thread->DetachCurrentThread();
}

void BaseDecoder::initFfmpegDecoder(JNIEnv *env) {
    m_format_ctx = avformat_alloc_context();

    if (avformat_open_input(&m_format_ctx, m_path, nullptr, nullptr) != 0) {
        LOG_ERROR(TAG, LogSpec(), "Fail to open file %s", m_path);
        return;
    }

    if (avformat_find_stream_info(m_format_ctx, nullptr) < 0) {
        LOG_ERROR(TAG, LogSpec(), "Fail to find stream info");
        return;
    }

    int vIdx = -1;
    for (int i = 0; i < m_format_ctx->nb_streams; ++i) {
        if (m_format_ctx->streams[i]->codecpar->codec_type == getMediaType()) {
            vIdx = i;
            break;
        }
    }
    if (vIdx == -1) {
        LOG_ERROR(TAG, LogSpec(), "Fail to find stream index");
        doneDecode(env);
        return;
    }
    m_stream_index = vIdx;

    AVCodecParameters *codecPar = m_format_ctx->streams[m_stream_index]->codecpar;

    m_codec = avcodec_find_decoder(codecPar->codec_id);
    m_codec_ctx = avcodec_alloc_context3(m_codec);
    if (avcodec_parameters_to_context(m_codec_ctx, codecPar) != 0) {
        LOG_ERROR(TAG, LogSpec(), "Fail to obtain av codec context");
        doneDecode(env);
        return;
    }

    if (avcodec_open2(m_codec_ctx, m_codec, nullptr) < 0) {
        LOG_ERROR(TAG, LogSpec(), "Fail to open av codec");
        doneDecode(env);
        return;
    }

    m_duration = m_format_ctx->duration / AV_TIME_BASE * 1000;

    LOG_INFO(TAG, LogSpec(), "Decoder init success");
}

void BaseDecoder::allocFrameBuffer() {
    m_packet = av_packet_alloc();
    m_frame = av_frame_alloc();
}

void BaseDecoder::loopDecode() {
    if (STOP == m_state) {
        m_state = START;
    }

    LOG_INFO(TAG, LogSpec(), "Start loop decode");

    while (true) {
        if (m_state != DECODING &&
            m_state != START &&
            m_state != STOP) {
            wait();
            m_started_time = GetCurMsTime() - m_cur_pos;
        }

        if (m_state == STOP) {
            break;
        }

        if (-1 == m_started_time) {
            m_started_time = GetCurMsTime();
        }

        if (decodeOneFrame() != nullptr) {
            syncRender();
            render(m_frame);

            if (m_state == START) {
                m_state = PAUSE;
            }
        } else {
            LOG_INFO(TAG, LogSpec(), "m_state = %d", m_state);
            if (forSynthesizer()) {
                m_state = STOP;
            } else {
                m_state = FINISH;
            }
        }
    }
}

AVFrame *BaseDecoder::decodeOneFrame() {
    int ret = av_read_frame(m_format_ctx, m_packet);
    while (ret == 0) {
        if (m_packet->stream_index == m_stream_index) {
            switch (avcodec_send_packet(m_codec_ctx, m_packet)) {
                case AVERROR_EOF: {
                    av_packet_unref(m_packet);
                    LOG_ERROR(TAG, LogSpec(), "Decode error: %s", av_err2str(AVERROR_EOF));
                    return nullptr;
                }
                case AVERROR(EAGAIN):
                    LOG_ERROR(TAG, LogSpec(), "Decode error: %s", av_err2str(AVERROR(EAGAIN)));
                    break;
                case AVERROR(EINVAL):
                    LOG_ERROR(TAG, LogSpec(), "Decode error: %s", av_err2str(AVERROR(EINVAL)));
                    break;
                case AVERROR(ENOMEM):
                    LOG_ERROR(TAG, LogSpec(), "Decode error: %s", av_err2str(AVERROR(ENOMEM)));
                    break;
                default:
                    break;
            }

            int result = avcodec_receive_frame(m_codec_ctx, m_frame);
            if (result == 0) {
                obtainTimestamp();
                av_packet_unref(m_packet);
                return m_frame;
            } else {
                LOG_INFO(TAG, LogSpec(), "Receive frame error:%d", result);
            }
        }
        av_packet_unref(m_packet);
        ret = av_read_frame(m_format_ctx, m_packet);
    }
    av_packet_unref(m_packet);
    LOGI(TAG, "ret = %s", av_err2str(AVERROR(ret)))
    return nullptr;
}

void BaseDecoder::obtainTimestamp() {
    if (m_frame->pkt_dts != AV_NOPTS_VALUE) {
        m_cur_pos = m_packet->dts;
    } else if (m_frame->pts != AV_NOPTS_VALUE) {
        m_cur_pos = m_frame->pts;
    } else {
        m_cur_pos = 0;
    }
    m_cur_pos = (int64_t) ((m_cur_pos * av_q2d(m_format_ctx->streams[m_stream_index]->time_base)) *
                           1000);
}

void BaseDecoder:: syncRender() {
    if (forSynthesizer()) {
//        av_usleep(15000);
        return;
    }
    int64_t ct = GetCurMsTime();
    int64_t passTime = ct - m_started_time;
    if (m_cur_pos > passTime) {
        av_usleep((unsigned int) ((m_cur_pos - passTime) * 1000));
    }
}

void BaseDecoder::doneDecode(JNIEnv *env) {
    if (m_packet != nullptr) {
        av_packet_free(&m_packet);
    }
    if (m_frame != nullptr) {
        av_frame_free(&m_frame);
    }
    if (m_codec_ctx != nullptr) {
        avcodec_close(m_codec_ctx);
        avcodec_free_context(&m_codec_ctx);
    }
    if (m_format_ctx != nullptr) {
        avformat_close_input(&m_format_ctx);
        avformat_free_context(m_format_ctx);
    }
    if (m_path_ref != nullptr && m_path != nullptr) {
        env->ReleaseStringUTFChars((jstring) m_path_ref, m_path);
        env->DeleteGlobalRef(m_path_ref);
    }

    release();
}

void BaseDecoder::wait(long second) {
//    LOG_INFO(TAG, LogSpec(), "Decoder run into wait, state：%s", GetStateStr())
//    pthread_mutex_lock(&m_mutex);
//    pthread_cond_wait(&m_cond, &m_mutex);
//    pthread_mutex_unlock(&m_mutex);
}

void BaseDecoder::sendSignal() {
//    LOG_INFO(TAG, LogSpec(), "Decoder wake up, state: %s", GetStateStr())
//    pthread_mutex_lock(&m_mutex);
//    pthread_cond_signal(&m_cond);
//    pthread_mutex_unlock(&m_mutex);
}

void BaseDecoder::play() {
    m_state = DECODING;
    sendSignal();
}

void BaseDecoder::pause() {
    m_state = PAUSE;
}

void BaseDecoder::stop() {
    m_state = STOP;
    sendSignal();
}

bool BaseDecoder::isRunning() {
    return DECODING == m_state;
}

long BaseDecoder::getDuration() {
    return m_duration;
}

long BaseDecoder::getCurPos() {
    return (long) m_cur_pos;
}

