package com.wotin.geniustest

import android.net.ConnectivityManager
import java.security.KeyStore

fun networkState(connectivityManager : ConnectivityManager): Boolean {
    return (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected)
}