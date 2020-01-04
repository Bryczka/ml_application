package pl.edu.wat.ml_application.ui.fragments.hcr

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import pl.edu.wat.ml_application.R

class HcrFragment : Fragment() {

    companion object {
        fun newInstance() = HcrFragment()
    }

    private lateinit var viewModel: HcrViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.hcr_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HcrViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
