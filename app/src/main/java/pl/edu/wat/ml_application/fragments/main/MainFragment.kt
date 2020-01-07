package pl.edu.wat.ml_application.ui.fragments.main

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import android.provider.UserDictionary
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.edu.wat.ml_application.R
import pl.edu.wat.ml_application.firebase.OcrRecognition
import pl.edu.wat.ml_application.ui.fragments.about.AboutFragment
import pl.edu.wat.ml_application.ui.fragments.hcr.HcrFragment
import pl.edu.wat.ml_application.ui.fragments.ocr.OcrFragment
import pl.edu.wat.ml_application.ui.fragments.scan.ScanFragment
import pl.edu.wat.ml_application.ui.fragments.translate.TranslateFragment
import pl.edu.wat.ml_application.utils.LoadImage
import java.io.File
import java.io.IOException

class MainFragment : Fragment() {

    private val permissionsRequestCode = 0
    private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET)

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        val hcrButton = view.findViewById<Button>(R.id.hcrButton)
        val ocrButton = view.findViewById<Button>(R.id.ocrButton)
        val translateButton = view.findViewById<Button>(R.id.translateButton)
        val scanButton = view.findViewById<Button>(R.id.scanButton)
        val aboutButton = view.findViewById<Button>(R.id.aboutButton)

        hcrButton.setOnClickListener {
            fragmentManager?.beginTransaction()!!.replace(R.id.container, HcrFragment.newInstance()).addToBackStack(null).commit()
        }
        ocrButton.setOnClickListener {
            fragmentManager?.beginTransaction()!!.replace(R.id.container, OcrFragment.newInstance()).addToBackStack(null).commit()
        }
        translateButton.setOnClickListener {
            fragmentManager?.beginTransaction()!!.replace(R.id.container, TranslateFragment.newInstance()).addToBackStack(null).commit()
        }
        scanButton.setOnClickListener {
            fragmentManager?.beginTransaction()!!.replace(R.id.container, ScanFragment.newInstance()).addToBackStack(null).commit()
        }
        aboutButton.setOnClickListener {
            fragmentManager?.beginTransaction()!!.replace(R.id.container, AboutFragment.newInstance()).addToBackStack(null).commit()
        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun permissionsGranted(context: Context): Boolean {
        for (permission in permissions) {
            if (checkSelfPermission(context ,permission) !== PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }
}
