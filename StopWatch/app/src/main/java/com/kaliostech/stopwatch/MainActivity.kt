package com.kaliostech.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {

    private lateinit var timer: TextView
    private lateinit var btn_start: Button
    private lateinit var btn_stop: Button
    private lateinit var btn_hold: Button
    private lateinit var animationView: LottieAnimationView

    private var handler = Handler(Looper.getMainLooper())
    private var startTime = 0L
    private var timeInSeconds = 0L
    private var isRunning = false
    private var isPaused = false
    private var currentFrame = 0

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                val currentTime = System.currentTimeMillis()
                timeInSeconds = (currentTime - startTime) / 1000
                val hours = timeInSeconds / 3600
                val minutes = (timeInSeconds % 3600) / 60
                val seconds = timeInSeconds % 60
                timer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        timer = findViewById(R.id.timer_view)
        btn_hold = findViewById(R.id.btn_hold)
        btn_start = findViewById(R.id.btn_start)
        btn_stop = findViewById(R.id.btn_stop)
        animationView = findViewById(R.id.animationView)

        btn_start.setOnClickListener {
            startTimer()
        }

        btn_hold.setOnClickListener {
            pauseTimer()
        }

        btn_stop.setOnClickListener {
            stopTimer()
        }
    }

    private fun startTimer() {
        if (!isRunning) {
            isRunning = true
            startTime = System.currentTimeMillis() - timeInSeconds * 1000
            handler.post(updateTimerRunnable)
            startOrResumeAnimation()
        }
    }

    private fun pauseTimer() {
        if (isRunning) {
            isRunning = false
            handler.removeCallbacks(updateTimerRunnable)
            pauseAnimation()
        }
    }

    private fun stopTimer() {
        isRunning = false
        handler.removeCallbacks(updateTimerRunnable)
        timeInSeconds = 0
        timer.text = "00:00:00"
        stopAnimation()
    }
    private fun startOrResumeAnimation() {
        if (isPaused) {
            animationView.resumeAnimation()
            isPaused = false
        } else {
            animationView.playAnimation()
        }
    }

    private fun pauseAnimation() {
        currentFrame = animationView.frame.toInt()
        animationView.pauseAnimation()
        isPaused = true
    }

    private fun stopAnimation() {
        animationView.cancelAnimation()
        animationView.setFrame(0)
        isPaused = false
    }
}
