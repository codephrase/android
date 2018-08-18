package com.codephrase.android.viewstate

import android.os.Parcel
import android.os.Parcelable

open class FrameFragmentState : ViewState {
    constructor() : super() {

    }

    constructor(parcel: Parcel) : super(parcel) {

    }

    companion object CREATOR : Parcelable.Creator<FrameFragmentState> {
        override fun createFromParcel(parcel: Parcel): FrameFragmentState {
            return FrameFragmentState(parcel)
        }

        override fun newArray(size: Int): Array<FrameFragmentState?> {
            return arrayOfNulls(size)
        }
    }
}