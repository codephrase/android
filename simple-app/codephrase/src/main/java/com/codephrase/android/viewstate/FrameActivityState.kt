package com.codephrase.android.viewstate

import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import com.codephrase.android.extension.readNullable
import com.codephrase.android.extension.writeNullable
import java.util.*
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
open class FrameActivityState : ViewState {
    var currentFragmentType: KClass<out Fragment>? = null
    var navigationStack = Stack<KClass<out Fragment>>()

    constructor() : super() {

    }

    constructor(parcel: Parcel) : super(parcel) {
        currentFragmentType = parcel.readNullable { (parcel.readSerializable() as Class<out Fragment>).kotlin }

        val stack = parcel.readSerializable() as Stack<Class<out Fragment>>
        stack.forEach { navigationStack.push(it.kotlin) }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)

        parcel.writeNullable(currentFragmentType) { parcel.writeSerializable(it.java) }

        val stack = Stack<Class<out Fragment>>()
        navigationStack.forEach { stack.push(it.java) }
        parcel.writeSerializable(stack)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FrameActivityState> {
        override fun createFromParcel(parcel: Parcel): FrameActivityState {
            return FrameActivityState(parcel)
        }

        override fun newArray(size: Int): Array<FrameActivityState?> {
            return arrayOfNulls(size)
        }
    }
}