package com.fionera.receiverchanges

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fionera.R
import com.fionera.background.BackgroundActivity
import com.fionera.receiverchanges.nougat.receiver.RegisterConnectivityReceiver
import com.fionera.receiverchanges.nougat.service.ConnectivityJobService
import com.fionera.receiverchanges.nougat.svelte.ImplicitAction
import com.fionera.receiverchanges.oreo.action.Actions
import com.fionera.receiverchanges.oreo.receiver.ManifestStaticReceiver
import com.fionera.receiverchanges.oreo.receiver.RegisterDynamicReceiver
import com.fionera.receiverchanges.util.showToast
import kotlinx.android.synthetic.main.activity_receiver.*

/**
 * ReceiverActivity
 * Created by fionera on 2019-05-17 in ReceiverChanges.
 */
class ReceiverActivity : AppCompatActivity() {

    companion object {
        const val CONNECTIVITY_JOB_ID = 12768
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)

        initViews()

        scheduleJob()

        registerCallback()

        registerBroadcastReceiver()
//        ll_title_container.getChildAt(0).setBackgroundColor(Color.CYAN)
//        ll_title_container.setOnClickListener { showToast("Test") }
//        tv_title.text = "Home"
//        print(tv_title ?: "Cannot be resolved default")
        (include_title as LinearLayout).getChildAt(0).setBackgroundColor(Color.CYAN)
        include_title.setOnClickListener { showToast("Test") }
        include_title.findViewById<TextView>(R.id.tv_title).text = "Home"
//        print(tv_title ?: "Cannot be resolved default")
//        tv_title.setOnClickListener { showToast("Test") }
//        print("View is: ${tv_title ?: "Cannot be resolved default"}")

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        startActivity(Intent(this, BackgroundActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBroadcastReceiver()
        unregisterCallback()
    }

    private val registerConnectivityReceiver = RegisterConnectivityReceiver()
    private val registerDynamicReceiver = RegisterDynamicReceiver()
    private val connCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            println("Connectivity Callback Available: ${network?.toString()}")
        }

        override fun onLost(network: Network?) {
            println("Connectivity Callback Lost: ${network?.toString()}")
        }

        override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
            println("Connectivity Callback Changed: ${network?.toString()} / ${networkCapabilities?.toString()}")
        }
    }

    private fun initViews() {
        btn_conn_register.setOnClickListener {
            val intentFilter = IntentFilter()
            intentFilter.addAction(ImplicitAction.CONNECTIVITY_ACTION)
            registerReceiver(registerConnectivityReceiver, intentFilter)
        }

        btn_conn_unregister.setOnClickListener {
            unregisterReceiver(registerConnectivityReceiver)
        }

        btn_static_send.setOnClickListener {
            val intent = Intent()
            intent.action = Actions.STATIC
            sendBroadcast(intent)
        }

        btn_static_send_with_pkg.setOnClickListener {
            val intent = Intent()
            intent.action = Actions.STATIC
            intent.setPackage(packageName)
            sendBroadcast(intent)
        }

        btn_static_send_explicit.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@ReceiverActivity, ManifestStaticReceiver::class.java)
            sendBroadcast(intent)
        }

        btn_dynamic_send.setOnClickListener {
            val intent = Intent()
            intent.action = Actions.DYNAMIC
            sendBroadcast(intent)
        }

        btn_dynamic_send_with_pkg.setOnClickListener {
            val intent = Intent()
            intent.action = Actions.DYNAMIC
            intent.setPackage(packageName)
            sendBroadcast(intent)
        }

        btn_dynamic_send_explicit.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@ReceiverActivity, RegisterDynamicReceiver::class.java)
            sendBroadcast(intent)
        }
    }

    private fun scheduleJob() {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val job = JobInfo.Builder(CONNECTIVITY_JOB_ID, ComponentName(this, ConnectivityJobService::class.java))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()
        jobScheduler.schedule(job)
    }

    private fun registerCallback() {
        val connManger = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connManger.registerNetworkCallback(
                NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build(),
                connCallback)
    }

    private fun unregisterCallback() {
        val connManger = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connManger.unregisterNetworkCallback(connCallback)
    }

    private fun registerBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Actions.DYNAMIC)
        registerReceiver(registerDynamicReceiver, intentFilter)
    }

    private fun unregisterBroadcastReceiver() {
        unregisterReceiver(registerDynamicReceiver)
    }
}