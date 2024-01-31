package com.snap.instant.loan.finder.helper

import android.os.Parcelable
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventModel(
    var eventName: EventType, var data: @RawValue Any?, var needUpdate: Boolean = false
) : Parcelable
