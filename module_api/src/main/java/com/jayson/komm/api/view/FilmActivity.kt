package com.jayson.komm.api.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jayson.komm.api.R
import com.jayson.komm.api.bean.Film
import com.jayson.komm.api.databinding.ActivityFilmBinding
import com.jayson.komm.api.model.FilmViewModel
import com.jayson.komm.api.view.adapter.FilmItemAdapter
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.LogUtils

class FilmActivity : BaseActivity() {

    companion object {
        private const val TAG = "FilmActivity"
    }

    private val filmViewModel by lazy {
        ViewModelProvider(this)[FilmViewModel::class.java]
    }

    private var binding: ActivityFilmBinding? = null
    private var itemAdapter: FilmItemAdapter? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_film)
        binding?.lifecycleOwner = this

        // 绑定ViewModel
        binding?.filmViewModel = filmViewModel

        initRecycleView()
    }

    private fun initRecycleView() {
        itemAdapter = FilmItemAdapter()
        itemAdapter?.setOnItemClickListener(object : FilmItemAdapter.OnItemClickListener {
            override fun onItemClick(item: Film, position: Int) {
                //FilmDialogFragment(item).show(supportFragmentManager, "")
            }
        })
        binding?.filmsRv?.apply {
            layoutManager = LinearLayoutManager(this@FilmActivity)
            adapter = itemAdapter
        }
        filmViewModel.filmsLiveData.observe(this) { filmsInfo ->
            LogUtils.d(TAG, "initRecycleView, updateUI-filmsInfo: $filmsInfo")
            // 在 UI 层接收数据更新
            if (filmsInfo.isNullOrEmpty()) {
                Toast.makeText(this@FilmActivity, "请稍后再试", Toast.LENGTH_SHORT).show()
                return@observe
            }
            itemAdapter?.submitList(filmsInfo.toMutableList())
        }
    }
}