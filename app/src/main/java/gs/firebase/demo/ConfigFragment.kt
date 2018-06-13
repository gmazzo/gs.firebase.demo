package gs.firebase.demo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_config.*

class ConfigFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_config, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val prefs = context!!.prefs

        arrayOf(notifGeneral, notifUsers, notifMessages).forEach { view ->
            val tag = view.tag as String

            view.isChecked = prefs.getBoolean(tag, true)

            view.setOnCheckedChangeListener { _, checked ->
                prefs.config(tag, checked)
            }
        }
    }

    companion object {
        private val firebase by lazy { FirebaseMessaging.getInstance() }

        fun init(context: Context) {
            context.prefs.let { prefs ->
                arrayOf(R.string.fcm_topic_general,
                        R.string.fcm_topic_new_users,
                        R.string.fcm_topic_new_messages)
                        .map { context.getString(it) }
                        .filter { !prefs.contains(it) }
                        .forEach {
                            prefs.config(it, true)
                        }
            }
        }

        private val Context.prefs
            get() = getSharedPreferences("config", Context.MODE_PRIVATE)

        private fun SharedPreferences.config(name: String, enabled: Boolean) {
            (if (enabled) firebase::subscribeToTopic else firebase::unsubscribeFromTopic)
                    .invoke(name)

            edit {
                putBoolean(name, enabled)
            }
        }
    }

}

