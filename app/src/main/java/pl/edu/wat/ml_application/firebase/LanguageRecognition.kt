package pl.edu.wat.ml_application.firebase

import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage


class LanguageRecognition {

    fun getLanguage(string: String) {
        val languageIdentifier = FirebaseNaturalLanguage.getInstance().languageIdentification
        languageIdentifier.identifyLanguage(string)
            .addOnSuccessListener { languageCode ->
                if (languageCode !== "und") {
                    print("Language:" + languageCode)
                } else {
                    print("Can't identify language.")
                }
            }
            .addOnFailureListener { e ->
                print(e.stackTrace)
                print(" Nie DZIAła")
            }
    }
}