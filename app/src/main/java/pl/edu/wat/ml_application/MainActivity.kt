package pl.edu.wat.ml_application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.opencv.android.OpenCVLoader
import pl.edu.wat.ml_application.ui.fragments.main.MainFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        OpenCVLoader.initDebug()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}
