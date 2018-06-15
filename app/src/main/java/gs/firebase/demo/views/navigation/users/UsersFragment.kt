package gs.firebase.demo.views.navigation.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import dagger.android.support.DaggerFragment
import gs.firebase.demo.R
import gs.firebase.demo.decorate
import gs.firebase.demo.withLoading
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.fragment_users.*
import javax.inject.Inject

class UsersFragment : DaggerFragment() {

    @Inject
    lateinit var database: FirebaseDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_users, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.apply {
            decorate()

            adapter = UsersAdapter(this@UsersFragment).withLoading(activity!!.loading)
        }
    }

}
