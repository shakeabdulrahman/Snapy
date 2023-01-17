package com.example.snappy.view

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.snappy.data.model.PetsDetail
import com.example.snappy.ui.home.adapter.PetsListAdapter

@BindingAdapter("petsList")
fun petsList(recyclerView: RecyclerView, list: ArrayList<PetsDetail>?) {
    val adapter = recyclerView.adapter as PetsListAdapter
    adapter.submitList(list)
}

@BindingAdapter("wishList")
fun wishList(recyclerView: RecyclerView, list: ArrayList<PetsDetail>?) {
    val adapter = recyclerView.adapter as PetsListAdapter
    adapter.submitList(list)
}