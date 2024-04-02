package com.snap.instant.loan.finder.activity.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.activity.MainActivity
import com.snap.instant.loan.finder.activity.ProfileUpdateActivity
import com.snap.instant.loan.finder.activity.base.BaseFragment
import com.snap.instant.loan.finder.activity.base.UserLoginDetail
import com.snap.instant.loan.finder.databinding.FragmentProfileBinding
import com.snap.instant.loan.finder.helper.DialogUtils
import com.snap.instant.loan.finder.helper.DialogUtils.openApp
import com.snap.instant.loan.finder.helper.EventModel
import com.snap.instant.loan.finder.helper.EventType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject


class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        Log.d("onEventReceived", "ProfileFragment")
        if (event?.eventName == EventType.PROFILE_UPDATE) {
            Log.d("onEventReceived", "ProfileFragment")
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
        binding.llRateus.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + context?.packageName)
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id =" + context?.packageName)
                    )
                )
            }
        }


        binding.llShare.setOnClickListener {
            makeShareIntent()
        }


        binding.llLogout.setOnClickListener {
            DialogUtils.showTwoOptionDialog(
                requireContext(),
                "Alert",
                "Are you sure to want logout?",
                "Yes",
                "No"
            ) {
                Hawk.deleteAll()
                Handler(Looper.getMainLooper()).postDelayed( {
                    requireActivity().openApp()
                },250L)

            }
        }
        binding.llRemove.setOnClickListener {
            deleteProfile()
        }
        binding.llPrivacy.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/")
                )
            )
        }
        binding.llTerm.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/")
                )
            )
        }
    }

    private fun deleteProfile() {
        lifecycleScope.launch(Dispatchers.IO) {
            (activity as MainActivity).apiRepository.deleteProfile().onSuccess {
                lifecycleScope.launch(Dispatchers.Main) {
                    val response = it.string()
                    JSONObject(response).let { jsonObject ->
                        if (jsonObject.has("success")) {
                            val message = jsonObject.getString("message")
                            if (jsonObject.getBoolean("success")) {
                                DialogUtils.showOkDialog(
                                    requireContext(),
                                    "Alert",
                                    "Your account was deleted"
                                ) {
                                    Hawk.deleteAll()
                                    Handler(Looper.getMainLooper()).postDelayed( {
                                        requireActivity().openApp()
                                    },250L)
                                }
                            } else {
                                (activity as MainActivity).showToast(requireContext(), message)
                            }
                        }
                    }
                }
            }.onFailure {

            }
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

    private fun makeShareIntent() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(
            Intent.EXTRA_SUBJECT, getString(R.string.app_name)
        )
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Best app for finding loan provider"
                    + "\n"
                    + "https://play.google.com/store/apps/details?id =" + context?.packageName
        )
        shareIntent.type = "text/plain"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(shareIntent, "send"))
    }
}