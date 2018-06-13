package gs.firebase.demo

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class Application : android.app.Application() {
    internal val fcmHelper by lazy { FirebaseMessagingHelper(this) }

    override fun onCreate() {
        super.onCreate()

        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettings(FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build())
            setDefaults(mapOf(BuildConfig.TOGGLE_NUDGE_ENABLED to false))
            fetch().addOnCompleteListener { activateFetched() }
        }

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

}