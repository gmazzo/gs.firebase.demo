package gs.firebase.demo.views.login

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface LoginModule {

    @ContributesAndroidInjector
    fun provideLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    fun provideSocialNetworksLoginFragment(): SocialNetworksLoginFragment

    @ContributesAndroidInjector
    fun provideGoogleLoginFragment(): GoogleLoginFragment

    @ContributesAndroidInjector(modules = [FacebookAuthModule::class])
    fun provideFacebookLoginFragment(): FacebookLoginFragment

    @ContributesAndroidInjector
    fun provideGitHubLoginFragment(): GitHubLoginFragment

    @ContributesAndroidInjector(modules = [GitHubAuthModule::class])
    fun provideGitHubAuthDialogFragment(): GitHubAuthDialogFragment

    @ContributesAndroidInjector
    fun providePhoneLoginFragment(): PhoneLoginFragment

    @ContributesAndroidInjector
    fun provideEmailLoginFragment(): EmailLoginFragment

}