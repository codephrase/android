package com.codephrase.android.viewstate

import android.os.Parcel
import android.os.Parcelable

@Suppress("UNUSED_PARAMETER")
abstract class ViewState : Parcelable {
    constructor() {

    }

    constructor(parcel: Parcel) {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }
}