package gs.firebase.demo.navigation.chat

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import gs.firebase.demo.R
import gs.firebase.demo.chatCollection
import gs.firebase.demo.models.Chat
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment(), TextWatcher {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_chat, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler.adapter = ChatAdapter(context!!)

        messageBox.addTextChangedListener(this)

        sendMessage.setOnClickListener {
            val text = messageBox.text
            messageBox.text = null
            messageBox.requestFocus()

            FirebaseDatabase.getInstance().chatCollection
                    .push()
                    .setValue(Chat(
                            userId = FirebaseAuth.getInstance().currentUser!!.uid,
                            timestamp = System.currentTimeMillis(),
                            message = text.toString()))
        }
    }

    override fun afterTextChanged(s: Editable) {
        sendMessage.isEnabled = s.isNotBlank()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

}
