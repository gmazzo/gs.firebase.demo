package gs.firebase.demo.views.navigation

import dagger.Module
import dagger.android.ContributesAndroidInjector
import gs.firebase.demo.views.navigation.chat.ChatFragment
import gs.firebase.demo.views.navigation.profile.ProfileFragment
import gs.firebase.demo.views.navigation.users.UsersFragment

@Module
interface NavigationModule {

    @ContributesAndroidInjector
    fun provideNavigationFragment(): NavigationFragment

    @ContributesAndroidInjector
    fun provideUsersFragment(): UsersFragment

    @ContributesAndroidInjector
    fun provideChatFragment(): ChatFragment

    @ContributesAndroidInjector
    fun provideProfileFragment(): ProfileFragment

}
