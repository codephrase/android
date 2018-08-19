package com.codephrase.android.helper

import java.util.regex.Pattern

class TextHelper private constructor() {
    companion object {
        fun isValidEmail(email: String): Boolean {
            return Pattern.compile(
                    "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email).matches()
        }

        fun isValidPhoneNumber(phoneNumber: String): Boolean {
            return Pattern.compile(
                    "^\\+(?:[0-9] ?){6,14}[0-9]$"
            ).matcher(phoneNumber).matches()
        }

        fun isValidIpv4Address(ipv4Address: String): Boolean {
            return Pattern.compile(
                    "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
            ).matcher(ipv4Address).matches()
        }

        fun isValidIpv6Address(ipv6Address: String): Boolean {
            return Pattern.compile(
                    "^[0-9a-f]{1,4}:" +
                            "[0-9a-f]{1,4}:" +
                            "[0-9a-f]{1,4}:" +
                            "[0-9a-f]{1,4}:" +
                            "[0-9a-f]{1,4}:" +
                            "[0-9a-f]{1,4}:" +
                            "[0-9a-f]{1,4}:" +
                            "[0-9a-f]{1,4}$"
            ).matcher(ipv6Address).matches()
        }
    }
}