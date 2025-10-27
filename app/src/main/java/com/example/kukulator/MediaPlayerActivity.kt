package com.example.kukulator

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class MediaPlayerActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var listTrack: TextView
    private lateinit var progressBar: SeekBar
    private lateinit var TextTime: TextView
    private val updateSeekBar = object : Runnable {
        override fun run() {
            if (mediaPlayer.isPlaying) {

                val progress = (mediaPlayer.currentPosition * 100 / mediaPlayer.duration).toInt()
                var miliseconds = mediaPlayer.currentPosition/1000
                var minutes = miliseconds / 60
                var seconds = miliseconds % 60

                var time = String.format("%02d:%02d", minutes, seconds)
                TextTime.setText(time)

                progressBar.progress = progress
            }
            progressBar.postDelayed(this, 1000)
        }
    }
    var count_pause = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_media_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource("/storage/emulated/0/Music/Browser/KINO_-_Bratskaya_lyubov_2025_79921224.mp3")
            mediaPlayer.setOnPreparedListener {
            }
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var currentTrackIndex = 0
    private fun playNext() {
            val musicPath = Environment.getExternalStorageDirectory().path + "/Music/Browser"
            val directory = File(musicPath)
            val files = directory.listFiles().filter { it.name.endsWith(".mp3") } ?: return

            if (files.isEmpty()) return

            currentTrackIndex = (currentTrackIndex + 1) % files.size

            mediaPlayer.reset()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
            }
            mediaPlayer.setDataSource(files[currentTrackIndex].absolutePath)
            mediaPlayer.prepareAsync()
    }

    private fun playPrevious() {
        val musicPath = Environment.getExternalStorageDirectory().path + "/Music/Browser"
        val directory = File(musicPath)
        val files = directory.listFiles().filter { it.name.endsWith(".mp3") } ?: return

        if (files.isEmpty()) return

        currentTrackIndex = currentTrackIndex - 1

        if (currentTrackIndex < 0) {
            currentTrackIndex = files.size - 1
        }

        mediaPlayer.reset()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
        mediaPlayer.setDataSource(files[currentTrackIndex].absolutePath)
        mediaPlayer.prepareAsync()
    }

    override fun onResume() {
        super.onResume()



        TextTime= findViewById(R.id.text_time)
        TextTime.text = ""

        listTrack = findViewById(R.id.list_track)

        val musicPath = Environment.getExternalStorageDirectory().path + "/Music/Browser"
        val directory = File(musicPath)
        val files = directory.listFiles()?.filter { it.name.endsWith(".mp3") } ?: return

        val trackText = files.joinToString("\n") { it.name }
        listTrack.text = trackText

        progressBar = findViewById(R.id.progress_bar)
        progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(progressBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val newPosition = (progress * mediaPlayer.duration) / 100
                    mediaPlayer.seekTo(newPosition)
                }
            }

            override fun onStartTrackingTouch(progressBar: SeekBar?) {}
            override fun onStopTrackingTouch(progressBar: SeekBar?) {}
        })

        var bPausePlay: Button = findViewById(R.id.button_pause_play)
        bPausePlay.setOnClickListener({
            count_pause = count_pause + 1
            if (count_pause % 2 != 0){
                mediaPlayer.start()
                bPausePlay.text = "| |"
                progressBar.post(updateSeekBar)

            }else{
                mediaPlayer.pause()
                bPausePlay.text = "|>"
                progressBar.post(updateSeekBar)
            }

        })

        var bRewind: Button = findViewById(R.id.button_rewind)
        bRewind.setOnClickListener({
            playPrevious()
        })

        var bFastForward: Button = findViewById(R.id.button_fast_forward)
        bFastForward.setOnClickListener({
            playNext()
        })

    }
}