package com.example.snappy.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.snappy.common.Util.Companion.loadImageFromUrl
import com.example.snappy.data.model.PetsDetail
import com.example.snappy.databinding.PetsListCellBinding

class PetsListAdapter(private var callBack: CallBack) : ListAdapter<PetsDetail, PetsListAdapter.ProductViewHolder>(DiffCallback) {

    class ProductViewHolder(
        private var binding: PetsListCellBinding,
        listener: CallBack
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: PetsDetail) {
            binding.petsCard = data
            binding.executePendingBindings()
            loadImageFromUrl(binding.playerImage, data.image)
        }

        init {
            binding.parentLayout.setOnClickListener {
                binding.petsCard?.let { petsData ->
                    listener.onProductCardClick(petsData)
                }
            }

            binding.productFavorInButton.setOnClickListener {
                binding.petsCard?.let { petsData ->
                    listener.onWishHeartClick(petsData)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PetsDetail>() {
        override fun areItemsTheSame(oldItem: PetsDetail, newItem: PetsDetail): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: PetsDetail, newItem: PetsDetail): Boolean {
            return oldItem.mobileNumber == newItem.mobileNumber
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        return ProductViewHolder(
            PetsListCellBinding.inflate(LayoutInflater.from(parent.context)),
            callBack
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val ecommerceDeals = getItem(position)
        holder.bind(ecommerceDeals)
    }

    interface CallBack {
        fun onProductCardClick(petsDetail: PetsDetail)
        fun onWishHeartClick(petsDetail: PetsDetail)
    }
}
