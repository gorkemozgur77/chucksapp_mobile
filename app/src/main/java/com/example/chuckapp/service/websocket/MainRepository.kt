package com.example.chuckapp.service.websocket

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel

class MainRepository constructor(private val webServicesProvider: WebServicesProvider) {

    @ExperimentalCoroutinesApi
    fun startSocket(): Channel<SocketUpdate> =
        webServicesProvider.startSocket()

    @ExperimentalCoroutinesApi
    fun closeSocket() {
        webServicesProvider.stopSocket()
    }
}