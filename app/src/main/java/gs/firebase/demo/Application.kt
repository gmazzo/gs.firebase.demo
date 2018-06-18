package gs.firebase.demo

import android.content.Context
import android.support.multidex.MultiDex
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject
import javax.inject.Singleton

class Application : DaggerApplication() {

    @Inject
    lateinit var database: FirebaseDatabase

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        remoteConfig.apply {
            setConfigSettings(FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build())

            setDefaults(mapOf(
                    BuildConfig.TOGGLE_NUDGE_ENABLED to false,
                    BuildConfig.TOGGLE_PHOTO_ENABLED to true))

            fetch().addOnCompleteListener { activateFetched() }
        }

        database.setPersistenceEnabled(true)
    }

    override fun applicationInjector() = DaggerApplication_Injector.builder()
            .create(this)

    @Singleton
    @Component(modules = [AndroidInjectionModule::class, ApplicationModule::class])
    interface Injector : AndroidInjector<Application> {

        @Component.Builder
        abstract class Builder : AndroidInjector.Builder<Application>()

    }

}