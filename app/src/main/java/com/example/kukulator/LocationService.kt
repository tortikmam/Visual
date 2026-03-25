package com.example.kukulator

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import org.zeromq.ZContext
import org.zeromq.ZMQ

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val serverAddress = "tcp://192.168.43.244:8888"
    private val CHANNEL_ID = "GpsServiceChannel"

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "GPS Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("GPS Мониторинг")
            .setContentText("Отправка данных на сервер...")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build()

        startForeground(1, notification)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    sendLocationToServer(location.latitude, location.longitude)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationUpdates()
        return START_STICKY
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
            .setMinUpdateIntervalMillis(1000)
            .setMaxUpdateDelayMillis(0)
            .setWaitForAccurateLocation(false)
            .build()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun sendLocationToServer(latitude: Double, longitude: Double) {
        Thread {
            try {
                val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val strNet = StringBuilder()

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    val cellInfoList = telephonyManager.allCellInfo
                    if (cellInfoList != null) {
                        val lteCells = cellInfoList.filterIsInstance<android.telephony.CellInfoLte>()
                        for (cellInfo in lteCells) {
                            strNet.append("CellInfoLte:")
                            strNet.append(" Registered: ${cellInfo.isRegistered}")
                            val identity = cellInfo.cellIdentity
                            val signal = cellInfo.cellSignalStrength

                            strNet.append(" CellIdentityLte:")
                            strNet.append(" PCI: ${identity.pci}")
                            strNet.append(" TAC: ${identity.tac}")
                            strNet.append(" CI: ${identity.ci}")
                            strNet.append(" EARFCN: ${identity.earfcn}")
                            strNet.append(" MCC: ${identity.mcc}")
                            strNet.append(" MNC: ${identity.mnc}\n")
                            strNet.append(" DBM: ${signal.dbm}\n")
                            strNet.append(" CellSignalStrengthLte:")
                            strNet.append(" Timing Advance: ${signal.timingAdvance}")
                            strNet.append(" ASU Level: ${signal.asuLevel}")

                            try {
                                val method = signal.javaClass.getMethod("getRsrq")
                                val rsrqValue = method.invoke(signal) as Int
                                strNet.append(" RSRQ: $rsrqValue dB\n")
                                val rssi = signal.javaClass.getMethod("getRssi")
                                val rssiValue = rssi.invoke(signal) as Int
                                strNet.append(" RSSI: $rssiValue dBm\n")
                            } catch (e: Exception) {
                                strNet.append(" RSRQ/RSSI: N/A\n")
                            }
                            strNet.append("\n")
                        }
                    }
                }

                ZContext().use { context ->
                    val socket = context.createSocket(ZMQ.REQ)
                    socket.connect(serverAddress)
                    socket.receiveTimeOut = 3000

                    val message = "LAT: $latitude, LON: $longitude\n$strNet"
                    socket.send(message.toByteArray(ZMQ.CHARSET), 0)

                    val reply = socket.recv(0)
                    if (reply != null) {
                        val serverText = String(reply, ZMQ.CHARSET)

                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this, "Сервер: $serverText", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("GpsService", "Сервис остановлен")
    }
}