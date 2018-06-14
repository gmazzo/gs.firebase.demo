package gs.firebase.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import gs.firebase.demo.login.LoginFragment
import gs.firebase.demo.navigation.NavigationFragment

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        FirebaseAuth.getInstance().removeAuthStateListener(this)
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
