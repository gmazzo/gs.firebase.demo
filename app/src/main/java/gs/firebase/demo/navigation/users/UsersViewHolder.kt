package gs.firebase.demo.navigation.users

import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import gs.firebase.demo.models.User
import gs.firebase.demo.report
import gs.firebase.demo.rounded
import kotlinx.android.synthetic.main.item_user.view.*
import java.lang.Exception

class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view), Callback {
    var item: User? = null
        set(value) {
            field = value!!

            itemView.name.text = value.name
            itemView.email.text = value.email

            Picasso.get()
                    .load(value.photoUrl)
                    .into(itemView.photo, this)
        }

    override fun onSuccess() {
        itemView.photo.rounded()
    }

    override fun onError(e: Exception) {
        e.report(itemView.context)
    }

}
