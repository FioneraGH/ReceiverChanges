//
// Created by fionera on 2023/4/20.
//

#include "player.h"
#include "render/video/native_render/native_render.h"

Player::Player(JNIEnv *jniEnv, jstring path, jobject surface) {
    m_v_decoder = new VideoDecoder(jniEnv, path, surface);
    m_v_render = new NativeRender(jniEnv, surface);
    m_v_decoder->setRender(m_v_render);
}

Player::~Player() = default;

void Player::play() {
    if (m_v_decoder != nullptr) {
        m_v_decoder->play();
    }
}

void Player::pause() {
    if (m_v_decoder != nullptr) {
        m_v_decoder->pause();
    }
}
