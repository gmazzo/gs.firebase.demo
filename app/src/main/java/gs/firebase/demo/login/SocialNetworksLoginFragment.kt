package gs.firebase.demo.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import gs.firebase.demo.CardWrapperFragment
import gs.firebase.demo.R

class SocialNetworksLoginFragment : CardWrapperFragment() {

    override fun onCreateWrappedView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_login_social_networks, container, false)!!

}
