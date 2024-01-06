package com.snap.instant.loan.finder.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.snap.instant.loan.finder.activity.ui.fragment.CalcFragment
import com.snap.instant.loan.finder.activity.ui.fragment.CategoryFragment
import com.snap.instant.loan.finder.activity.ui.fragment.HomeFragment
import com.snap.instant.loan.finder.activity.ui.fragment.ProfileFragment
import com.snap.instant.loan.finder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var categoryFragment: CategoryFragment? = null
    private var homeFragment: HomeFragment? = null
    private var calcFragment: CalcFragment? = null
    private var profileFragment: ProfileFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.frameContent.adapter = HomePagerAdapter(this)


    }

    inner class HomePagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                1 -> categoryFragment ?: CategoryFragment().apply {
                    categoryFragment = this
                }

                0 -> homeFragment ?: HomeFragment().apply {
                    homeFragment = this
                }

                2 -> calcFragment ?: CalcFragment().apply {
                    calcFragment = this
                }

                else -> profileFragment ?: ProfileFragment().apply {
                    profileFragment = this
                }
            }
        }
    }
}