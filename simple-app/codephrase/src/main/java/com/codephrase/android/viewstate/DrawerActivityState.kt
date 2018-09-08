package com.codephrase.android.viewstate

import android.os.Parcel
import android.os.Parcelable

open class DrawerActivityState : FrameActivityState {
    var homeNavigationId = 0
    var selectedNavigationId = 0

    constructor() : super() {

    }

    constructor(parcel: Parcel) : super(parcel) {
        homeNavigationId = parcel.readInt()
        selectedNavigationId = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)

        parcel.writeInt(homeNavigationId)
        parcel.writeInt(selectedNavigationId)
    }

    companion object CREATOR : Parcelable.Creator<DrawerActivityState> {
        override fun createFromParcel(parcel: Parcel): DrawerActivityState {
            return DrawerActivityState(parcel)
        }

        override fun newArray(size: Int): Array<DrawerActivityState?> {
            return arrayOfNulls(size)
        }
    }
}