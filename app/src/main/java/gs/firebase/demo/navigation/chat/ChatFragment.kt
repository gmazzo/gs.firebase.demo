package gs.firebase.demo.navigation.chat

import android.media.MediaPlayer
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
import gs.firebase.demo.*
import gs.firebase.demo.models.Chat
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_toolbar.*

class ChatFragment : Fragment(), TextView.OnEditorActionListener {
    lateinit var msnSound: MediaPlayer
    lateinit var nudgeSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        msnSound = MediaPlayer.create(context, R.raw.msn)
        nudgeSound = MediaPlayer.create(context, R.raw.nudge)
    }

    override fun onDestroy() {
        super.onDestroy()

        msnSound.release()
        nudgeSound.release()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_chat, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler.adapter = ChatAdapter(this).withLoading(activity!!.loading)
        recycler.scrollDownOnInsert()

        messageBox.setOnEditorActionListener(this)

        sendMessage.setOnClickListener {
            val text = messageBox.text
            messageBox.text = null
            messageBox.requestFocus()

            if (text.isNotBlank()) {
                sendMessage({ message = text.toString() })
            }
        }

        sendNudge.setOnClickListener {
            sendMessage({ nudge = true })
        }

        FirebaseRemoteConfig.getInstance().apply {
            sendNudge.visibility = if (getBoolean(BuildConfig.TOGGLE_NUDGE_ENABLED)) View.VISIBLE else View.GONE
        }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
        sendMessage.performClick()
        return true
    }

    private fun sendMessage(block: Chat.() -> Unit) =
            FirebaseDatabase.getInstance().chatCollection
                    .push()
                    .setValue(Chat(
                            userId = FirebaseAuth.getInstance().currentUser!!.uid,
                            timestamp = System.currentTimeMillis())
                            .apply(block))

}
