//
// Created by fionera on 2023/4/20.
//

#ifndef BEHAVIORCHANGES_BASE_DECODER_H
#define BEHAVIORCHANGES_BASE_DECODER_H

#include <jni.h>
#include <memory>
#include <thread>
#include "i_decoder.h"
#include "decode_state.h"

extern "C" {
#include <libavformat/avformat.h>
#include <libavcodec/avcodec.h>
#include <libavutil/frame.h>
#include <libavutil/time.h>
}

class BaseDecoder : public IDecoder {
private:
    const char *TAG = "BaseDecoder";

    // region FFMPEG

    AVFormatContext *m_format_ctx = nullptr;
    AVCodec *m_codec = nullptr;
    AVCodecContext *m_codec_ctx = nullptr;
    AVPacket *m_packet = nullptr;
    AVFrame *m_frame = nullptr;

    int64_t m_cur_pos = 0;
    long m_duration = 0;
    int64_t m_started_time = -1;
    DecodeState m_state = STOP;
    int m_stream_index = -1;

    // endregion

    // region Internal Fun

    void initDecoder(JNIEnv *env, jstring path);

    void initFfmpegDecoder(JNIEnv *env);

    void allocFrameBuffer();

    void loopDecode();

    void obtainTimestamp();

    void doneDecode(JNIEnv *env);

    void syncRender();

    // endregion

    // region Thread

    JavaVM  *m_jvm_for_thread = nullptr;
    jobject m_path_ref = nullptr;
    const char *m_path = nullptr;

    pthread_mutex_t  m_mutex = PTHREAD_MUTEX_INITIALIZER;
    pthread_cond_t m_cond = PTHREAD_COND_INITIALIZER;

    bool m_for_synthesizer = false;

    void createDecodeThread();

    static void decode(const std::shared_ptr<BaseDecoder>& that);

    // endregion

public:
    BaseDecoder(JNIEnv *env, jstring path, bool for_synthesizer);
    virtual ~BaseDecoder();

    int width() {
        return m_codec_ctx->width;
    }

    int height() {
        return m_codec_ctx->height;
    }

    long duration() {
        return m_duration;
    }

    // region Override

    void play() override;
    void pause() override;
    void stop() override;
    bool isRunning() override;
    long getDuration() override;
    long getCurPos() override;

    // endregion

protected:
    bool forSynthesizer() {
        return m_for_synthesizer;
    }

    const char *path() {
        return m_path;
    }

    AVCodecContext *codec_cxt() {
        return m_codec_ctx;
    }

    AVPixelFormat video_pixel_format() {
        return m_codec_ctx->pix_fmt;
    }

    AVRational time_base() {
        return m_format_ctx->streams[m_stream_index]->time_base;
    }

    AVFrame* decodeOneFrame();

    virtual FFAVMediaType getMediaType() = 0;

    virtual bool needLoopDecode() = 0;
    virtual void prepare(JNIEnv *env) = 0;
    virtual void render(AVFrame *frame) = 0;
    virtual void release() = 0;

    virtual const char *const LogSpec() = 0;

    void wait(long second = 0);
    void sendSignal();
};


#endif //BEHAVIORCHANGES_BASE_DECODER_H
