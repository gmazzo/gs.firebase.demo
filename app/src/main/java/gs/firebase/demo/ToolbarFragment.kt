package gs.firebase.demo

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.fragment_toolbar.*
import java.lang.Exception

class ToolbarFragment : Fragment(), FirebaseAuth.AuthStateListener, Target {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_toolbar, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        FirebaseAuth.getInstance().addAuthStateListener(this)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        FirebaseAuth.getInstance().removeAuthStateListener(this)
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
