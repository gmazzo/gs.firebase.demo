package gs.firebase.demo

import com.google.firebase.iid.FirebaseInstanceIdService
import gs.firebase.demo.views.ConfigFragment

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()

        ConfigFragment.init(this)
    }

}
