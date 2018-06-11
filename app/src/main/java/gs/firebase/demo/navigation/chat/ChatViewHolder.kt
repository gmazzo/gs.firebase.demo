package gs.firebase.demo.navigation.chat

import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import gs.firebase.demo.models.Chat
import gs.firebase.demo.report
import gs.firebase.demo.rounded
import gs.firebase.demo.toUser
import gs.firebase.demo.usersCollection
import kotlinx.android.synthetic.main.item_chat.view.*
import java.lang.Exception

class ChatViewHolder(view: View, single: Boolean) : RecyclerView.ViewHolder(view), Callback {
    var item: Chat? = null
        set(value) {
            field = value!!

            if (value.user == null) {
                lookupUser(value.userId!!)
            }

            bindItem(value)
        }

    init {
        val visible = if (single) View.GONE else View.VISIBLE

        arrayOf(itemView.name, itemView.timestamp, itemView.photo).forEach {
            it.visibility = visible
        }
    }

    private fun bindItem(item: Chat) {
        itemView.name.text = item.user?.name
        itemView.timestamp.text = DateUtils.formatDateTime(itemView.context, item.timestamp!!, 0)
        itemView.message.text = item.message

        itemView.photo?.also { imageView ->
            val user = item.user

            if (user != null) {
                Picasso.get()
                        .load(user.photoUrl)
                        .into(imageView, this)
            }
        }
    }

    private fun lookupUser(userId: String) {
        FirebaseDatabase.getInstance().usersCollection
                .child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        item?.also { item ->
                            if (item.userId == userId) { // checks if the holder was rebound to another item
                                item.user = snapshot.toUser()

                                bindItem(item)
                            }
                        }
                    }

                    override fun onCancelled(e: DatabaseError) {
                        e.toException().report(itemView.context)
                    }

                })
    }

    override fun onSuccess() {
        itemView.photo.rounded()
    }

    override fun onError(e: Exception) {
        e.report(itemView.context)
    }

}
