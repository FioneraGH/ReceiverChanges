//
// Created by fionera on 2023/4/20.
//

#ifndef BEHAVIORCHANGES_NATIVE_RENDER_H
#define BEHAVIORCHANGES_NATIVE_RENDER_H

#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <jni.h>

#include "../video_render.h"

extern "C" {
#include <libavutil/mem.h>
}

class NativeRender : public VideoRender {
private:
    const char *TAG = "NativeRender";

    jobject m_surface_ref = nullptr;

    ANativeWindow_Buffer m_out_buffer;
    ANativeWindow *m_native_window = nullptr;

    int m_dst_w = 0;
    int m_dst_h = 0;

public:
    NativeRender(JNIEnv *env, jobject surface);
    virtual ~NativeRender();

    void initRender(JNIEnv *env, int video_width, int video_height, int *dst_size) override;

    void render(OneFrame *one_frame) override;

    void releaseRender() override;
};

#endif //BEHAVIORCHANGES_NATIVE_RENDER_H
