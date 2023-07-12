package com.jayson.komm.home.ui.fragment

import android.content.Intent
import android.net.Uri
import android.view.View
import com.jayson.komm.common.base.BaseFragment
import com.jayson.komm.common.util.ToastUtil
import com.jayson.komm.common.view.custom.FlowLayout
import com.jayson.komm.home.R
import com.jayson.komm.home.databinding.FragmentGameBinding


class GameFragment : BaseFragment() {

    companion object {
        private const val TAG = "GameFragment"

        // url
        private const val YANYUN_VIDEO_URL =
            "https://nie.v.netease.com/nie/2023/0529/4e8566119e852c0b7697c8c3349402cc.mp4"
        private const val BAIMIAN_VIDEO_URL =
            "https://assets.papegames.com/nikkiweb/papegame/bmqxcn/material/enrh31pr/home-pv-cn.mp4"
    }

    private lateinit var binding: FragmentGameBinding

    private val tags = listOf(
        FlowLayout.Tag(name = "燕云十六声", linkUrl = YANYUN_VIDEO_URL),
        FlowLayout.Tag(name = "百面千相", linkUrl = BAIMIAN_VIDEO_URL),
        FlowLayout.Tag(name = "影之刃零", linkUrl = "")
    )

    override fun getLayoutRes(): Int {
        return R.layout.fragment_game
    }

    override fun initView(view: View) {
        super.initView(view)
        binding = FragmentGameBinding.bind(view)
        initTagView()
    }

    private fun initTagView() {
        binding.flowLayout.addTags(tags, object : FlowLayout.OnTagClickListener {
            override fun onTagClick(tag: FlowLayout.Tag) {
                tagClick(tag)
            }
        })
    }

    private fun tagClick(tag: FlowLayout.Tag) {
        if (tag.linkUrl.isEmpty()) {
            ToastUtil.show(context, "暂无视频源")
            return
        }
        // 调用本机可使用的播放器
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(tag.linkUrl), "video/*")
        startActivity(intent)
    }
}