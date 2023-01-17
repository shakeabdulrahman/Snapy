package com.example.snappy.ui.wishlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.example.snappy.base.BaseFragment
import com.example.snappy.data.model.PetsDetail
import com.example.snappy.databinding.FragmentWishlistBinding
import com.example.snappy.ui.home.adapter.PetsListAdapter


class WishListFragment: BaseFragment<FragmentWishlistBinding>(), PetsListAdapter.CallBack {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private val wishListViewModel: WishListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = wishListViewModel
        }

        binding.petsRecyclerView.adapter = PetsListAdapter(this)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wishListViewModel.myWishList.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.isNotEmpty()) {
                    binding.petsRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }

        initViews()
    }

    private fun initViews() {
        binding.layoutSearchProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(msg: String): Boolean {
                if (msg.isEmpty()) {
                    //wishListViewModel.getWishList()
                } else {
                    wishListViewModel.filter(msg)
                    binding.petsRecyclerView.adapter?.notifyDataSetChanged()
                }
                return false
            }
        })
    }

    override fun onProductCardClick(petsDetail: PetsDetail) {
        // Code here
    }

    override fun onWishHeartClick(petsDetail: PetsDetail) {
        petsDetail.apply {
            isWishListed = !isWishListed
        }

        wishListViewModel.getWishList(petsDetail)
    }
}