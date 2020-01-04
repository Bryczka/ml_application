package pl.edu.wat.ml_application.firebase

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import java.lang.StringBuilder

class OcrRecognition{
    val data: MutableLiveData<String> = MutableLiveData()

    fun recognizeOCR(bitmap: Bitmap){

        val bitmap =
            bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val image =
            FirebaseVisionImage.fromBitmap(bitmap)
        val detector =
            FirebaseVision.getInstance().onDeviceTextRecognizer

        detector.processImage(image)
            .addOnSuccessListener { result ->
                data.value = processResultText(result)
            }
            .addOnFailureListener { e ->
                data.value = "Error during reading text"
            }
    }

    private fun processResultText(resultText: FirebaseVisionText) : String{

        if (resultText.textBlocks.size == 0) {
            return "Unable to recognize any text"
        }

        val text = StringBuilder()
        for (block in resultText.textBlocks) {
            val blockText = block.text
            text.append(blockText)
        }
        return text.toString()
    }
}
