package com.jayson.komm.common.manager

import android.content.Context
import android.view.SurfaceView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.jayson.komm.common.util.LogUtils
import java.lang.ref.WeakReference

object ExoPlayerManager {

    private const val TAG = "ExoPlayerManager"

    private var player: WeakReference<ExoPlayer>? = null
    private var currentUri: String? = null

    @JvmStatic
    val isPlaying
        get() = getPlayer()?.isPlaying ?: false

    private fun initialize(context: Context?) {
        context?.let {
            val exoPlayer = ExoPlayer.Builder(it).build()
            player = WeakReference(exoPlayer)
        }
    }

    @JvmStatic
    fun getPlayer(context: Context? = null): ExoPlayer? {
        val py = player?.get()
        if (py == null) {
            initialize(context)
        }
        return player?.get()
    }

    @JvmStatic
    fun playVideo(context: Context, mediaUri: String, surfaceView: SurfaceView? = null) {
        kotlin.runCatching {
            val playerInstance = getPlayer(context)
            if (currentUri != null) {
                playerInstance?.play()
                return
            }
            currentUri = mediaUri
            val mediaItem = MediaItem.fromUri(mediaUri)
            playerInstance?.apply {
                surfaceView?.let {
                    setVideoSurfaceView(it)
                }
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
        }.getOrElse {
            LogUtils.e(TAG, "Player is not initialized")
        }
    }

    @JvmStatic
    fun pause() {
        getPlayer()?.apply {
            if (isPlaying) {
                pause()
            }
        }
    }

    @JvmStatic
    fun release() {
        getPlayer()?.release()
        player?.clear()
        player = null
        currentUri = null
    }
}