package com.snap.instant.loan.finder.activity.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.activity.ProfileUpdateActivity
import com.snap.instant.loan.finder.activity.base.BaseFragment
import com.snap.instant.loan.finder.activity.base.UserLoginDetail
import com.snap.instant.loan.finder.databinding.FragmentProfileBinding
import com.snap.instant.loan.finder.helper.EventModel
import com.snap.instant.loan.finder.helper.EventType
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
        setProfileData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventReceived(event: EventModel?) {
        Log.d("onEventReceived","ProfileFragment")
        if (event?.eventName == EventType.PROFILE_UPDATE) {
            Log.d("onEventReceived","ProfileFragment")
            setProfileData()
        }
    }

    private fun setProfileData() {

        Glide.with(this).load(Hawk.get(UserLoginDetail.IMAGE, ""))
            .placeholder(R.drawable.ic_profile_placeholder)
            .error(R.drawable.ic_profile_placeholder).into(binding.ivUserImage)
        binding.txtName.text = Hawk.get(UserLoginDetail.FIRST_NAME, "").plus(" ")
            .plus(Hawk.get(UserLoginDetail.LAST_NAME, ""))
        binding.txtNumber.text = Hawk.get(UserLoginDetail.CONTACT, "")

        binding.llProfile.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileUpdateActivity::class.java))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }
}