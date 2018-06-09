package gs.firebase.demo.login

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import gs.firebase.demo.R


class LoginPagerAdapter(fragment: Fragment) : FragmentPagerAdapter(fragment.childFragmentManager) {
    private val items = fragment.resources.getTextArray(R.array.label_login_labels)

    override fun getCount() = items.size

    override fun getPageTitle(position: Int) = items[position]!!

    override fun getItem(position: Int) =
            when (position) {
                0 -> SocialNetworksLoginFragment()
                1 -> PhoneLoginFragment()
                2 -> EmailLoginFragment()
                else -> throw IllegalArgumentException("unknown fragment: $position")
            }

}
