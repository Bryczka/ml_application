package pl.edu.wat.ml_application.ui.fragments.hcr

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout

import pl.edu.wat.ml_application.R
import pl.edu.wat.ml_application.firebase.HCRRecognition

class HcrFragment : Fragment() {

    companion object {
        fun newInstance() = HcrFragment()
    }

    private lateinit var viewModel: HcrViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.hcr_fragment, container, false)
        val myCanvasView = MyCanvas(context!!)
        val frame = view.findViewById<FrameLayout>(R.id.frame)
        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener{
            val hcrProcessor = HCRRecognition()
            hcrProcessor.recognize(myCanvasView.extraBitmap)
        }
        myCanvasView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        frame.addView(myCanvasView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HcrViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
