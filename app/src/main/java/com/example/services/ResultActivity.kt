package com.example.services

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.services.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private var mService: MyServices? = null
    private var mIsBound: Boolean? = null
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        bindService()
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, iBinder: IBinder) {
            val binder = iBinder as MyServices.MyBinder
            mService = binder.service
            mIsBound = true
            getRandomNumberFromService()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mIsBound = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getRandomNumberFromService() {
        mService?.randomNumberLiveData?.observe(this) {
            binding.logs.text = "Random number from service: $it"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService()
    }

    private fun bindService() {
        Intent(this, MyServices::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindService() {
        Intent(this, MyServices::class.java).also { intent ->
            unbindService(serviceConnection)
        }
    }
}