package gs.firebase.demo.navigation.users

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import gs.firebase.demo.R
import gs.firebase.demo.models.User
import kotlinx.android.synthetic.main.item_user.view.*

class UsersAdapter(private val items: List<User>) : RecyclerView.Adapter<UsersViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemCount() = items.size

    override fun getItemId(position: Int) = items[position].id!!.hashCode().toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            UsersViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user, parent, false))

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val item = items[position]

        holder.apply {
            itemView.name.text = item.name
            itemView.email.text = item.email

            Picasso.get()
                    .load(item.photoUrl)
                    .into(itemView.photo, this)
        }
    }

}
