package gs.firebase.demo.views.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import gs.firebase.demo.R
import gs.firebase.demo.text
import gs.firebase.demo.views.CardWrapperFragment
import kotlinx.android.synthetic.main.fragment_login_email.*

class EmailLoginFragment : CardWrapperFragment() {

    override fun onCreateWrappedView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_login_email, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        login.setOnClickListener {
            val user = email.text
            val pass = password.text

            if (user != null && pass != null) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(user, pass)
                        .addOnFailureListener { e ->
                            e.printStackTrace()

                            password.error = e.localizedMessage
                        }
            }
        }
    }

}
