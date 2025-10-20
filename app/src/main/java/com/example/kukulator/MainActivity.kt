package com.example.kukulator

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    override fun onResume() {
        super.onResume()

        var bKukulator: Button = findViewById(R.id.button_kukulator)
        bKukulator.setOnClickListener({
            val randomIntent = Intent(this, KukulatorActivity::class.java)
            startActivity(randomIntent)
        })

        var bMediaPlayer: Button = findViewById(R.id.button_media_player)
        bMediaPlayer.setOnClickListener({
            val MedAct = Intent(this, MediaPlayerActivity::class.java)
            startActivity(MedAct)
        })

    }
}