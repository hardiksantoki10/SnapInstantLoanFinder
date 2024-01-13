package com.snap.instant.loan.finder.activity.ui.fragment


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.adapter.SliderAdapter
import com.snap.instant.loan.finder.databinding.FragmentHomeBinding
import com.snap.instant.loan.finder.databinding.HomeListProviderItemBinding
import kotlin.math.abs


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private val sliderHandler: Handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
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
            SliderAdapter(
                sliderItems,
                binding.viewPagerHorizontal
            )
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
    }

    private val sliderRunnable = Runnable { binding.viewPagerHorizontal.currentItem += 1 }


}



class HomeListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContentItemHolder(
            HomeListProviderItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    inner class ContentItemHolder(val binding: HomeListProviderItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
