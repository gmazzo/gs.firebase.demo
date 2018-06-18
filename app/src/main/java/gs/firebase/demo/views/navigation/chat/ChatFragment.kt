package gs.firebase.demo.views.navigation.chat

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.content.FileProvider
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage
import dagger.android.support.DaggerFragment
import gs.firebase.demo.*
import gs.firebase.demo.models.Chat
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_toolbar.*
import javax.inject.Inject

class ChatFragment : DaggerFragment(), TextView.OnEditorActionListener {
    private lateinit var tempCameraFile: Uri
    lateinit var msnSound: MediaPlayer
    lateinit var nudgeSound: MediaPlayer

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var database: FirebaseDatabase

    @Inject
    lateinit var storage: FirebaseStorage

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

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
        recycler.adapter = ChatAdapter(this)
                .apply { registerAdapterDataObserver(ShowEmptyMessageAdapterObserver(this)) }
                .withLoading(activity!!.loading)
        recycler.scrollDownOnInsert()

        messageBox.setOnEditorActionListener(this)

        sendMessage.setOnClickListener {
            val text = messageBox.text
            messageBox.text = null
            messageBox.requestFocus()

            if (text.isNotBlank()) {
                sendMessage { message = text.toString() }
            }
        }

        sendNudge.setOnClickListener {
            sendMessage { type = Chat.Type.NUDGE }
        }

        sendPhoto.setOnClickListener {
            context!!.apply {
                tempCameraFile = FileProvider.getUriForFile(this, packageName,
                        createTempFile("camera", directory = cacheDir))

                startActivityForResult(
                        createPickPhoto(getText(R.string.title_send_photo), tempCameraFile),
                        RC_SELECT_PHOTO)
            }
        }

        remoteConfig.apply {
            sendNudge.isVisible = getBoolean(BuildConfig.TOGGLE_NUDGE_ENABLED)
            sendPhoto.isVisible = getBoolean(BuildConfig.TOGGLE_PHOTO_ENABLED)
        }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
        sendMessage.performClick()
        return true
    }

    private fun sendMessage(block: Chat.() -> Unit) =
            database.chatCollection
                    .push()
                    .setValue(Chat(
                            userId = auth.currentUser!!.uid,
                            timestamp = System.currentTimeMillis())
                            .apply(block))

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SELECT_PHOTO && resultCode == RESULT_OK) {
            val userId = auth.currentUser!!.uid
            val imageId = System.currentTimeMillis().hashCode()
            val ref = storage.getReference("uploads/$userId/$imageId.png")

            storage.getReference("uploads/$userId/$imageId.png")
                    .putFile(data?.data ?: tempCameraFile)
                    .onSuccessTask { ref.downloadUrl }
                    .onSuccessTask { uri ->
                        sendMessage {
                            type = Chat.Type.IMAGE
                            imageUrl = uri.toString()
                        }
                    }
        }
    }

    inner class ShowEmptyMessageAdapterObserver(private val adapter: RecyclerView.Adapter<*>) : RecyclerView.AdapterDataObserver(), Runnable {
        private val handler = Handler(Looper.getMainLooper())

        init {
            showMessageIfEmpty()
        }

        override fun onChanged() {
            showMessageIfEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            showMessageIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            showMessageIfEmpty()
        }

        private fun showMessageIfEmpty() {
            handler.removeCallbacks(this)
            handler.postDelayed(this, 100)
        }

        override fun run() {
            empty?.isVisible = adapter.itemCount == 0
        }

    }

    companion object {
        private const val RC_SELECT_PHOTO = 1001
    }

}
