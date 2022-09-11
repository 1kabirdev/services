package com.example.services

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.example.services.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    /**
     * Переменная для хранения экземпляра нашего класса обслуживания
     */
    var mService: MyServices? = null

    /**
     * Логическое значение, чтобы проверить, связана ли наша активность с обслуживанием или нет.
     */
    var mIsBound: Boolean? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        binding.starts.setOnClickListener {
            startService(Intent(this, MyServices::class.java))
            bindService()
        }

        binding.stops.setOnClickListener {
            stopService(Intent(this, MyServices::class.java))
            if (mIsBound == true) {
                unbindService()
                mIsBound = false
            }
        }

        binding.activityNext.setOnClickListener {
            startActivity(Intent(this, ResultActivity::class.java))
        }
    }

    /**
     * Интерфейс для получения экземпляра связующего из нашего класса обслуживания
     * Таким образом, клиент может получить экземпляр нашего класса обслуживания и напрямую общаться с ним.
     */
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, iBinder: IBinder) {
            /**
             * Мы привязались к MyService, привели IBinder и получили экземпляр MyBinder.
             */
            val binder = iBinder as MyServices.MyBinder
            mService = binder.service
            mIsBound = true
            /**
             * Вернуть случайное число из сервиса
             */
            getRandomNumberFromService()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mIsBound = false
        }
    }

    /**
     * Метод прослушивания случайных чисел, сгенерированных нашим сервисным классом
     */
    @SuppressLint("SetTextI18n")
    private fun getRandomNumberFromService() {
        mService?.randomNumberLiveData?.observe(this) {
            binding.logs.text = "Random number from service: $it"
        }
    }

    /**
     * Используется для привязки к нашему классу обслуживания
     */
    private fun bindService() {
        Intent(this, MyServices::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    /**
     * Используется для отключения и остановки нашего класса обслуживания
     */
    private fun unbindService() {
        Intent(this, MyServices::class.java).also { intent ->
            unbindService(serviceConnection)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        /**
         * Отвязка от класса обслуживания
         */
        unbindService()
    }
}