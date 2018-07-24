package gs.firebase.demo.views.login

import com.facebook.CallbackManager
import dagger.Module
import dagger.Provides

@Module
class FacebookAuthModule {

    @Provides
    fun provideCallbackManager() = CallbackManager.Factory.create()!!

}