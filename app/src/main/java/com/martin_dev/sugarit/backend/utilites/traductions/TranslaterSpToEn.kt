package com.martin_dev.sugarit.backend.utilites.traductions

import android.util.Log
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslaterSpToEn()
{
    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.SPANISH)
        .setTargetLanguage(TranslateLanguage.ENGLISH)
        .build()


    private val translator = Translation.getClient(options)

    fun translate(message: String, onResult: ((String?) -> Unit)) {
        translator.downloadModelIfNeeded().addOnSuccessListener {
            Log.i("Translater Download", "Model downloaded")
            translator.translate(message).addOnSuccessListener { translatedText ->
                Log.i("Success Translater","Tanslated text: $translatedText")
                onResult(translatedText)
            }.addOnFailureListener { exception ->
                Log.e("Error Translate"," error: ${exception.message}")
                onResult(message)
            }
        }.addOnFailureListener { exception ->
            Log.e("Translater Error Download"," error: ${exception.message}")
            onResult(message)
        }
    }
}