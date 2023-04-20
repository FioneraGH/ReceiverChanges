//
// Created by fionera on 2023/4/20.
//

#ifndef BEHAVIORCHANGES_I_DECODER_H
#define BEHAVIORCHANGES_I_DECODER_H

class IDecoder {
public:
    virtual void play() = 0;

    virtual void pause() = 0;

    virtual void stop() = 0;

    virtual bool isRunning() = 0;

    virtual long getDuration() = 0;

    virtual long getCurPos() = 0;
};

#endif //BEHAVIORCHANGES_I_DECODER_H
