package gs.firebase.demo.views.navigation.chat

import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils.getRelativeTimeSpanString
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import gs.firebase.demo.*
import gs.firebase.demo.models.Chat
import gs.firebase.demo.models.User
import kotlinx.android.synthetic.main.item_chat.view.*
import java.lang.Exception

class ChatViewHolder(val database: FirebaseDatabase, view: View, single: Boolean) : RecyclerView.ViewHolder(view), Callback {
    var isNew: Boolean = false
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
        itemView.timestamp.text = getRelativeTimeSpanString(itemView.context, item.timestamp!!, false)
        itemView.message.text = if (item.nudge == true)
            context.getText(R.string.message_nudge) else
            item.message

        item.user?.also { user ->
            Picasso.get()
                    .load(user.photoUrl)
                    .into(itemView.photo, this)
        }
    }

    private fun lookupUser(userId: String) {
        database.usersCollection
                .child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        item?.also { item ->
                            if (item.userId == userId) { // checks if the holder was rebound to another item
                                item.user = snapshot.takeIf { snapshot.exists() }?.toUser() ?: User()

                                bindItem(item)
                            }
                        }
                    }

                    override fun onCancelled(e: DatabaseError) {
                        e.toException().report(itemView.context)
                    }

                })
    }

    fun onAttached(fragment: ChatFragment) {
        if (isNew) {
            isNew = false

            if (item?.nudge == true) {
                fragment.nudgeSound.start()
                itemView.ancestor(android.R.id.content)!!.shake()

            } else {
                fragment.msnSound.start()
            }
        }
    }

    override fun onSuccess() {
        itemView.photo.rounded()
    }

    override fun onError(e: Exception) {
        e.report(itemView.context)
    }

}
