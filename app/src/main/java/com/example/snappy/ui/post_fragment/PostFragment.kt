package com.example.snappy.ui.post_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.example.snappy.base.BaseFragment
import com.example.snappy.data.model.PetsDetail
import com.example.snappy.databinding.FragmentPostBinding
import com.example.snappy.databinding.FragmentWishlistBinding
import com.example.snappy.ui.home.adapter.PetsListAdapter
import com.example.snappy.ui.wishlist.WishListViewModel


class PostFragment: BaseFragment<FragmentPostBinding>() {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private val postViewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initViews()
    }

    private fun initViews() {
        binding.buttonTestAdd.setOnClickListener {
            val name = binding.name.text.toString()
            val age = binding.age.text.toString()
            val location = binding.location.text.toString()

            val data = PetsDetail(name = name, age = age, location = location)
            postViewModel.addPet(data)
        }
    }
}