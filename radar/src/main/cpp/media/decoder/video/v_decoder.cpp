//
// Created by fionera on 2023/4/20.
//

#include "v_decoder.h"
#include "logger.h"

VideoDecoder::VideoDecoder(JNIEnv *env, jstring path, bool for_synthesizer)
        : BaseDecoder(env, path, for_synthesizer) {

}

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdelete-abstract-non-virtual-dtor"

VideoDecoder::~VideoDecoder() {
    // is NativeRender
    delete m_video_render;
}

#pragma clang diagnostic pop

void VideoDecoder::prepare(JNIEnv *env) {
    initRender(env);
    initBuffer();
    initSws();
}

void VideoDecoder::initBuffer() {
    m_rgb_frame = av_frame_alloc();
    int numBytes = av_image_get_buffer_size(DST_FORMAT, m_dst_w, m_dst_h, 1);
    m_buf_for_rgb_frame = (uint8_t *) av_malloc(numBytes * sizeof(uint8_t));
    av_image_fill_arrays(m_rgb_frame->data, m_rgb_frame->linesize, m_buf_for_rgb_frame,
                         DST_FORMAT, m_dst_w, m_dst_h, 1);

}

void VideoDecoder::initSws() {
    m_sws_ctx = sws_getContext(width(), height(), video_pixel_format(),
                               m_dst_w, m_dst_h, DST_FORMAT,
                               SWS_FAST_BILINEAR, nullptr, nullptr, nullptr);
}

void VideoDecoder::release() {
    LOGE(TAG, "[VIDEO] release")
    if (m_rgb_frame != nullptr) {
        av_frame_free(&m_rgb_frame);
        m_rgb_frame = nullptr;
    }
    if (m_buf_for_rgb_frame != nullptr) {
        free(m_buf_for_rgb_frame);
        m_buf_for_rgb_frame = nullptr;
    }
    if (m_sws_ctx != nullptr) {
        sws_freeContext(m_sws_ctx);
        m_sws_ctx = nullptr;
    }
    if (m_video_render != nullptr) {
        m_video_render->releaseRender();
        m_video_render = nullptr;
    }
}

bool VideoDecoder::needLoopDecode() {
    return true;
}

void VideoDecoder::setRender(VideoRender *render) {
    m_video_render = render;
}

void VideoDecoder::initRender(JNIEnv *env) {
    if (m_video_render != nullptr) {
        LOGI(TAG, "codec %d, %d",  width(), height())

        int dst_size[] = {-1, -1};
        m_video_render->initRender(env, width(), height(), dst_size);

        m_dst_w = dst_size[0];
        m_dst_h = dst_size[1];

        if (m_dst_w == -1) {
            m_dst_w = width();
        }
        if (m_dst_h == -1) {
            m_dst_w = height();
        }
        LOGI(TAG, "dst %d, %d", m_dst_w, m_dst_h)
    } else {
        LOGE(TAG, "Init render error, you should call SetRender first!")
    }
}

void VideoDecoder::render(AVFrame *frame) {
    sws_scale(m_sws_ctx, frame->data, frame->linesize, 0, height(), m_rgb_frame->data,
              m_rgb_frame->linesize);
    auto *oneFrame = new OneFrame(m_rgb_frame->data[0], m_rgb_frame->linesize[0], frame->pts,
                                      time_base(), nullptr, false);
    m_video_render->render(oneFrame);
}
