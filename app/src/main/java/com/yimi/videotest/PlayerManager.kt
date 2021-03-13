package com.yimi.videotest

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util

object PlayerManager {

    private var exoPlayer: SimpleExoPlayer? = null

    fun initPlayer(context: Context) {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            context,
            DefaultTrackSelector(AdaptiveTrackSelection.Factory())
        )
    }

    fun playWithUrl(context: Context, url: String?) {
        require(!url.isNullOrEmpty()) { "Empty url" }
        exoPlayer?.run {
            prepare(buildMediaSource(context, Uri.parse(url)))
            playWhenReady = true
        }
    }

    fun getPlayer(): SimpleExoPlayer? {
        return exoPlayer
    }

    fun releasePlayer() {
        exoPlayer?.release()
    }

    private fun buildMediaSource(context: Context, uri: Uri): MediaSource {
        val dataSourceFactory = buildDataSourceFactory(context)
        return when (val type = Util.inferContentType(uri)) {
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
    }

    private fun buildDataSourceFactory(context: Context): DefaultDataSourceFactory {
        val appName = context.getString(R.string.app_name)
        return DefaultDataSourceFactory(
            context, DefaultHttpDataSourceFactory(
                Util.getUserAgent(context, appName)
            )
        )
    }
}