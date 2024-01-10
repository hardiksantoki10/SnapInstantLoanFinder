package com.snap.instant.loan.finder.activity.base

import android.app.Application
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class LoanApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        EventBus.builder().throwSubscriberException(BuildConfig.DEBUG).installDefaultEventBus()
        Hawk.init(this).build()
    }

}