package com.jayson.komm.home.ui.fragment

import android.view.View
import com.jayson.komm.common.base.BaseFragment
import com.jayson.komm.common.bean.Banner
import com.jayson.komm.common.bean.Card
import com.jayson.komm.common.ext.replaceFragment
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.home.R
import com.jayson.komm.home.databinding.FragmentRecommendBinding

class RecommendFragment : BaseFragment() {

    companion object {
        private const val TAG = "RecommendFragment"
    }

    private lateinit var binding: FragmentRecommendBinding

    private val banners = listOf(
        Banner(imageResource = R.mipmap.banner_game_1),
        Banner(imageResource = R.mipmap.banner_game_2),
        Banner(imageResource = R.mipmap.banner_game_3_1)
    )

    private val cards = listOf(
        Card(title = "Card 1", imageResource = R.mipmap.card_h_1),
        Card(title = "Card 2", imageResource = R.mipmap.card_h_2),
        Card(title = "Card 3", imageResource = R.mipmap.card_h_3),
        Card(title = "Card 4", imageResource = R.mipmap.card_h_4),
        Card(title = "Card 5", imageResource = R.mipmap.card_h_6),
    )


    override fun getLayoutRes(): Int {
        return R.layout.fragment_recommend
    }

    override fun initView(view: View) {
        super.initView(view)
        binding = FragmentRecommendBinding.bind(view)
        binding.bannerView.initBanner(banners)
        context?.let {
            binding.horizontalCard.apply {
                initCardView(cards)
                configCard(isImageBackGround = true, isShowText = true)
            }
        }
        activity?.replaceFragment(R.id.recommend_container, RecommendListFrag())
    }

    override fun onResume() {
        super.onResume()
        LogUtils.d(TAG, "onResume")
        // 启动自动轮播
        binding.bannerView.startAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        LogUtils.d(TAG, "onPause")
        // 停止自动轮播
        binding.bannerView.stopAutoPlay()
    }
}