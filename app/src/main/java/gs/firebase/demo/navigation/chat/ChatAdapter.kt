package gs.firebase.demo.navigation.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import gs.firebase.demo.FirebaseAdapter
import gs.firebase.demo.R
import gs.firebase.demo.chatCollection
import gs.firebase.demo.models.Chat
import gs.firebase.demo.toChat

class ChatAdapter(context: Context) : FirebaseAdapter<Chat, ChatViewHolder>(
        context,
        FirebaseDatabase.getInstance().chatCollection,
        DataSnapshot::toChat,
        { it.id!! }) {
    private var lastTimestamp = System.currentTimeMillis()

    override fun getItemViewType(position: Int) =
            if (position == 0 || getItem(position).userId != getItem(position - 1).userId)
                TYPE_FULL else TYPE_SINGLE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatViewHolder(
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat, parent, false),
            single = viewType == TYPE_SINGLE)

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = getItem(position)

        holder.isNew = item.timestamp!! > lastTimestamp
        holder.item = item
        lastTimestamp = Math.max(lastTimestamp, item.timestamp!!)
    }

    override fun onViewAttachedToWindow(holder: ChatViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.onAttached()
    }

    companion object {

        private const val TYPE_FULL = 0
        private const val TYPE_SINGLE = 1

    }

}
