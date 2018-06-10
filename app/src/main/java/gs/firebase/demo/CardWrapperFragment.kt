package gs.firebase.demo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_card_wrapper.view.*

abstract class CardWrapperFragment : Fragment() {

    abstract fun onCreateWrappedView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_card_wrapper, container, false).also { view ->
                view.cardWrapper.apply {
                    addView(onCreateWrappedView(inflater, this, savedInstanceState))
                }
            }

}