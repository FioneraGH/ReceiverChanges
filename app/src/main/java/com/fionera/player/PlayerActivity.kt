package com.fionera.player

import android.animation.*
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.fionera.databinding.ActivityPlayerBinding
import com.radar.FfmpegVersion

/**
 * MainActivity
 * Created by fionera on 2019-05-17 in ReceiverChanges.
 */
class PlayerActivity : AppCompatActivity() {

    private lateinit var activityPlayerBinding: ActivityPlayerBinding

    private val player = FfmpegVersion()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPlayerBinding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(activityPlayerBinding.root)

        activityPlayerBinding.tvVersion.text = "Version:"
        activityPlayerBinding.tvVersion.append(player.getVersion())

        requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 12768)

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        activityPlayerBinding.surfacePreview.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("SdCardPath")
            override fun surfaceCreated(holder: SurfaceHolder) {
                player.createPlayer("/sdcard/1.mp4", holder.surface)

                activityPlayerBinding.surfacePreview.postDelayed({
                    player.playPlayer()
                }, 1000)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                player.pausePlayer()
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}