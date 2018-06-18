package gs.firebase.demo.views.navigation.chat

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils.getRelativeTimeSpanString
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import gs.firebase.demo.*
import gs.firebase.demo.models.Chat
import gs.firebase.demo.models.User
import gs.firebase.demo.views.ProgressDrawable
import kotlinx.android.synthetic.main.item_chat.view.*
import java.lang.Exception

class ChatViewHolder(val database: FirebaseDatabase, val storage: FirebaseStorage, view: View, full: Boolean) : RecyclerView.ViewHolder(view), Callback, Target {
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
        arrayOf(itemView.name, itemView.timestamp, itemView.photo).forEach {
            it.isVisible = full
        }
    }

    private fun bindItem(item: Chat) {
        itemView.name.text = item.user?.name
        itemView.timestamp.text = getRelativeTimeSpanString(itemView.context, item.timestamp!!, false)
        itemView.message.text = when (item.type) {
            Chat.Type.MESSAGE -> item.message
            Chat.Type.NUDGE -> context.getText(R.string.message_nudge)
            Chat.Type.IMAGE -> null
        }
        itemView.imageMessage.isVisible = item.type == Chat.Type.IMAGE
        itemView.message.isVisible = !itemView.imageMessage.isVisible

        if (itemView.imageMessage.isVisible) {
            itemView.imageMessage.visibility = View.VISIBLE

            Picasso.get()
                    .load(item.imageUrl!!.toUri())
                    .placeholder(ProgressDrawable(context))
                    .into(this)

        } else {
            itemView.imageMessage.setImageDrawable(null)
        }

        item.user?.also { user ->
            Picasso.get()
                    .load(user.photoUrl)
                    .placeholder(ProgressDrawable(context))
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

            when (item!!.type) {
                Chat.Type.NUDGE -> {
                    fragment.nudgeSound.start()
                    itemView.ancestor(android.R.id.content)!!.shake()
                }

                else -> fragment.msnSound.start()
            }
        }
    }

    override fun onSuccess() {
        itemView.photo.rounded()
    }

    override fun onError(e: Exception) {
        e.report(context)
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable) {
        itemView.imageMessage.setImageDrawable(placeHolderDrawable)
    }

    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
        itemView.imageMessage.setImageDrawable(bitmap.toDrawable(itemView.context.resources))
    }

    override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {
        e.report(context)
    }

}
