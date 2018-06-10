package gs.firebase.demo.login

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import com.google.gson.annotations.SerializedName
import gs.firebase.demo.BuildConfig
import gs.firebase.demo.R
import gs.firebase.demo.toast
import kotlinx.android.synthetic.main.fragment_auth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

class GitHubAuthDialogFragment : DialogFragment(), Callback<GitHubAuthDialogFragment.TokenResponse> {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_auth, container, false)!!

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webView.apply {
            settings.javaScriptEnabled = true
            settings.setAppCacheEnabled(false)

            webViewClient = object : WebViewClient() {

                @Suppress("OverridingDeprecatedMember")
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    if (url.startsWith(BuildConfig.FIREBASE_AUTH_ENDPOINT)) {
                        handleCallback(Uri.parse(url))
                        return true
                    }
                    return false
                }

            }

            loadUrl("https://github.com/login/oauth/authorize?client_id=${BuildConfig.GITHUB_CLIENT_ID}")
        }
    }

    private fun handleCallback(uri: Uri) {
        Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://github.com/")
                .build()
                .create(GithubAPI::class.java)
                .exchangeToken(code = uri.getQueryParameter("code"))
                .enqueue(this)

    }

    override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
        val credential = GithubAuthProvider.getCredential(response.body()!!.accessToken)

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    e.localizedMessage.toast(context!!)
                }
                .addOnCompleteListener {
                    dismiss()
                }
    }

    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
        t.printStackTrace()
        t.localizedMessage.toast(context!!)
    }

    private interface GithubAPI {

        @POST("login/oauth/access_token")
        @FormUrlEncoded
        fun exchangeToken(@Header("Accept") accept: String = "application/json",
                          @Field("client_id") clientId: String = BuildConfig.GITHUB_CLIENT_ID,
                          @Field("client_secret") clientSecret: String = "1b45a7e60e764c83df5341eee15d388115f792dc",
                          @Field("code") code: String): Call<TokenResponse>

    }

    data class TokenResponse(@SerializedName("access_token") var accessToken: String)

}
