package gs.firebase.demo

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.annotation.IdRes
import android.support.design.widget.TextInputLayout
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import gs.firebase.demo.models.Chat
import gs.firebase.demo.models.User
import gs.firebase.demo.views.LoadingAdapterObserver
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun View.ancestor(@IdRes withId: Int): View? =
        ancestor { it.id == withId }

fun View.ancestor(condition: (View) -> Boolean): View? = this.parent
        ?.takeIf { it is View }
        ?.let { (it as View).let { it.takeIf(condition) ?: it.ancestor(condition) } }

fun View.shake() = AnimationUtils
        .loadAnimation(context, R.anim.shake)
        .also(this::startAnimation)!!

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

fun RecyclerView.scrollDownOnInsert() {
    adapter.also {
        it.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                smoothScrollToPosition(positionStart)
            }
        })
    }
}

fun <T : RecyclerView.Adapter<*>> T.withLoading(loadingView: View?): T {
    registerAdapterDataObserver(LoadingAdapterObserver(
            onChange = { loadingView?.visibility = if (it) View.VISIBLE else View.GONE }
    ))
    return this
}

val RecyclerView.ViewHolder.context
    get() = itemView.context!!

fun String.toast(context: Context, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(context, this, duration).show()

fun Throwable.report(context: Context) {
    printStackTrace()

    localizedMessage?.toast(context)
}

fun <T> retrofit(baseUrl: String, service: Class<T>) =
        Retrofit.Builder()
                .client(OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
                .create(service)!!

val FirebaseDatabase.usersCollection
    get() =
        getReference("users")

val FirebaseDatabase.chatCollection
    get() =
        getReference("chat")

fun FirebaseUser.toModel() = User(
        id = uid,
        name = displayName,
        email = email,
        photoUrl = photoUrl?.toString())

fun DataSnapshot.toUser() =
        getValue(User::class.java)!!.also { it.id = key }

fun DataSnapshot.toChat() =
        getValue(Chat::class.java)!!.also { it.id = key }
