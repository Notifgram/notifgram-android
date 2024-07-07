package com.notifgram.fcm

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_remote.networking.channel.ChannelDto
import com.notifgram.core.data_remote.networking.channel.toChannel
import com.notifgram.core.data_remote.networking.post.PostDto
import com.notifgram.core.data_remote.networking.post.toPost
import com.notifgram.core.data_repository.FileUriBuilder
import com.notifgram.core.data_repository.repository.ChannelRepositoryImpl
import com.notifgram.core.data_repository.repository.PostRepositoryImpl
import com.notifgram.core.data_repository.sync.SyncManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

enum class FcmTopics {
    RESTFUL_SYNC_REQUEST_TOPIC, // Requests client to start syncing by restful Api.
    FCM_SYNC_TOPIC              // For messages containing data
}

enum class FcmKeys {
    TEXT_CONTENT,
    FILE_NAME,
    FILE_CHUNK_INDEX,
    FILE_TOTAL_CHUNKS,
    FILE_CHUNK_DATA,
    JSON_POST,
    JSON_CHANNEL,
    JSON_ALL_CHANNEL,
    DELETED_POST,
    DELETED_CHANNEL,

    JSON_OBJECT_TYPE,
    NOTIFICATION_TITLE,
    NOTIFICATION_BODY
}

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@AndroidEntryPoint
class AppFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var syncManager: SyncManager

    @Inject
    lateinit var postRepositoryImpl: PostRepositoryImpl

    @Inject
    lateinit var channelRepositoryImpl: ChannelRepositoryImpl

    @Inject
    lateinit var fileUriBuilder: FileUriBuilder

    @Inject
    lateinit var fileChunkHandler: FileChunkHandler

    var json = Json { ignoreUnknownKeys = true }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        i("$TAG onMessageReceived")

        i("$TAG onMessageReceived remoteMessage.from=${remoteMessage.from}")
        if (remoteMessage.from == "/topics/${FcmTopics.RESTFUL_SYNC_REQUEST_TOPIC}") {
            i("$TAG onMessageReceived remoteMessage.from== /topics/${FcmTopics.RESTFUL_SYNC_REQUEST_TOPIC}")
            syncManager.sync()
            return
        }

        remoteMessage.messageType?.let {
            i("$TAG onMessageReceived messageType: $it")
        }
        remoteMessage.notification?.let {
            i("$TAG onMessageReceived notification: ${it.title} ${it.body}")
        }

        if (remoteMessage.notification != null) {
            i(
                "$TAG onMessageReceived notification title: ${remoteMessage.notification?.title}" +
                        "notification body:${remoteMessage.notification?.body}"
            )
            //handleNotification(remoteMessage.notification)
        } else {
            i("$TAG onMessageReceived else")
            val data: Map<String, String> = remoteMessage.data
            logDataPartOfFcmMessage(data)

            if (data.containsKey(FcmKeys.FILE_NAME.toString())) {
                i("$TAG data.containsKey(FcmKeys.FILE_NAME.toString())")
                fileChunkHandler.handleFileChunk(data)
            } else if (data.containsKey(FcmKeys.TEXT_CONTENT.toString())) {
//                state = state.apply { textMessageContent = data[FcmKeys.TEXT_CONTENT.toString()]!! }
//                handleTextContent(data)
            } else if (data.containsKey(FcmKeys.JSON_POST.toString())) {
                i("$TAG data.containsKey(FcmKeys.JSON_POST.toString())")
                handlePostObject(data)
            } else if (data.containsKey(FcmKeys.JSON_CHANNEL.toString())) {
                i("$TAG data.containsKey(FcmKeys.JSON_CHANNEL.toString())")
                handleChannelObject(data)
            } else if (data.containsKey(FcmKeys.DELETED_POST.toString())) {
                i("$TAG data.containsKey(FcmKeys.DELETED_POST.toString())")
                handleDeletedPostMessage(data)
            } else if (data.containsKey(FcmKeys.DELETED_CHANNEL.toString())) {
                i("$TAG data.containsKey(FcmKeys.DELETED_CHANNEL.toString())")
                handleDeletedChannelMessage(data)
            } else if (data.containsKey(FcmKeys.JSON_ALL_CHANNEL.toString())) {
                i("$TAG data.containsKey(FcmKeys.JSON_ALL_CHANNEL.toString())")
                handleAllChannelsObject(data)
            }
//            else if (data.containsKey(FcmKeys.NOTIFICATION_TITLE.toString()) && data.containsKey(
//                    FcmKeys.NOTIFICATION_BODY.toString()
//                )
//            ) {
//                i("$TAG  type is notification")
////             handleNotification(data)
//            }
        }

    }

    private fun logDataPartOfFcmMessage(data: Map<String, String>) {
        var logText by mutableStateOf("")
        data.onEach { logText = logText.plus(" ${it.key}=${it.value}") }
        i("$TAG logDataPartOfFcmMessage Data: $logText")
    }

    private fun handleDeletedPostMessage(data: Map<String, String>) {
        val idString = data[FcmKeys.DELETED_POST.toString()]!!
        val id = idString.toInt()
        i("$TAG  handleDeletedPostMessage() id=$id")

        CoroutineScope(Dispatchers.IO).launch {
            postRepositoryImpl.deletePostLocally(id)
        }
    }

    private fun handleDeletedChannelMessage(data: Map<String, String>) {
        val idString = data[FcmKeys.DELETED_CHANNEL.toString()]!!
        val id = idString.toInt()
        i("$TAG  handleDeletedChannelMessage() id=$id")

        CoroutineScope(Dispatchers.IO).launch {
            channelRepositoryImpl.deleteChannelLocally(id)
        }
    }

    private fun handlePostObject(data: Map<String, String>) {
        val postJsonObject = data[FcmKeys.JSON_POST.toString()]!!
        val postDto = convertJsonObjectToPostDto(postJsonObject)
        i("$TAG postDto=$postDto")

        CoroutineScope(Dispatchers.IO).launch {
            postRepositoryImpl.upsertPostLocally(postDto.toPost(fileUriBuilder))
        }
    }

    //todo: change to convertJsonObjectToPostDto<>
    fun convertJsonObjectToPostDto(postJsonObject: String): PostDto {
        return json.decodeFromString<PostDto>(postJsonObject)
    }

