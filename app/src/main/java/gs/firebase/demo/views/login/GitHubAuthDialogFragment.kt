package gs.firebase.demo.views.login

import android.annotation.SuppressLint
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import com.google.gson.annotations.SerializedName
import dagger.android.support.DaggerAppCompatDialogFragment
import gs.firebase.demo.BuildConfig
import gs.firebase.demo.R
import gs.firebase.demo.report
import kotlinx.android.synthetic.main.fragment_auth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import javax.inject.Inject

class GitHubAuthDialogFragment : DaggerAppCompatDialogFragment(), Callback<GitHubAuthDialogFragment.TokenResponse> {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    internal lateinit var api: GithubAPI

    // workaround: webView doesn't shows well if wrap_content and AppCompatDialog
    override fun onCreateDialog(savedInstanceState: Bundle?) =
            Dialog(activity!!, theme)

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
                        api.exchangeToken(code = Uri.parse(url).getQueryParameter("code"))
                                .enqueue(this@GitHubAuthDialogFragment)
                        return true
                    }
                    return false
                }

            }

            loadUrl("https://github.com/login/oauth/authorize?client_id=${BuildConfig.GITHUB_CLIENT_ID}")
        }
    }

    override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
        val credential = GithubAuthProvider.getCredential(response.body()!!.accessToken)

        auth.signInWithCredential(credential)
                .addOnFailureListener { e ->
                    e.report(context!!)
                }
                .addOnCompleteListener {
                    dismiss()
                }
    }

    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
        t.report(context!!)
    }

    internal interface GithubAPI {

        @POST("login/oauth/access_token")
        @FormUrlEncoded
        fun exchangeToken(@Header("Accept") accept: String = "application/json",
                          @Field("client_id") clientId: String = BuildConfig.GITHUB_CLIENT_ID,
                          @Field("client_secret") clientSecret: String = "1b45a7e60e764c83df5341eee15d388115f792dc",
                          @Field("code") code: String): Call<TokenResponse>

    }

    data class TokenResponse(@SerializedName("access_token") var accessToken: String)

}
