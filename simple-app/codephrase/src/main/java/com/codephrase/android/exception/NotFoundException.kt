package com.codephrase.android.exception

class NotFoundException(key: String) : Exception("$key doesn't exist.") {

}