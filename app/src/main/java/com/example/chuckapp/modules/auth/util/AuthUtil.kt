package com.example.chuckapp.modules.auth.util

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter

class AuthUtil {

    fun getDeviceName() : String {
        return BluetoothAdapter.getDefaultAdapter().name
    }

    fun getIpAddress(context: Context) : String {    //Ip address getters
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
    }

    @SuppressLint("HardwareIds")
    fun getMacAddress(context: Context) : String {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.connectionInfo.macAddress
    }

}