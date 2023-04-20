//
// Created by fionera on 2023/4/20.
//

#ifndef BEHAVIORCHANGES_V_DECODER_H
#define BEHAVIORCHANGES_V_DECODER_H

#include "../base_decoder.h"
#include "../../render/video/video_render.h"

#include <jni.h>

extern "C" {
#include <libavutil/imgutils.h>
#include <libswscale/swscale.h>
}

class VideoDecoder: public BaseDecoder {
private:
    const char *TAG = "VideoDecoder";

    const AVPixelFormat DST_FORMAT = AV_PIX_FMT_RGBA;
    AVFrame *m_rgb_frame = nullptr;
    uint8_t *m_buf_for_rgb_frame = nullptr;
    SwsContext *m_sws_ctx = nullptr;
    VideoRender *m_video_render = nullptr;

    // init for warn
    int m_dst_w = 0;
    int m_dst_h = 0;

    void initRender(JNIEnv *env);
    void initBuffer();
    void initSws();

public:
    VideoDecoder(JNIEnv *env, jstring path, bool for_synthesizer = false);
    ~VideoDecoder();

    void setRender(VideoRender *render);

protected:
    FFAVMediaType getMediaType() override {
        return AVMEDIA_TYPE_VIDEO;
    }

    bool needLoopDecode() override;
    void prepare(JNIEnv *env) override;
    void render(AVFrame *frame) override;
    void release() override;

    const char *const LogSpec() override {
        return "VIDEO";
    }
};


#endif //BEHAVIORCHANGES_V_DECODER_H
