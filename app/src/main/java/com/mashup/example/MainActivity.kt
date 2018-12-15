package com.mashup.example

import android.Manifest
import android.bluetooth.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import java.io.InputStreamReader
import java.lang.Exception
import java.util.*
import java.io.BufferedReader


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION
        ), 2000)



        val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        when {
            adapter == null -> {
                // not supported
            }
            adapter.isEnabled -> {
                // bluetooth turned on
                val socket = adapter.listenUsingRfcommWithServiceRecord("ble-example", UUID.fromString("040cb2a6-d0f4-4854-953f-8cde192b23c0"))
                AcceptThread(socket).run()
            }
            else -> {
                // bluetooth turned off
            }
        }
    }

    inner class AcceptThread(private val serverSocket: BluetoothServerSocket) : Thread() {
        override fun run() {
            super.run()

            lateinit var socket: BluetoothSocket
            while (true) {
                try {
                    Log.v("TAG", "socket before accepted")
                    socket = serverSocket.accept()
                    Log.v("TAG", "socket accepted")
                    break
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.v("TAG", "socket accept failed")
                    continue
                }
            }
            while (true) {
                Log.v("TAG", "before inputstream!?")
                val response = BufferedReader(InputStreamReader(socket.inputStream))
                Log.v("TAG", "receive inputstream!?")

                val total = StringBuilder()
                while (true) {
                    Log.v("TAG", "before read")
                    var line: String? = response.readLine()
                    Log.v("TAG", "line: $line")
                    if (line == null){
                        break
                    }
                    total.append(line).append("\n")
                }
                Log.v("TAG", total.toString())
            }
        }
    }


}
