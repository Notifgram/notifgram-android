package com.notifgram.core.common

import com.notifgram.core.common.MyLog.e
import com.notifgram.core.common.MyLog.i
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging

class FirebaseCloudMessaging {

    fun subscribeToTopic(topic: String) {
        i("$TAG subscribeToTopic() called. text=$topic")

        Firebase.messaging.subscribeToTopic(topic).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                e("$TAG subscribeToTopic() addOnCompleteListener failed.")
            } else
                i("$TAG subscribeToTopic() addOnCompleteListener success.")

            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun unsubscribeFromTopic(topic: String) {
        i("$TAG unsubscribeFromTopic() called. text=$topic")

        Firebase.messaging.unsubscribeFromTopic(topic).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                e("$TAG unsubscribeFromTopic() addOnCompleteListener failed.")
            } else
                i("$TAG unsubscribeFromTopic() addOnCompleteListener success.")

            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun logRegistrationToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { regTokenTask ->
            if (regTokenTask.isSuccessful)
                i("$TAG FCM registration token: ${regTokenTask.result}")
            else
                e(
                    "$TAG Unable to retrieve registration token",
                    regTokenTask.exception
                )

        }
    }

    fun getFCMToken(callback: (String?) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task: Task<String?> ->
            if (task.isSuccessful) {
                val token = task.result
                callback(token)
            } else {
                callback(null)
            }
        })
    }


    companion object {
        private const val TAG = "FirebaseCloudMessaging"
    }

}