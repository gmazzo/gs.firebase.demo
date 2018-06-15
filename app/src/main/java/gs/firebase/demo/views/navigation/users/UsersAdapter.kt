package gs.firebase.demo.views.navigation.users

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import gs.firebase.demo.R
import gs.firebase.demo.models.User
import gs.firebase.demo.toUser
import gs.firebase.demo.usersCollection
import gs.firebase.demo.views.FirebaseAdapter

class UsersAdapter(fragment: UsersFragment) : FirebaseAdapter<User, UsersViewHolder>(
        fragment.context!!,
        fragment.database.usersCollection,
        DataSnapshot::toUser,
        { it.id!! }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            UsersViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user, parent, false))

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.item = getItem(position)
    }

}
