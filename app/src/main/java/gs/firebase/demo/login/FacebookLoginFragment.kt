package gs.firebase.demo.login

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import gs.firebase.demo.R
import gs.firebase.demo.report
import kotlinx.android.synthetic.main.fragment_login_facebook.*

class FacebookLoginFragment : Fragment(), FacebookCallback<LoginResult> {
    private val callbackManager = CallbackManager.Factory.create()!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_login_facebook, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        login.also {
            it.setReadPermissions("public_profile", "email")
            it.fragment = this
            it.registerCallback(callbackManager, this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSuccess(result: LoginResult) {
        FirebaseAuth.getInstance().signInWithCredential(FacebookAuthProvider.getCredential(result.accessToken.token))
    }

    override fun onCancel() {
    }

    override fun onError(error: FacebookException) {
        error.report(context!!)
    }

}
