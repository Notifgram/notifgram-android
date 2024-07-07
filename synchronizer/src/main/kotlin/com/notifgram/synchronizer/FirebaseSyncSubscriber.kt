package com.notifgram.synchronizer

import com.notifgram.core.common.MyLog
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "FirebaseSyncSubscriber"
const val SYNC_TOPIC = "sync"

class FirebaseSyncSubscriber @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
) : SyncSubscriber {
    override suspend fun subscribe() {
        MyLog.i("$TAG subscribe to SYNC_TOPIC=$SYNC_TOPIC")

        firebaseMessaging
            .subscribeToTopic(SYNC_TOPIC)
            .await()
    }
}
