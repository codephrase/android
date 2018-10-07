package com.codephrase.android.error

class NotFoundException(key: String) : Exception("$key doesn't exist.") {

}