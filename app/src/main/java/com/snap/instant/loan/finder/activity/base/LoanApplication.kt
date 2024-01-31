package com.snap.instant.loan.finder.activity.base

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.snap.instant.loan.finder.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import org.greenrobot.eventbus.EventBus


@HiltAndroidApp
class LoanApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        EventBus.builder().throwSubscriberException(BuildConfig.DEBUG).installDefaultEventBus()
        Hawk.init(this).build()
    }

}