//    inline fun <reified PostDto> convertJsonObjectToPostDto(postJsonObject: String): PostDto {
//        return json.decodeFromString<PostDto>(postJsonObject)
//    }

    private fun handleChannelObject(data: Map<String, String>) {
        val channelJsonObject = data[FcmKeys.JSON_CHANNEL.toString()]!!
        val channelDto = convertJsonObjectToChannelDto(channelJsonObject)
        i("$TAG channelDto=$channelDto")

        CoroutineScope(Dispatchers.IO).launch {
            channelRepositoryImpl.upsertChannelLocally(channelDto.toChannel())
        }
    }

    fun convertJsonObjectToChannelDto(channelJsonObject: String): ChannelDto {
        return json.decodeFromString<ChannelDto>(channelJsonObject)
    }

    private fun handleAllChannelsObject(data: Map<String, String>) {
        val channelsJsonObject = data[FcmKeys.JSON_ALL_CHANNEL.toString()]!!
        val allChannelDto = convertJsonObjectToChannelDtoList(channelsJsonObject)
        i("$TAG allChannelDto=$allChannelDto")

        CoroutineScope(Dispatchers.IO).launch {
            allChannelDto.map {
                channelRepositoryImpl.upsertChannelLocally(
                    it.toChannel()
                )
            }
        }
    }

    private fun convertJsonObjectToChannelDtoList(channelJsonObject: String): List<ChannelDto> {
        return json.decodeFromString<List<ChannelDto>>(channelJsonObject)
    }

    companion object {
        private const val TAG = "AppFirebaseMessagingService"
    }
}

