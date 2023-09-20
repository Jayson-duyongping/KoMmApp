package com.jayson.komm.common.view.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.jayson.komm.common.R
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.bean.FileInfo
import com.jayson.komm.common.databinding.ActivityShowMediaBinding
import com.jayson.komm.common.ext.context
import com.jayson.komm.common.manager.ExoPlayerManager
import com.jayson.komm.common.util.BitmapUtil
import com.jayson.komm.common.util.ShareUtils
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
class ShowMediaActivity : BaseActivity() {

    companion object {
        private const val TAG = "ShowMediaActivity"
        const val INTENT_POSITION = "position"
        const val INTENT_FILES = "fileList"
    }

    private lateinit var binding: ActivityShowMediaBinding
    private var shareIv: ImageView? = null
    private var closeIv: ImageView? = null

    private var files = listOf<FileInfo>()

    override fun initView() {
        super.initView()
        binding = ActivityShowMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewPager()
        shareIv = findViewById(R.id.share_iv)
        closeIv = findViewById(R.id.close_iv)
        closeIv?.apply {
            setOnClickListener {
                finish()
            }
        }
        shareIv?.apply {
            setOnClickListener {
                val filePath = files[binding.pictureVp.currentItem].path
                ShareUtils.shareFile(this@ShowMediaActivity, filePath)
            }
        }
    }

    override fun initData() {
        super.initData()
        val byteArray = intent.getByteArrayExtra(INTENT_FILES)
        byteArray?.let {
            files = ProtoBuf.decodeFromByteArray<ArrayList<FileInfo>>(it)
        }
        val position = intent?.getIntExtra(INTENT_POSITION, 0) ?: 0
        binding.pictureVp.setCurrentItem(position, false)
        binding.exoPlayer.player = ExoPlayerManager.getPlayer(this)
    }

    private fun initViewPager() {
        // 设置ViewPager2的适配器
        binding.pictureVp.apply {
            adapter = PictureAdapter()
            // 设置ViewPager2为水平方向可滑动
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            // 预加载3
            offscreenPageLimit = 5
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ExoPlayerManager.release()
        binding.exoPlayer.player?.release()
    }

    inner class PictureAdapter : RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.layout_media, parent, false)
            return PictureViewHolder(view)
        }

        override fun onBindViewHolder(holder: PictureAdapter.PictureViewHolder, position: Int) {
            val file = files[position]
            holder.bind(file)
        }

        override fun getItemCount(): Int {
            return files.size
        }

        inner class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val imagePv = itemView.findViewById<ImageView>(R.id.image_pv)
            private val otherLl = itemView.findViewById<LinearLayout>(R.id.other_ll)
            private val titleTv = itemView.findViewById<TextView>(R.id.title_tv)
            private val typeTv = itemView.findViewById<TextView>(R.id.type_tv)
            fun bind(item: FileInfo) {
                if (item.fileType.contains("image")) {
                    imagePv.visibility = View.VISIBLE
                    otherLl.visibility = View.GONE
                    Glide.with(context)
                        .load(item.path)
                        .into(imagePv)
                } else {
                    val text = item.fileType.split("/")[0]
                    imagePv.setImageBitmap(BitmapUtil.makeBitmapWithText(text.uppercase()))
                    imagePv.visibility = View.GONE
                    otherLl.visibility = View.VISIBLE
                    titleTv.text = item.fileName
                    typeTv.text = item.fileType
                }
                titleTv.setOnClickListener {
                    if (binding.exoPlayer.visibility == View.GONE) {
                        binding.exoPlayer.visibility = View.VISIBLE
                    }
                    ExoPlayerManager.playVideo(this@ShowMediaActivity, item.path)
//                    ExoPlayerManager?.apply {
//                        if (isPlaying) {
//                            pause()
//                        } else {
//                            playVideo(this@ShowMediaActivity, item.path)
//                        }
//                    }
                }
            }
        }
    }
}