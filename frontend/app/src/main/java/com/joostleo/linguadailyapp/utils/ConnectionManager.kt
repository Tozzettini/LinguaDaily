package com.joostleo.linguadailyapp.utils
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class ConnectionManager {
    companion object {
        fun getNetworkType(context: Context): NetworkType {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val network = connectivityManager.activeNetwork ?: return NetworkType.NONE
            val capabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return NetworkType.NONE

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.MOBILE_DATA
                else -> NetworkType.NONE
            }
        }
    }
}

enum class NetworkType {
    WIFI,
    MOBILE_DATA,
    NONE
}
