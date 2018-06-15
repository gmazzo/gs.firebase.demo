package gs.firebase.demo.views.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import gs.firebase.demo.R
import gs.firebase.demo.text
import gs.firebase.demo.views.CardWrapperFragment
import kotlinx.android.synthetic.main.fragment_login_email.*
import kotlinx.android.synthetic.main.fragment_login_phone.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PhoneLoginFragment : CardWrapperFragment() {

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateWrappedView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_login_phone, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        verificationMode(false)

        requestCode.setOnClickListener {
            val number = phone.text

            if (number != null) {
                verificationMode(true)

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        number, 60, TimeUnit.SECONDS, activity!!, Callback())
            }
        }

        validateCode.setOnClickListener {
            val email = email.text
            val password = password.text

            if (email != null && password != null) {
                auth.signInWithEmailAndPassword(email, password)
            }
        }
    }

    private fun verificationMode(enabled: Boolean) {
        phone.editText!!.isEnabled = !enabled
        requestCode.visibility = if (enabled) View.GONE else View.VISIBLE
        validationCode.visibility = if (enabled) View.VISIBLE else View.GONE
        validateCode.visibility = validationCode.visibility
    }

    inner class Callback : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            auth.signInWithCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            e.printStackTrace()

            verificationMode(false)
            phone.error = e.localizedMessage
        }

    }

}
