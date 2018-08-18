package com.codephrase.android.extension

import android.os.Parcel
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

inline fun Parcel.readBoolean() =
        readInt() != 0

inline fun Parcel.writeBoolean(value: Boolean) =
        writeInt(if (value) 1 else 0)

inline fun <reified T : Enum<T>> Parcel.readEnum() =
        readInt().let { if (it >= 0) enumValues<T>()[it] else null }

inline fun <T : Enum<T>> Parcel.writeEnum(value: T?) =
        writeInt(value?.ordinal ?: -1)

inline fun Parcel.readDate() =
        Date(readLong())

inline fun Parcel.writeDate(value: Date) =
        writeLong(value.time)

inline fun Parcel.readBigInteger() =
        BigInteger(createByteArray())

inline fun Parcel.writeBigInteger(value: BigInteger) =
        writeByteArray(value.toByteArray())

inline fun Parcel.readBigDecimal() =
        BigDecimal(BigInteger(createByteArray()), readInt())

inline fun Parcel.writeBigDecimal(value: BigDecimal) {
    writeByteArray(value.unscaledValue().toByteArray())
    writeInt(value.scale())
}

inline fun <T> Parcel.readNullable(action: () -> T) =
        if (readInt() != 0) action() else null

inline fun <T> Parcel.writeNullable(value: T?, action: (T) -> Unit) {
    if (value != null) {
        writeInt(1)
        action(value)
    } else {
        writeInt(0)
    }
}