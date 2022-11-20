package com.example.snappy.view

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.snappy.data.model.PetsDetail
import com.example.snappy.ui.home.adapter.PetsListAdapter

@BindingAdapter("pets_list")
fun petsList(recyclerView: RecyclerView, list: ArrayList<PetsDetail>?) {
    val adapter = recyclerView.adapter as PetsListAdapter
    adapter.submitList(list)
}