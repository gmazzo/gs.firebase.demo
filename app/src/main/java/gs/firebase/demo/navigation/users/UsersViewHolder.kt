package gs.firebase.demo.navigation.users

import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Callback
import gs.firebase.demo.report
import gs.firebase.demo.rounded
import kotlinx.android.synthetic.main.item_user.view.*
import java.lang.Exception

class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view), Callback {

    override fun onSuccess() {
        itemView.photo.rounded()
    }

    override fun onError(e: Exception) {
        e.report(itemView.context)
    }

}
