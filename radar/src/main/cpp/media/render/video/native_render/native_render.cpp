//
// Created by fionera on 2023/4/20.
//

#include <cstring>
#include "native_render.h"
#include "logger.h"

NativeRender::NativeRender(JNIEnv *env, jobject surface) {
    m_surface_ref = env->NewGlobalRef(surface);
}

NativeRender::~NativeRender() = default;

void NativeRender::initRender(JNIEnv *env, int video_width, int video_height, int *dst_size) {
    m_native_window = ANativeWindow_fromSurface(env, m_surface_ref);

    int windowWidth = ANativeWindow_getWidth(m_native_window);
    int windowHeight = ANativeWindow_getHeight(m_native_window);

    m_dst_w = windowWidth;
    m_dst_h = m_dst_w * video_height / video_width;
    if (m_dst_h > windowHeight) {
        m_dst_h = windowHeight;
        m_dst_w = windowHeight * video_width / video_height;
    }

    LOGE(TAG, "windowW: %d, windowH: %d, dstVideoW: %d, dstVideoH: %d",
         windowWidth, windowHeight, m_dst_w, m_dst_h);

    ANativeWindow_setBuffersGeometry(m_native_window, windowWidth, windowHeight,
                                     WINDOW_FORMAT_RGBA_8888);

    dst_size[0] = m_dst_w;
    dst_size[1] = m_dst_h;
}

void NativeRender::render(OneFrame *one_frame) {
    ANativeWindow_lock(m_native_window, &m_out_buffer, nullptr);

    auto *dst = (uint8_t *) m_out_buffer.bits;

    int dstStride = m_out_buffer.stride * 4;
    int srcStride = one_frame->line_size;

    for (int h = 0; h < m_dst_h; ++h) {
        memcpy(dst + h * dstStride, one_frame->data + h * srcStride, srcStride);
    }

    ANativeWindow_unlockAndPost(m_native_window);
}

void NativeRender::releaseRender() {
    if (m_native_window != nullptr) {
        ANativeWindow_release(m_native_window);
    }
    av_free(&m_out_buffer);
}
