package com.example.snappy.ui.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.snappy.R

class APHistoryAdapter(
    private var entryList: ArrayList<String>
) : RecyclerView.Adapter<APHistoryAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var serialNumber = itemView.findViewById(R.id.s_no) as TextView
        var number = itemView.findViewById(R.id.number) as TextView
        var price = itemView.findViewById(R.id.price) as TextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val v1: View = layoutInflater.inflate(R.layout.history_cell_detail_ap, parent, false)
        return MyViewHolder(v1)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val entry: String = entryList[position]
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getItemCount(): Int {
        return entryList.size
    }
}

