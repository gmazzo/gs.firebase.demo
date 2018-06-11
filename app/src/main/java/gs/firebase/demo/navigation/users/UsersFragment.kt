package gs.firebase.demo.navigation.users

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import gs.firebase.demo.R
import gs.firebase.demo.decorate
import gs.firebase.demo.models.User
import gs.firebase.demo.report
import gs.firebase.demo.usersCollection
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : Fragment(), ValueEventListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_users, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.decorate()

        FirebaseDatabase.getInstance().usersCollection.addValueEventListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        FirebaseDatabase.getInstance().usersCollection.removeEventListener(this)
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        val items = snapshot.children.map {
            it.getValue(User::class.java)!!.apply {
                id = it.key
            }
        }

        recycler?.swapAdapter(UsersAdapter(items), false)
    }

    override fun onCancelled(e: DatabaseError) {
        e.toException().report(context!!)
    }

}