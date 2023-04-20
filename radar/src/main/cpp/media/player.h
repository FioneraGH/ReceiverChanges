//
// Created by fionera on 2023/4/20.
//

#ifndef BEHAVIORCHANGES_PLAYER_H
#define BEHAVIORCHANGES_PLAYER_H


#include "decoder/video/v_decoder.h"

class Player {
private:
    VideoDecoder *m_v_decoder;
    VideoRender *m_v_render;

public:
    Player(JNIEnv *jniEnv, jstring path, jobject surface);

    ~Player();

    void play();

    void pause();
};

#endif //BEHAVIORCHANGES_PLAYER_H
