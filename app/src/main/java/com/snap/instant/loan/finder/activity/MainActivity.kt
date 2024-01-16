package com.snap.instant.loan.finder.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.activity.base.BaseActivity
import com.snap.instant.loan.finder.activity.ui.fragment.CalcFragment
import com.snap.instant.loan.finder.activity.ui.fragment.CategoryFragment
import com.snap.instant.loan.finder.activity.ui.fragment.HomeFragment
import com.snap.instant.loan.finder.activity.ui.fragment.ProfileFragment
import com.snap.instant.loan.finder.apimodule.ApiRepository
import com.snap.instant.loan.finder.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var apiRepository: ApiRepository

    enum class TAB {
        HOME, CATEGORY, CALC, PROFILE
    }

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
        binding.frameContent.isUserInputEnabled = false
        binding.frameContent.offscreenPageLimit = 2

        binding.frameContent.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            @SuppressLint("WrongConstant")
            override fun onPageSelected(position: Int) {
            }
        })

        binding.llHome.setOnClickListener { setFragment(TAB.HOME) }
        binding.llCategory.setOnClickListener { setFragment(TAB.CATEGORY) }
        binding.llCalc.setOnClickListener { setFragment(TAB.CALC) }
        binding.llProfile.setOnClickListener { setFragment(TAB.PROFILE) }

        setFragment(TAB.HOME)
    }

    private fun setFragment(it: TAB?) {
        setTabSelection(it)
        when (it) {
            TAB.HOME -> {
                binding.frameContent.currentItem = 0
            }

            TAB.CATEGORY -> {
                binding.frameContent.currentItem = 1
            }

            TAB.CALC -> {
                binding.frameContent.currentItem = 2
            }

            else -> {
                binding.frameContent.currentItem = 3
            }
        }
    }

    private fun setTabSelection(it: TAB?) {
        binding.ivHome.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_home_icon_inactive
            )
        )
        binding.ivCalc.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_calculator_inactive
            )
        )
        binding.ivCategory.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_category_inactive
            )
        )
        binding.ivProfile.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_profile_inactive
            )
        )

        binding.txtProfile.setTextColor(getColor(R.color.color_767C98))
        binding.txtHome.setTextColor(getColor(R.color.color_767C98))
        binding.txtCalc.setTextColor(getColor(R.color.color_767C98))
        binding.txtCategory.setTextColor(getColor(R.color.color_767C98))

        if (it == TAB.HOME) {
            binding.txtHome.setTextColor(getColor(R.color.white))
            binding.ivHome.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_home_icon_active
                )
            )
        }
        if (it == TAB.CALC) {
            binding.txtCalc.setTextColor(getColor(R.color.white))
            binding.ivCalc.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_calculator_active
                )
            )
        }
        if (it == TAB.CATEGORY) {
            binding.txtCategory.setTextColor(getColor(R.color.white))
            binding.ivCategory.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_category_active
                )
            )
        }
        if (it == TAB.PROFILE) {
            binding.txtProfile.setTextColor(getColor(R.color.white))
            binding.ivProfile.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_profile_active
                )
            )
        }
    }

    inner class HomePagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int {
            return 4
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