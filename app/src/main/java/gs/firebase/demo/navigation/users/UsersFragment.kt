package gs.firebase.demo.navigation.users

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gs.firebase.demo.R
import gs.firebase.demo.decorate
import gs.firebase.demo.withLoading
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_users, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.apply {
            decorate()

            adapter = UsersAdapter(context).withLoading(activity!!.loading)
        }
    }

}
