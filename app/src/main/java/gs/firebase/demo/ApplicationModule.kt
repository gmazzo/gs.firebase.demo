package gs.firebase.demo

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import gs.firebase.demo.views.ViewsModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [
    ApplicationModule.Bindings::class,
    ViewsModule::class
])
class ApplicationModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig() = FirebaseRemoteConfig.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(context: Context) = FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    fun provideOkHttpClient() = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()!!

    @Provides
    fun provideRetrofit(client: OkHttpClient) = Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())!!

    @Module
    interface Bindings {

        @Binds
        fun bindContext(impl: Application): Context

    }

}


