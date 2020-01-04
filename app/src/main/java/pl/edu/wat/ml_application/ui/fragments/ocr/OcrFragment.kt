package pl.edu.wat.ml_application.ui.fragments.ocr

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.ocr_fragment.*
import pl.edu.wat.ml_application.R
import pl.edu.wat.ml_application.firebase.HCRRecognition
import pl.edu.wat.ml_application.firebase.OcrRecognition
import pl.edu.wat.ml_application.ui.fragments.baseResult.BaseResultFragment
import pl.edu.wat.ml_application.utils.LoadImage
import java.io.File
import java.io.IOException

class OcrFragment : Fragment() {
    var uri : Uri = Uri.EMPTY
    companion object {
        fun newInstance() = OcrFragment()
    }

    private lateinit var viewModel: OcrViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.ocr_fragment, container, false)
        val cameraFloatingButton = view.findViewById<FloatingActionButton>(R.id.floatingCameraButton)
        val galleryFloatingButton = view.findViewById<FloatingActionButton>(R.id.floatingGalleryButton)
        val startFloatingButton = view.findViewById<FloatingActionButton>(R.id.floatingStartButton)
        val selectedImageImageView = view.findViewById<ImageView>(R.id.selectedImageImageView)
        val ocrProcessor = OcrRecognition()

        cameraFloatingButton.setOnClickListener {
            LoadImage.Camera.getCamera(this)
        }

        galleryFloatingButton.setOnClickListener {
            LoadImage.Gallery.getGallery(this)
        }

        startFloatingButton.setOnClickListener {
            //val hcrProcessor = HCRRecognition()
            //hcrProcessor.recognize((selectedImageImageView.drawable as BitmapDrawable).bitmap)
            ocrProcessor.data.observe(this, Observer { onOcrResult(it) })
            if(selectedImageImageView.drawable != null)
            ocrProcessor.recognizeOCR((selectedImageImageView.drawable as BitmapDrawable).bitmap)
            else
                Toast.makeText(context, "First select image!!", Toast.LENGTH_LONG).show()
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == LoadImage.galleryRequestCode) {
                uri = data!!.data!!
            } else if (requestCode == LoadImage.cameraRequestCode) {
                uri = Uri.fromFile(File(LoadImage.uri))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        selectedImageImageView.setImageBitmap(LoadImage.CreateBitmapFromUri.convert(uri, activity!!))
    }
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OcrViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun onOcrResult(data:String){
        val bundle = Bundle()
        bundle.putString("ocrResult", data)
        val resultFragment = BaseResultFragment.newInstance()
        resultFragment.arguments = bundle
        fragmentManager?.beginTransaction()!!.replace(R.id.container, resultFragment).addToBackStack(null).commit()
    }
}
