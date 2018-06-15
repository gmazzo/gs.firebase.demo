package gs.firebase.demo.views

import dagger.Module
import dagger.android.ContributesAndroidInjector
import gs.firebase.demo.MainActivity
import gs.firebase.demo.views.login.LoginModule
import gs.firebase.demo.views.navigation.NavigationModule

@Module(includes = [LoginModule::class, NavigationModule::class])
interface ViewsModule {

    @ContributesAndroidInjector
    fun provideMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun provideToolbarFragment(): ToolbarFragment

    @ContributesAndroidInjector
    fun provideConfigFragment(): ConfigFragment

}