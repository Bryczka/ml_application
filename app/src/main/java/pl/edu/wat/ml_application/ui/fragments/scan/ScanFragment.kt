package pl.edu.wat.ml_application.ui.fragments.scan

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.ocr_fragment.*
import pl.edu.wat.ml_application.R
import pl.edu.wat.ml_application.ui.fragments.cropResult.CropResultFragment
import pl.edu.wat.ml_application.utils.LoadImage
import team.clevel.documentscanner.ImageCropActivity
import team.clevel.documentscanner.helpers.ScannerConstants
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class ScanFragment : Fragment() {

    companion object {
        fun newInstance() = ScanFragment()
    }

    private lateinit var viewModel: ScanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.scan_fragment, container, false)
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
            val scannerRequestCode = 4000
            val bitmap = (selectedImageImageView.drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
            ScannerConstants.selectedImageBitmap=bitmap
            startActivityForResult(Intent(this.context, ImageCropActivity::class.java),scannerRequestCode)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var uri : Uri = Uri.EMPTY
        try {
            if (requestCode == LoadImage.galleryRequestCode) {
                uri = data!!.data!!
                //bitmap = LoadImage.RotateBitmap.rotateImageIfRequired(bitmap!!, LoadImage.RotateBitmap.getRealPathFromURI(uri, this))
                selectedImageImageView.setImageBitmap(LoadImage.CreateBitmapFromUri.convert(uri, activity!!))
            } else if (requestCode == LoadImage.cameraRequestCode) {
                uri = Uri.fromFile(File(LoadImage.uri))
                selectedImageImageView.setImageBitmap(LoadImage.CreateBitmapFromUri.convert(uri, activity!!))
                //bitmap = LoadImage.RotateBitmap.rotateImageIfRequired(bitmap!!, uri.encodedPath!!)
            }
            else if (requestCode==4000 && resultCode== Activity.RESULT_OK )
            {
                if (ScannerConstants.selectedImageBitmap!=null)
                    prepareResult(ScannerConstants.selectedImageBitmap)
                else
                    Toast.makeText(requireContext(),"Something wen't wrong.",Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ScanViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun prepareResult(bitmap:Bitmap){
        val bundle = Bundle()
        bundle.putByteArray("Image", bitmapToArray(bitmap))
        val cropFragment = CropResultFragment.newInstance()
        cropFragment.arguments = bundle
        fragmentManager?.beginTransaction()!!.replace(R.id.container, cropFragment).addToBackStack(null).commit()
    }

    fun bitmapToArray(bitmap: Bitmap): ByteArray{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
        //bitmap.recycle()
    }
}
