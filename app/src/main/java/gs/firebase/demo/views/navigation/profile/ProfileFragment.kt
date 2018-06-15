package gs.firebase.demo.views.navigation.profile

import android.os.Bundle
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
import dagger.android.support.DaggerFragment
import gs.firebase.demo.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.Exception
import javax.inject.Inject

class ProfileFragment : DaggerFragment(), FirebaseAuth.AuthStateListener, Callback, ValueEventListener {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var database: FirebaseDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_profile, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logout.setOnClickListener {
            auth.signOut()
        }
    }

    override fun onStart() {
        super.onStart()

        auth.addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()

        auth.removeAuthStateListener(this)
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        auth.currentUser?.also { user ->
            database.usersCollection
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
                auth.signOut()
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
