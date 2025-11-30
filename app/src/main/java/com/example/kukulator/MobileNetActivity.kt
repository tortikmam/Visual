package com.example.kukulator

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MobileNetActivity : AppCompatActivity() {

    val TAG = "MobileNetActivity"
    private lateinit var ViewLocal: TextView
    private val PERMISSION_REQUEST_CODE = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mobile_net)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        val hasPhonePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED

        val hasLocationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (hasPhonePermission && hasLocationPermission) {

            Log.d(TAG, "Разрешения получены")
            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val cellInfoList = telephonyManager.allCellInfo
            ViewLocal = findViewById(R.id.view_net)

            if (cellInfoList != null) {
                val strNet = StringBuilder()

                val lteCells = cellInfoList.filterIsInstance<android.telephony.CellInfoLte>()
                for (cellInfo in lteCells) {
                    strNet.append("CellInfoLte:")
                    strNet.append("  Registered: ${cellInfo.isRegistered}")
                    val identity = cellInfo.cellIdentity
                    val signal = cellInfo.cellSignalStrength

                    strNet.append("  CellIdentityLte:")
                    strNet.append("    PCI: ${identity.pci}")
                    strNet.append("    TAC: ${identity.tac}")
                    strNet.append("    CI: ${identity.ci}")
                    strNet.append("    EARFCN: ${identity.earfcn}")
                    strNet.append("    MCC: ${identity.mcc}")
                    strNet.append("    MNC: ${identity.mnc}\n")

                    strNet.append("  CellSignalStrengthLte:")
                    strNet.append("    Timing Advance: ${signal.timingAdvance}")
                    strNet.append("    ASU Level: ${signal.asuLevel}")
                    strNet.append("\n")
                }

                val gsmCells = cellInfoList.filterIsInstance<android.telephony.CellInfoGsm>()
                for (cellInfo in gsmCells) {
                    strNet.append("CellInfoGsm:\n")
                    val identity = cellInfo.cellIdentity
                    val signal = cellInfo.cellSignalStrength

                    strNet.append("  CellIdentityGsm:\n")
                    strNet.append("    LAC: ${identity.lac}\n")
                    strNet.append("    CID: ${identity.cid}\n")
                    strNet.append("    ARFCN: ${identity.arfcn}\n")
                    strNet.append("    BSIC: ${identity.bsic}\n")
                    strNet.append("    PSC: ${identity.psc}\n")
                    strNet.append("    MCC: ${identity.mcc}\n")
                    strNet.append("    MNC: ${identity.mnc}\n")

                    strNet.append("  CellSignalStrengthGsm:\n")
                    strNet.append("    Dbm: ${signal.dbm} dBm\n")
                    strNet.append("\n")
                }

                ViewLocal.text = strNet.toString()

            }
        }else {
            requestPermissions()
        }

    }


    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(android.Manifest.permission.READ_PHONE_STATE)
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                val allGranted = grantResults.all { result ->
                    result == PackageManager.PERMISSION_GRANTED
                }
            }
        }
    }
}