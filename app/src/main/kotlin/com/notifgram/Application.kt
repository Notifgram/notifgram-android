package com.notifgram

import android.app.Application
import com.notifgram.synchronizer.startSync
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
internal class Application : Application() {

//    @Inject
//    lateinit var workerFactory: HiltWorkerFactory
//
//    override fun getWorkManagerConfiguration() =
//        Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .build()

    override fun onCreate() {
        super.onCreate()

        startSync(this)
    }


}