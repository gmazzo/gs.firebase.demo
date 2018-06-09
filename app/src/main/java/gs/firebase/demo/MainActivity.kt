package gs.firebase.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import gs.firebase.demo.login.LoginFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContent, LoginFragment(), null)
                    .commitNow()
        }
    }

}
