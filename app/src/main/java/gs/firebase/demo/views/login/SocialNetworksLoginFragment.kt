package gs.firebase.demo.views.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import gs.firebase.demo.R
import gs.firebase.demo.views.CardWrapperFragment

class SocialNetworksLoginFragment : CardWrapperFragment() {

    override fun onCreateWrappedView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_login_social_networks, container, false)!!

}
