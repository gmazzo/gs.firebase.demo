package gs.firebase.demo.navigation.chat

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import gs.firebase.demo.BuildConfig
import gs.firebase.demo.R
import gs.firebase.demo.chatCollection
import gs.firebase.demo.models.Chat
import gs.firebase.demo.scrollDownOnInsert
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment(), TextView.OnEditorActionListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_chat, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler.adapter = ChatAdapter(context!!)
        recycler.scrollDownOnInsert()

        messageBox.setOnEditorActionListener(this)

        sendMessage.setOnClickListener {
            val text = messageBox.text
            messageBox.text = null
            messageBox.requestFocus()

            if (text.isNotBlank()) {
                FirebaseDatabase.getInstance().chatCollection
                        .push()
                        .setValue(Chat(
                                userId = FirebaseAuth.getInstance().currentUser!!.uid,
                                timestamp = System.currentTimeMillis(),
                                message = text.toString()))
            }
        }

        sendNudge.setOnClickListener {
            FirebaseDatabase.getInstance().chatCollection
                    .push()
                    .setValue(Chat(
                            userId = FirebaseAuth.getInstance().currentUser!!.uid,
                            timestamp = System.currentTimeMillis(),
                            nudge = true))
        }

        FirebaseRemoteConfig.getInstance().apply {
            sendNudge.visibility = if (getBoolean(BuildConfig.TOGGLE_NUDGE_ENABLED)) View.VISIBLE else View.GONE
        }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
        sendMessage.performClick()
        return true
    }

}
