package gs.firebase.demo

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import dagger.android.support.DaggerAppCompatActivity
import gs.firebase.demo.views.login.LoginFragment
import gs.firebase.demo.views.navigation.NavigationFragment
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), FirebaseAuth.AuthStateListener {

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        auth.addAuthStateListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        auth.removeAuthStateListener(this)
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        val hasUser = auth.currentUser != null

        // shows the proper screen (login or navigation)
        supportFragmentManager!!.apply {
            val current = findFragmentById(R.id.mainContent)

            if (!hasUser && current !is LoginFragment) {
                beginTransaction()
                        .replace(R.id.mainContent, LoginFragment(), null)
                        .commitNow()

            } else if (hasUser && (current is LoginFragment || current == null)) {
                beginTransaction()
                        .replace(R.id.mainContent, NavigationFragment(), null)
                        .commitNow()
            }
        }
    }

}
