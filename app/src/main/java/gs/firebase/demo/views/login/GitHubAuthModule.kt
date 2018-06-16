package gs.firebase.demo.views.login

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class GitHubAuthModule {

    @Provides
    internal fun provideGithubAPI(retrofit: Retrofit.Builder) =
            retrofit.baseUrl("https://github.com/")
                    .build()
                    .create(GitHubAuthDialogFragment.GithubAPI::class.java)

}