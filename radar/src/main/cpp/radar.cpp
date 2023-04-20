//
// Created by fionera on 2023/4/20.
//
#include <jni.h>
#include "media/player.h"

extern "C" {
#include <libavcodec/version.h>
#include <libavcodec/avcodec.h>
#include <libavformat/version.h>
#include <libavutil/version.h>
#include <libavfilter/version.h>
#include <libswresample/version.h>
#include <libswscale/version.h>
}

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL
Java_com_radar_FfmpegVersion_getVersion(JNIEnv *env, jobject thiz) {

    char strBuffer[1024 * 4] = {0};
    strcat(strBuffer, "\nlibavcodec:");
    strcat(strBuffer, AV_STRINGIFY(LIBAVCODEC_VERSION));
    strcat(strBuffer, "\nlibavformat:");
    strcat(strBuffer, AV_STRINGIFY(LIBAVFORMAT_VERSION));
    strcat(strBuffer, "\nlibavutil:");
    strcat(strBuffer, AV_STRINGIFY(LIBAVUTIL_VERSION));
    strcat(strBuffer, "\nlibavfilter:");
    strcat(strBuffer, AV_STRINGIFY(LIBAVFILTER_VERSION));
    strcat(strBuffer, "\nlibswresample:");
    strcat(strBuffer, AV_STRINGIFY(LIBSWRESAMPLE_VERSION));
    strcat(strBuffer, "\nlibswscale:");
    strcat(strBuffer, AV_STRINGIFY(LIBSWSCALE_VERSION));
    return env->NewStringUTF(strBuffer);
}

JNIEXPORT jlong JNICALL
Java_com_radar_FfmpegVersion_createPlayer(JNIEnv *env, jobject thiz, jstring path,
                                          jobject surface) {
    auto *p = new Player(env, path, surface);
    return (jlong) p;
}

JNIEXPORT void JNICALL
Java_com_radar_FfmpegVersion_play(JNIEnv *env, jobject thiz, jlong player) {
    auto *p = (Player *) player;
    p->play();
}

JNIEXPORT void JNICALL
Java_com_radar_FfmpegVersion_pause(JNIEnv *env, jobject thiz, jlong player) {
    auto *p = (Player *) player;
    p->pause();
}

#ifdef __cplusplus
}
#endif

