package gs.firebase.demo.views.navigation

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import gs.firebase.demo.R
import gs.firebase.demo.views.navigation.chat.ChatFragment
import gs.firebase.demo.views.navigation.profile.ProfileFragment
import gs.firebase.demo.views.navigation.users.UsersFragment
import kotlinx.android.synthetic.main.fragment_navigation.*

class NavigationFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_navigation, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigation.also {
            it.setOnNavigationItemSelectedListener(this)

            if (savedInstanceState == null) {
                it.selectedItemId = R.id.users
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment = when (item.itemId) {
            R.id.users -> UsersFragment()
            R.id.chat -> ChatFragment()
            R.id.profile -> ProfileFragment()

            else -> throw IllegalArgumentException("unknown menu: $item")
        }.apply { retainInstance = true }

        fragmentManager!!.beginTransaction()
                .replace(R.id.navigationContent, fragment, item.itemId.toString())
                .commit()
        return true
    }

}
