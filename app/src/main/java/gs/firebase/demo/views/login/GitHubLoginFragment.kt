package gs.firebase.demo.views.login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gs.firebase.demo.R
import kotlinx.android.synthetic.main.fragment_login_github.*

class GitHubLoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_login_github, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        github.setOnClickListener {
            childFragmentManager.beginTransaction()
                    .add(GitHubAuthDialogFragment(), null)
                    .commit()
        }
    }

}