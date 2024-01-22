package com.snap.instant.loan.finder.activity.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.activity.MainActivity
import com.snap.instant.loan.finder.activity.base.BaseFragment
import com.snap.instant.loan.finder.apimodule.pojoModel.ProfileRes
import com.snap.instant.loan.finder.databinding.FragmentProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.bind(
            inflater.inflate(
                R.layout.fragment_profile,
                container,
                false
            )
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                        setData(homeRes)
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

    private fun setData(homeRes: ProfileRes?) {
        homeRes?.let {
            Glide.with(this).load(homeRes.data?.image).into(binding.ivEditProfile)
            binding.txtName.text = homeRes.data?.firstName.plus(" ").plus(homeRes.data?.lastName)
            binding.txtNumber.text = homeRes.data?.contact
        }
    }
}