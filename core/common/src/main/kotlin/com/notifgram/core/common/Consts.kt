package com.notifgram.core.common

//    private const val BASE_URL = BuildConfig.BACKEND_URL    // TODO: use this
//    private const val BASE_URL = "https://net7.azurewebsites.net/"

// For emulator
private const val BASE_URL = "http://10.0.2.2:57679/"

// For real device
//private const val BASE_URL = "http://192.168.174.34:57679/"

const val BACKEND_BASE_URL = BASE_URL
const val BACKEND_API_URL = BASE_URL + "api"
const val BACKEND_FILES_URL = BACKEND_API_URL + "/Files"