package com.example.snappy.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.snappy.R
import com.example.snappy.common.Util.Companion.loadImageFromUrl

class BannerAdapter(private val imageList: ArrayList<String>) :
    RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.banner_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image: String = imageList[position]
        loadImageFromUrl(holder.images, image)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var images: ImageView = itemView.findViewById(R.id.banner_image)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}
