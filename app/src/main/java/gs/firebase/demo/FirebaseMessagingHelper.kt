package gs.firebase.demo

import android.content.Context
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

internal class FirebaseMessagingHelper(val context: Context) {
    private val firebaseToken: String by lazy {
        GoogleCredential
                .fromStream(context.resources.openRawResource(R.raw.firebase))
                .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
                .apply { refreshToken() }
                .let { "Bearer ${it.accessToken}" }
    }

    private val firebaseCloudAPI by lazy {
        retrofit("https://fcm.googleapis.com/", FirebaseCloudAPI::class.java)
    }

    fun sendMessageToTopic(topic: String, title: String, message: String) = firebaseCloudAPI
            .messagesSend(
                    authorization = firebaseToken,
                    project = context.getString(R.string.project_id),
                    request = Request(Message(topic, Notification(title, message))))
            .execute()
            .body()!!

    private interface FirebaseCloudAPI {

        @POST("v1/projects/{project}/messages:send")
        fun messagesSend(@Header("Authorization") authorization: String,
                         @Path("project") project: String,
                         @Body request: Request): Call<JsonObject>


    }

    data class Request(val message: Message)

    data class Message(val topic: String,
                       val notification: Notification)

    data class Notification(val title: String,
                            val body: String)

}