package gs.firebase.demo.views

import android.content.Context
import android.graphics.Color
import android.support.v4.widget.CircularProgressDrawable
import androidx.core.content.withStyledAttributes
import gs.firebase.demo.R

class ProgressDrawable(context: Context) : CircularProgressDrawable(context) {
    private var intrinsicSize: Int = 0

    init {
        context.withStyledAttributes(0, R.styleable.ProgressDrawable) {
            setStyle(CircularProgressDrawable.LARGE)
            setColorSchemeColors(getColor(R.styleable.ProgressDrawable_colorPrimary, Color.BLACK))
            intrinsicSize = getDimensionPixelSize(R.styleable.ProgressDrawable_actionBarSize, intrinsicSize)
        }

        start()
    }

    override fun getIntrinsicWidth() = intrinsicSize

    override fun getIntrinsicHeight() = intrinsicSize

}
