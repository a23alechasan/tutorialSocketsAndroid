package com.exemple.tutorialsocketsandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class MainActivity : ComponentActivity() {
    private lateinit var socket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            socket = IO.socket("http://tutorialsockets.dam.inspedralbes.cat:29888")
            socket.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        setContent {
            ControladorApp(socket)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }
}
