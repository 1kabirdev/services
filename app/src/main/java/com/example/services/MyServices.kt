package com.example.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import java.util.*

class MyServices : Service() {

    private val mBinder: IBinder = MyBinder()
    private val mGenerator: Random = Random()
    val randomNumberLiveData: MutableLiveData<Int> = MutableLiveData()
    val CHANNEL_ID = "Random number notification"
    var ambientMediaPlayer: MediaPlayer? = null

    inner class MyBinder : Binder() {
        val service: MyServices
            get() = this@MyServices
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d("onBind", "SUCCESS")
        return mBinder
    }

    /**
     * Инициализируется медиа-проигрыватель с помощью музыкального ресурса,
     * который добавлен в папку res/raw.
     */
    override fun onCreate() {
        ambientMediaPlayer = MediaPlayer.create(this, R.raw.music)
        ambientMediaPlayer!!.isLooping = true

        Log.d("MyBoundService", "onCreate called")
        startNotification()

        Handler().postDelayed({
            val randomNumber = mGenerator.nextInt(100)
            randomNumberLiveData.postValue(randomNumber)
        }, 1000)
    }

    /**
     * Начинается воспроизведение.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ambientMediaPlayer!!.start()
        return START_STICKY
    }

    /**
     * Завершает воспроизведение.
     */
    override fun onDestroy() {
        ambientMediaPlayer!!.stop()
    }

    private fun startNotification() {
        val channel =
            NotificationChannel(
                CHANNEL_ID,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            channel
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("A service is running in the background")
            .setContentText("Generating random number").build()
        startForeground(1, notification)
    }
}