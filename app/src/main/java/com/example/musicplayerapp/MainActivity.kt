package com.example.musicplayerapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.style.UpdateAppearance
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // variables
    var startTime = 0.0
    var finalTime = 0.0
    var forwardTime = 10000
    var backwardTime = 10000
    var oneTimeOnly = 0

    // Handler
    var handler: Handler = Handler()

    // Media Player
    var mediaPlayer = MediaPlayer()
    lateinit var time_txt: TextView
    lateinit var progress_bar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val song_tile : TextView = findViewById(R.id.songTitle)
        time_txt = findViewById(R.id.timeLeft)
        progress_bar  = findViewById(R.id.progressBar)

        val back_btn : Button = findViewById(R.id.backBtn)
        val play_btn : Button = findViewById(R.id.playBtn)
        val pause_btn : Button = findViewById(R.id.pauseBtn)
        val fwd_btn : Button = findViewById(R.id.fwdBtn)

        // creating a media player and add song
        mediaPlayer = MediaPlayer.create(
            this,
            R.raw.astronaut)

        // setting the seekbar as unclicakable
        progress_bar.isClickable = false


        // adding func for the buttons
        play_btn.setOnClickListener(){
            mediaPlayer.start()

            // duration of whole song and startime as current position of playing song
            finalTime = mediaPlayer.duration.toDouble()
            startTime = mediaPlayer.currentPosition.toDouble()


            // checking for one time playing of song
            if (oneTimeOnly == 0){
                progress_bar.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            // making
            time_txt.text = startTime.toString()
            progress_bar.setProgress(startTime.toInt())

            // realtime updating progress and song time
            // handler is used
            handler.postDelayed(UpdateSongTime, 100)
        }

        // setting music title
        song_tile.text = resources.getResourceEntryName(R.raw.astronaut)

        // pause button
        pause_btn.setOnClickListener(){
            mediaPlayer.pause()
        }

        // fwd button
        fwd_btn.setOnClickListener(){
            var temp = startTime
            if (temp + forwardTime <= finalTime){
                startTime += forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else{
                Toast.makeText(this, "Can't Jump Forward", Toast.LENGTH_LONG).show()
            }
        }

        // back button
        back_btn.setOnClickListener(){
            var temp = startTime.toInt()
            if (temp - backwardTime > 0){
                startTime -= backwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else{
                Toast.makeText(this, "Can't Jump Backward", Toast.LENGTH_LONG).show()
            }
        }

    }

    // creating runnable
    val UpdateSongTime : Runnable = object : Runnable{
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            time_txt.text = String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()
                        - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(
                            startTime.toLong())
                        )
                ))

            progress_bar.progress = startTime.toInt()
            handler.postDelayed(this, 100)
        }
    }
}