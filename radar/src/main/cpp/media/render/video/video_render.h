//
// Created by fionera on 2023/4/20.
//

#ifndef BEHAVIORCHANGES_VIDEO_RENDER_H
#define BEHAVIORCHANGES_VIDEO_RENDER_H

#include <jni.h>
#include "../../one_frame.h"

class VideoRender {
public:
    virtual void initRender(JNIEnv *env, int video_width, int video_height, int *dst_size = 0) = 0;
    virtual void render(OneFrame *one_frame) = 0;
    virtual void releaseRender() = 0;
};


#endif //BEHAVIORCHANGES_VIDEO_RENDER_H
