package com.notifgram.core.common

import android.util.Log

interface Logger {
    fun i(tag: String, message: String)
    fun e(tag: String, message: String)
    // ... other log methods
}

class AndroidLogger : Logger {
    override fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }
    // ... other log methods
}

object MyLog {
    private const val TAG = "mycode"
    var logger: Logger = AndroidLogger()

    @JvmStatic
    fun i(log: String?) {
        logger.i(TAG, log!!)
    }

    fun e(log: String?) {
        logger.e(TAG, log!!)
    }

    fun e(e: Exception) {
        logger.e(TAG, e.toString())
    }

    fun e(log: String?, tr: Throwable?) {
        Log.e(TAG, log, tr)
    }
}