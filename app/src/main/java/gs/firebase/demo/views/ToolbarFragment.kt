package gs.firebase.demo.views

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import dagger.android.support.DaggerFragment
import gs.firebase.demo.R
import kotlinx.android.synthetic.main.fragment_toolbar.*
import java.lang.Exception
import javax.inject.Inject

class ToolbarFragment : DaggerFragment(), FirebaseAuth.AuthStateListener, Target {

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_toolbar, container, false)!!

    override fun onStart() {
        super.onStart()

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        auth.addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()

        auth.removeAuthStateListener(this)
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        val user = auth.currentUser

        toolbar.title = user?.displayName ?: getString(R.string.app_name)

        if (user?.photoUrl != null) {
            Picasso.get().load(user.photoUrl)
                    .resizeDimen(R.dimen.icon_size, R.dimen.icon_size)
                    .into(this)

        } else {
            toolbar.navigationIcon = null
        }

        if (user == null) {
            LoginManager.getInstance().logOut()
        }
    }

    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
        toolbar?.navigationIcon = RoundedBitmapDrawableFactory.create(resources, bitmap).apply {
            isCircular = true
        }
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        e?.printStackTrace()

        toolbar.navigationIcon = null
    }

}
