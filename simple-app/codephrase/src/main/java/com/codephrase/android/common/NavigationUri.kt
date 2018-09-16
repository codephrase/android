package com.codephrase.android.common

import android.net.Uri

class NavigationUri {
    val scheme: String
    val host: String
    val pathSegments: List<String>
    val queryParameters: HashMap<String, String>

    val path: String
        get() = pathSegments.joinToString("/")

    val query: String
        get() {
            val arr = ArrayList<String>()
            queryParameters.forEach { (key, value) -> arr.add("$key=$value") }
            return arr.joinToString("&")
        }

    constructor(uri: Uri) {
        scheme = uri.scheme
        host = uri.host
        pathSegments = ArrayList(uri.pathSegments)
        queryParameters = LinkedHashMap()
        uri.queryParameterNames.forEach { queryParameterName -> queryParameters[queryParameterName] = uri.getQueryParameter(queryParameterName) }
    }

    override fun toString(): String {
        val uri = StringBuilder()
        uri.append(scheme)
        uri.append("://")
        uri.append(host)

        path.takeIf { it.isNotEmpty() }
                ?.apply {
                    uri.append("/")
                    uri.append(this)
                }

        query.takeIf { it.isNotEmpty() }
                ?.apply {
                    uri.append("?")
                    uri.append(this)
                }

        return uri.toString()
    }
}