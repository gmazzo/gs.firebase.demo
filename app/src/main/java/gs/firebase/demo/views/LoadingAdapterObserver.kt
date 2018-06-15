package gs.firebase.demo.views

import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView

class LoadingAdapterObserver(private val delayMillis: Long = 200,
                             private val onChange: (Boolean) -> Unit) : RecyclerView.AdapterDataObserver(), Runnable {
    private val handler = Handler(Looper.getMainLooper())
    private val stopLoading = Runnable { loading = false }
    private var loading: Boolean = false
        set(value) {
            if (field != value) {
                field = value

                if (value) {
                    handler.apply {
                        removeCallbacks(stopLoading)
                        postDelayed(stopLoading, delayMillis)
                    }
                }

                onChange.invoke(value)
            }
        }

    init {
        loading = true
    }

    override fun onChanged() {
        loading = true
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        loading = true
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        loading = true
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        loading = true
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        loading = true
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        loading = true
    }

    override fun run() {
        loading = false
    }

}