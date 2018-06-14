package gs.firebase.demo

import android.support.multidex.MultiDexApplication
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class Application : MultiDexApplication() {

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