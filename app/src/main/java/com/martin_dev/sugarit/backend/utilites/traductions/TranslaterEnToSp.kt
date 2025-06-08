package com.martin_dev.sugarit.backend.utilites.traductions

import android.util.Log
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslaterEnToSp
{
    private var options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.SPANISH)
        .build()

    private val translator = Translation.getClient(options)

    fun translate(message: String, onResult: (String?) -> Unit) {
        translator.downloadModelIfNeeded().addOnSuccessListener {
            translator.translate(message).addOnSuccessListener { translatedText ->
                onResult(translatedText)
            }.addOnFailureListener { exception ->
                onResult(message)
            }
        }.addOnFailureListener { exception ->
            onResult(message)
        }
    }
}