package pl.edu.wat.ml_application.firebase

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import pl.edu.wat.ml_application.utils.ProgressDialog

class Translator {

    var data: MutableLiveData<String> = MutableLiveData()

    fun translate(string: String, languages: IntArray, context: Context) {

        val dialog = ProgressDialog.progressDialog(context)
        val options =
            FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(languages[0])
                .setTargetLanguage(languages[1])
                .build()
        val translator =
            FirebaseNaturalLanguage.getInstance().getTranslator(options)

        dialog.show()
        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                translator.translate(string)
                    .addOnSuccessListener { translatedText ->
                        print(translatedText)
                        data.value = translatedText
                        dialog.dismiss()
                    }
                    .addOnFailureListener { e ->
                        data.value = "Error during translating text"
                        dialog.dismiss()
                    }
            }
            .addOnFailureListener { e ->
                print("error")
                data.value = "Error during preparing model"
                dialog.dismiss()
            }
    }
}