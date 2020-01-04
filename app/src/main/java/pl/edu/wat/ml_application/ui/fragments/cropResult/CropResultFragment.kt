package pl.edu.wat.ml_application.ui.fragments.cropResult

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.createBitmap

import pl.edu.wat.ml_application.R
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.stream.Stream

class CropResultFragment : Fragment() {

    companion object {
        fun newInstance() = CropResultFragment()
    }

    private lateinit var viewModel: CropResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.crop_result_fragment, container, false)
        val imageResult = view.findViewById<ImageView>(R.id.resultImageView)
        imageResult.setImageBitmap(BitmapFactory.decodeStream(ByteArrayInputStream(arguments!!.getByteArray("Image"))))
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CropResultViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
