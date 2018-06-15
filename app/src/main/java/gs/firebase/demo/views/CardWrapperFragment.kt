package gs.firebase.demo.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import gs.firebase.demo.R
import kotlinx.android.synthetic.main.fragment_card_wrapper.view.*

abstract class CardWrapperFragment : DaggerFragment() {

    abstract fun onCreateWrappedView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_card_wrapper, container, false).also { view ->
                view.cardWrapper.apply {
                    addView(onCreateWrappedView(inflater, this, savedInstanceState))
                }
            }!!

}
