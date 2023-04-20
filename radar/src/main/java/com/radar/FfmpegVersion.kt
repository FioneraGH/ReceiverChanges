package com.radar

import android.view.Surface

/**
 * FFmpegVersion
 *
 * @author fionera
 * @date 2023/4/20 in BehaviorChanges.
 */
class FfmpegVersion {

    private var handle = 0L

    fun makePlayer(path: String, surface: Surface) {
        handle = createPlayer(path, surface)
    }

    fun playPlayer() {
        if (handle == 0L) {
            return
        }
        play(handle)
    }

    fun pausePlayer() {
        if (handle == 0L) {
            return
        }
        pause(handle)
    }

    external fun getVersion(): String

    external fun createPlayer(path: String, surface: Surface): Long

    external fun play(player: Long)
    external fun pause(player: Long)

    companion object {
        // Used to load the 'radar' library on application startup.
        init {
            System.loadLibrary("radar")
        }
    }
}