package pl.edu.wat.ml_application.ui.fragments.translate

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.translate_fragment.*

import pl.edu.wat.ml_application.R
import pl.edu.wat.ml_application.firebase.OcrRecognition
import pl.edu.wat.ml_application.firebase.Translator
import pl.edu.wat.ml_application.ui.fragments.baseResult.BaseResultFragment
import pl.edu.wat.ml_application.utils.LoadImage
import pl.edu.wat.ml_application.utils.StaticValues.Companion.languages
import java.io.File
import java.io.IOException

class TranslateFragment : Fragment() {
    val translateProcessor = Translator()
    companion object {
        fun newInstance() = TranslateFragment()
    }

    private lateinit var viewModel: TranslateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.translate_fragment, container, false)
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        val spinner2 = view.findViewById<Spinner>(R.id.spinner2)
        val switch = view.findViewById<Switch>(R.id.switch1)
        val cameraFloatingButton =
            view.findViewById<FloatingActionButton>(R.id.floatingCameraButton)
        val galleryFloatingButton =
            view.findViewById<FloatingActionButton>(R.id.floatingGalleryButton)
        val startFloatingButton = view.findViewById<FloatingActionButton>(R.id.floatingStartButton)
        val selectedImageImageView = view.findViewById<ImageView>(R.id.selectedImageImageView)


        cameraFloatingButton.setOnClickListener {
            LoadImage.Camera.getCamera(this)
        }
        galleryFloatingButton.setOnClickListener {
            LoadImage.Gallery.getGallery(this)
        }

        startFloatingButton.setOnClickListener{
            translateProcessor.data.observe(this, Observer { onTranslateResult(it) })

            if(switch.isChecked){
                translateProcessor.translate(editText2.text.toString(),
                    intArrayOf(spinner.selectedItemPosition, spinner2.selectedItemPosition), context!!)
            }
            else{
                recognizeTextToTranslate((selectedImageImageView.drawable as BitmapDrawable).bitmap)
            }

        }

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editText2.visibility=View.VISIBLE
                editText2.isEnabled = true
                selectedImageImageView.visibility = View.INVISIBLE
                selectedImageImageView.isEnabled = false
            } else {
                editText2.visibility=View.INVISIBLE
                editText2.isEnabled = false
                selectedImageImageView.visibility = View.VISIBLE
                selectedImageImageView.isEnabled = true
            }
        }

        cameraFloatingButton.setOnClickListener {
            LoadImage.Camera.getCamera(this)

        }
        galleryFloatingButton.setOnClickListener {
            LoadImage.Gallery.getGallery(this)

        }
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, languages)
        spinner2.adapter = adapter
        languages.add("Detect")
        spinner.adapter = adapter
        spinner.setSelection(languages.lastIndex)

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var uri : Uri = Uri.EMPTY

        try {
            if (requestCode == LoadImage.galleryRequestCode) {
                uri = data!!.data!!
                //bitmap = LoadImage.RotateBitmap.rotateImageIfRequired(bitmap!!, LoadImage.RotateBitmap.getRealPathFromURI(uri, this))
            } else if (requestCode == LoadImage.cameraRequestCode) {
                uri = Uri.fromFile(File(LoadImage.uri))
                //bitmap = LoadImage.RotateBitmap.rotateImageIfRequired(bitmap!!, uri.encodedPath!!)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        selectedImageImageView.setImageBitmap(LoadImage.CreateBitmapFromUri.convert(uri, activity!!))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TranslateViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun onTranslateResult(data:String){
        val bundle = Bundle()
        bundle.putString("translateResult", data)
        val resultFragment = BaseResultFragment.newInstance()
        resultFragment.arguments = bundle
        fragmentManager?.beginTransaction()!!.replace(R.id.container, resultFragment).addToBackStack(null).commit()
    }

    private fun recognizeTextToTranslate(bitmap: Bitmap){
        val ocrProcessor = OcrRecognition()
        ocrProcessor.data.observe(this, Observer { onOcrResult(it) })
        if(bitmap != null)
            ocrProcessor.recognizeOCR(bitmap)
    }

    private fun onOcrResult(data:String){
        translateProcessor.translate(data,
            intArrayOf(spinner.selectedItemPosition, spinner2.selectedItemPosition), context!!)
    }

}
