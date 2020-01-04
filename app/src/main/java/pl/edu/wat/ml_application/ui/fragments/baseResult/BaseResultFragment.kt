package pl.edu.wat.ml_application.ui.fragments.baseResult

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import pl.edu.wat.ml_application.R

class BaseResultFragment : Fragment() {

    companion object {
        fun newInstance() = BaseResultFragment()
    }

    private lateinit var viewModel: BaseResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.base_result_fragment, container, false)
        val resultTextView = view.findViewById<TextView>(R.id.resultTextView)

        resultTextView.setText(arguments!!.getString("ocrResult"))
        resultTextView.setText(arguments!!.getString("translateResult"))
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BaseResultViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
