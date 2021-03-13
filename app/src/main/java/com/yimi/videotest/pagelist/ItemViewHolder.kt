package com.yimi.videotest.pagelist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.yimi.videotest.PlayerActivity
import com.yimi.videotest.PlayerManager
import com.yimi.videotest.R
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.video_list_item.view.*

class ItemViewHolder(private val rootView: View) :
    RecyclerView.ViewHolder(rootView) {

    @SuppressLint("SetTextI18n")
    fun bind(url: String) {
        rootView.list_item_btn.text = "Video ${absoluteAdapterPosition + 1}"
        rootView.list_item_btn.setOnClickListener { startPlayerActivity(url) }
    }

    private fun startPlayerActivity(url: String) {
        val args = Bundle().apply {
            putString("url", url)
        }
        Intent(itemView.context, PlayerActivity::class.java).run {
            putExtras(args)
            itemView.context.startActivity(this)


//            PlayerManager.initPlayer(itemView.context)
//
//            PlayerManager.getPlayer()?.run {
//                rootView.player_view_small.player = this
//                PlayerManager.playWithUrl(itemView.context, url)
//            }

        }
    }
}