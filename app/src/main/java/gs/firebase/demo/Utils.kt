package gs.firebase.demo

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.design.widget.TextInputLayout
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import gs.firebase.demo.models.User

fun ImageView.rounded() =
        drawable?.takeIf { it is BitmapDrawable }?.apply {
            val bitmap = (this as BitmapDrawable).bitmap

            setImageDrawable(RoundedBitmapDrawableFactory.create(resources, bitmap).apply {
                isCircular = true
            })
        }


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

fun RecyclerView.decorate() =
        addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

fun String.toast(context: Context, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(context, this, duration).show()

fun Throwable.report(context: Context) {
    printStackTrace()

    localizedMessage?.toast(context)
}

fun FirebaseUser.toModel() = User(
        id = uid,
        name = displayName,
        email = email,
        photoUrl = photoUrl?.toString())

val FirebaseDatabase.usersCollection
    get() =
        getReference("users")
