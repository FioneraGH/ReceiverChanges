package com.fionera.receiverchanges.nougat.service

import android.app.job.JobParameters
import android.app.job.JobService
import com.fionera.receiverchanges.util.postDelay
import com.fionera.receiverchanges.util.showToast

/**
 * ConnectivityJobService
 * Created by fionera on 2019-05-17 in ReceiverChanges.
 */
class ConnectivityJobService : JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {
        val msg = "JobService Connectivity Stop: ${this.hashCode()}"
        baseContext?.showToast(msg)
        println(msg)
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        val msg = "JobService Connectivity Start: ${this.hashCode()}"
        baseContext?.showToast(msg)
        println(msg)
        postDelay({ jobFinished(params, true) }, 1000)
        return true
    }
}