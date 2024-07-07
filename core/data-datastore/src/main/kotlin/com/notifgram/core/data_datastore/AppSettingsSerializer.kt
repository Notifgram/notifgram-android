package com.notifgram.core.data_datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class AppSettingsSerializer @Inject constructor() : Serializer<AppSettings> {
    override val defaultValue: AppSettings = AppSettings.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): AppSettings =
        try {
            AppSettings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }


    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        t.writeTo(output)
    }
}
