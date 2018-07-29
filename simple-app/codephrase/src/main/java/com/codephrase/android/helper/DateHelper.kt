package com.codephrase.android.helper

import java.util.*

class DateHelper private constructor() {
    companion object {
        fun dateDiff(date1: Date, date2: Date): Long {
            return (date2.time - date1.time) / 1000
        }
    }
}