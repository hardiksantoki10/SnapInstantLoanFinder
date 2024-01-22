package com.snap.instant.loan.finder.activity.ui.fragment


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.activity.MainActivity
import com.snap.instant.loan.finder.activity.base.BaseFragment
import com.snap.instant.loan.finder.adapter.SliderAdapter
import com.snap.instant.loan.finder.apimodule.ApiRepository
import com.snap.instant.loan.finder.apimodule.pojoModel.HomeRes
import com.snap.instant.loan.finder.apimodule.pojoModel.LoginRes
import com.snap.instant.loan.finder.apimodule.pojoModel.ProfileRes
import com.snap.instant.loan.finder.databinding.FragmentHomeBinding
import com.snap.instant.loan.finder.databinding.HomeListProviderItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import javax.inject.Inject
import kotlin.math.abs
class HomeFragment : BaseFragment() {

    private var companiesList: List<HomeRes.Company?>? = emptyList()
    private var homeList: List<HomeRes.Home?>? = emptyList()

    lateinit var binding: FragmentHomeBinding
    private val sliderHandler: Handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
    }

    private fun initList() {

        binding.rvLoanProviderList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLoanProviderList.adapter = HomeListAdapter()

        val sliderItems = ArrayList<Int>()
        sliderItems.add(R.drawable.ic_slider_1)
        sliderItems.add(R.drawable.ic_slider_2)
        sliderItems.add(R.drawable.ic_slider_3)
        sliderItems.add(R.drawable.ic_slider_4)

        binding.viewPagerHorizontal.setAdapter(
            SliderAdapter(sliderItems, binding.viewPagerHorizontal)
        )

        binding.viewPagerHorizontal.clipToPadding = false
        binding.viewPagerHorizontal.clipChildren = false
        binding.viewPagerHorizontal.setOffscreenPageLimit(3)
        binding.viewPagerHorizontal.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(18))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.90f + r * 0.1f
        }

        binding.viewPagerHorizontal.setPageTransformer(compositePageTransformer)
        binding.viewPagerHorizontal.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 2000) // slide duration 2 seconds
            }
        })

        getHomeData()
    }

    private fun getHomeData() {
        showProgress()
        lifecycleScope.launch(Dispatchers.IO) {
            (activity as MainActivity).apiRepository.makeLogin().onSuccess {
                lifecycleScope.launch(Dispatchers.Main) {
                    hideProgress()
                    val res = it.string()
                    Log.d("getHomeData", "onSuccess responce $res")
                    val homeRes = Gson().fromJson(res, HomeRes::class.java)

                    if (homeRes.success == true) {
                        setData(homeRes)
                    }
                }
            }.onFailure {
                lifecycleScope.launch(Dispatchers.Main) {
                    hideProgress()
                    val res = it.message
                    Log.d("getHomeData", "onFailure responce ${res}")
                }
            }
        }
    }

    private fun setData(homeRes: HomeRes?) {
        companiesList = homeRes?.companies
        homeList = homeRes?.home
        binding.rvLoanProviderList.adapter?.notifyDataSetChanged()
        getProfileData()
    }

    private fun getProfileData() {
        showProgress()
        lifecycleScope.launch(Dispatchers.IO) {
            (activity as MainActivity).apiRepository.getProfile().onSuccess {
                lifecycleScope.launch(Dispatchers.Main) {
                    hideProgress()
                    val res = it.string()
                    Log.d("getProfileData", "onSuccess responce $res")

                    val homeRes = Gson().fromJson(res, ProfileRes::class.java)

                    if (homeRes.success == true) {
                        setProfileData(homeRes)
                    }
                }
            }.onFailure {
                lifecycleScope.launch(Dispatchers.Main) {
                    hideProgress()
                    Log.d("getProfileData", "onFailure responce ${it.message}")
                }
            }

        }
    }

    private fun setProfileData(homeRes: ProfileRes?) {
        homeRes?.let {
            Log.d("setProfileData","image ${homeRes.data?.image}")
            Glide.with(this).load(homeRes.data?.image).into(binding.ivProfileImage)
            binding.txtName.text = homeRes.data?.firstName.plus(" ").plus(homeRes.data?.lastName)
        }
    }

    private val sliderRunnable = Runnable { binding.viewPagerHorizontal.currentItem += 1 }
    inner class HomeListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ContentItemHolder(
                HomeListProviderItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun getItemCount(): Int {
            return companiesList?.size!!
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder as ContentItemHolder

            val imageFile = companiesList?.get(holder.adapterPosition)
            Glide.with(holder.binding.ivBankImage).load(imageFile?.image.orEmpty())
                .into(holder.binding.ivBankImage)
            holder.binding.txtBankName.text = imageFile?.name.orEmpty()
            holder.binding.txtStartingFrom.text = imageFile?.subtitle.orEmpty()

            holder.binding.ivArrow.setOnClickListener {


            }
        }

        inner class ContentItemHolder(val binding: HomeListProviderItemBinding) :
            RecyclerView.ViewHolder(binding.root)
    }
}

