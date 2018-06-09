package gs.firebase.demo

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.widget.Toast

val TextInputLayout.text: String?
    get() {
        val text = editText!!.text.toString()

        if (text.isBlank()) {
            error = context.getString(R.string.err_field_required)
            return null

        } else {
            isErrorEnabled = false
        }
        return text
    }

fun String.toast(context: Context, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(context, this, duration)
