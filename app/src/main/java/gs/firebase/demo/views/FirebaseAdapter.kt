package gs.firebase.demo.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import gs.firebase.demo.report

abstract class FirebaseAdapter<T, VH : RecyclerView.ViewHolder>(
        context: Context,
        private val reference: DatabaseReference,
        private val converter: (DataSnapshot) -> T,
        private val idGetter: (T) -> Comparable<*>) : RecyclerView.Adapter<VH>(), ChildEventListener {

    private val context = context.applicationContext
    private val items = mutableListOf<T>()

    init {
        @Suppress("LeakingThis")
        setHasStableIds(true)
    }

    override fun getItemCount() = items.size

    open fun getItem(position: Int): T = items[position]

    override fun getItemId(position: Int) =
            idGetter.invoke(getItem(position)).hashCode().toLong()

    open fun getPosition(item: T) =
            idGetter.invoke(item).let { id ->
                items.indexOfFirst { idGetter.invoke(it) == id }
            }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        reference.addChildEventListener(this)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        reference.removeEventListener(this)
    }

    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        items.add(converter.invoke(snapshot))

        notifyItemInserted(items.size - 1)
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        converter.invoke(snapshot).apply {
            val position = getPosition(this)

            items[position] = this
            notifyItemChanged(position)
        }
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        converter.invoke(snapshot).apply {
            val position = getPosition(this)

            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCancelled(e: DatabaseError) {
        e.toException().report(context)
    }

}
