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
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer

class ResultActivity : AppCompatActivity() {

    val mTAG = ResultActivity::class.java.simpleName
    var mService: MyServices? = null
    var mIsBound: Boolean? = null

    private lateinit var logs: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        logs = findViewById(R.id.logs)
    }

    override fun onStart() {
        super.onStart()
        bindService()
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, iBinder: IBinder) {
            Log.d(mTAG, "ServiceConnection: connected to service.")
            val binder = iBinder as MyServices.MyBinder
            mService = binder.service
            mIsBound = true
            getRandomNumberFromService()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.d(mTAG, "ServiceConnection: disconnected from service.")
            mIsBound = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getRandomNumberFromService() {
        mService?.randomNumberLiveData?.observe(this, Observer {
            logs.text = "Random number from service: $it"
        })
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