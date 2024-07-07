package com.notifgram.core.common

class TestLogger : Logger {
    override fun i(tag: String, message: String) {
        println("INFO: $tag: $message")
    }

    override fun e(tag: String, message: String) {
        println("ERROR: $tag: $message")

    }
    // ... other log methods
}