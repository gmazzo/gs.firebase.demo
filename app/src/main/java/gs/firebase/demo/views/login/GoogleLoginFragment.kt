package gs.firebase.demo.views.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.android.support.DaggerFragment
import gs.firebase.demo.R
import gs.firebase.demo.report
import kotlinx.android.synthetic.main.fragment_login_google.*
import javax.inject.Inject


class GoogleLoginFragment : DaggerFragment() {
    private lateinit var client: GoogleSignInClient

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        client = GoogleSignIn.getClient(activity!!, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_login_google, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        login.setOnClickListener {
            startActivityForResult(client.signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = GoogleSignIn.getSignedInAccountFromIntent(data)

            if (result.isSuccessful) {
                val credential = GoogleAuthProvider.getCredential(result.result.idToken, null)

                auth.signInWithCredential(credential)

            } else {
                result.exception?.report(context!!)
            }
        }
    }

    companion object {

        const val RC_SIGN_IN = 1001

    }

}
