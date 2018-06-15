package gs.firebase.demo.views.navigation.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import gs.firebase.demo.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.Exception

class ProfileFragment : Fragment(), FirebaseAuth.AuthStateListener, Callback, ValueEventListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_profile, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }
    }

    override fun onStart() {
        super.onStart()

        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()

        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        auth.currentUser?.also { user ->
            FirebaseDatabase.getInstance().usersCollection
                    .child(user.uid)
                    .addListenerForSingleValueEvent(this)
        }
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        snapshot.apply {
            if (exists()) {
                toUser().also { user ->
                    name.text = user.name
                    email.text = user.email

                    Picasso.get()
                            .load(user.photoUrl)
                            .into(photo, this@ProfileFragment)
                }

            } else {
                FirebaseAuth.getInstance().signOut()
            }
        }
    }

    override fun onCancelled(e: DatabaseError) {
        e.toException().report(context!!)
    }

    override fun onSuccess() {
        photo?.rounded()
    }

    override fun onError(e: Exception) {
        e.report(context!!)
    }

}
