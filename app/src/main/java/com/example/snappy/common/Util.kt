package com.example.snappy.common

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.snappy.R

class Util {
    companion object {
        fun loadImageFromUrl(imageView: ImageView, url: String?) {
            Glide.with(imageView)
                .load(url).placeholder(R.drawable.loading_animation).error(R.drawable.loading_img)
                .into(imageView)
        }
    }
}