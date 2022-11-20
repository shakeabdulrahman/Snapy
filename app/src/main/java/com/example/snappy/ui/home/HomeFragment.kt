package com.example.snappy.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.snappy.base.BaseFragment
import com.example.snappy.data.model.PetsDetail
import com.example.snappy.databinding.FragmentHomeBinding
import com.example.snappy.ui.home.adapter.BannerAdapter
import com.example.snappy.ui.home.adapter.PetsListAdapter
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment: BaseFragment<FragmentHomeBinding>(), PetsListAdapter.CallBack {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()

    lateinit var bannerAdapter: BannerAdapter

    /** tab indicator for poster **/
    private lateinit var handler: Handler
    private var runnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = homeViewModel
        }

        binding.recyclerViewPetsList.adapter = PetsListAdapter(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.petsList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.recyclerViewPetsList.adapter?.notifyDataSetChanged()
            }
        }

        initViews()
    }

    private fun initViews() {
        handler = Handler(Looper.getMainLooper()) //to initialize handler for sliding
        setUpViewPager()
        homeViewModel.loadDummyData()
    }

    private fun setUpViewPager() {
        val viewPager = binding.bannerViewPager

        bannerAdapter = BannerAdapter(homeViewModel.bannerImageUrl)
        viewPager.adapter = bannerAdapter

        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // OnClick code here
            }
        })

        /** tab layout for dot indicator **/
        TabLayoutMediator(binding.bannerTabs, viewPager) { _, _ ->
            // tab.text = fragmentBannerList[position].title
        }.attach()

        //this code is user to slide the banner automatically after 5sec
        var currentPage = 0

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (currentPage == homeViewModel.bannerImageUrl.size) {
                    currentPage = 0
                }
                viewPager.setCurrentItem(currentPage++, true)
                runnable = this
                handler.postDelayed(runnable!!, 5000)
            }
        }, 1500)
    }

    override fun onProductCardClick(petsDetail: PetsDetail) {
        // Code here
    }

    override fun onWishHeartClick(petsDetail: PetsDetail) {
        // code here
    }
}