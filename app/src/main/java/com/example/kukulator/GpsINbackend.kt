package com.example.kukulator

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.*
import org.zeromq.ZContext
import org.zeromq.ZMQ

class GpsINbackend : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val serverAddress = "tcp://192.168.43.244:8888"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gps_inbackend)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val lat = location.latitude
                    val lon = location.longitude

                    Toast.makeText(
                        this@GpsINbackend,
                        "GPS: $lat, $lon",
                        Toast.LENGTH_SHORT
                    ).show()

                    sendLocationToServer(lat, lon)
                }
            }
        }

        checkPermissions()
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val needsRequest = permissions.any {
            ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (needsRequest) {
            ActivityCompat.requestPermissions(this, permissions, 100)
        } else {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        // Настройка частоты опроса GPS (раз в 5 секунд)
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(2000)
            .build()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            Toast.makeText(this, "Поиск спутников запущен...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendLocationToServer(latitude: Double, longitude: Double) {
        Thread {
            try {
                ZContext().use { context ->
                    val socket = context.createSocket(ZMQ.REQ)
                    socket.connect(serverAddress)
                    socket.receiveTimeOut = 3000

                    val message = "LAT: $latitude, LON: $longitude"
                    socket.send(message.toByteArray(ZMQ.CHARSET), 0)

                    val reply = socket.recv(0)
                    if (reply != null) {
                        val serverText = String(reply, ZMQ.CHARSET)
                        runOnUiThread {
                            Toast.makeText(this, "Сервер: $serverText", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

}