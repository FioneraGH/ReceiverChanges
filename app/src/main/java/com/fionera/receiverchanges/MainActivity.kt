package com.fionera.receiverchanges

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fionera.receiverchanges.nougat.receiver.RegisterConnectivityReceiver
import com.fionera.receiverchanges.nougat.service.ConnectivityJobService
import com.fionera.receiverchanges.nougat.svelte.ImplicitAction
import kotlinx.android.synthetic.main.activity_main.*

/**
 * MainActivity
 * Created by fionera on 2019-05-17 in ReceiverChanges.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        const val CONNECTIVITY_JOB_ID = 12768
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterCallback()
    }

    private val registerConnectivityReceiver = RegisterConnectivityReceiver()
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

        scheduleJob()

        registerCallback()
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
}