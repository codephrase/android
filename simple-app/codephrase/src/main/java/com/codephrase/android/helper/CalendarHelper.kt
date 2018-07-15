package com.codephrase.android.helper

import java.text.SimpleDateFormat
import java.util.*

class CalendarHelper private constructor() {

    companion object {
        fun parse(str: String, format: String): Calendar? {
            try {
                val dateFormat = SimpleDateFormat(format)

                val calendar = Calendar.getInstance()
                calendar.time = dateFormat.parse(str);
                return calendar
            } catch (e: Exception) {
                return null
            }
        }

        fun format(calendar: Calendar, format: String): String {
            val dateFormat = SimpleDateFormat(format)
            return dateFormat.format(calendar.time)
        }
    }
}