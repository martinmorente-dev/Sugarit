package com.martin_dev.sugarit.backend.utilites.alert_prompt

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterSpToEn


class AlertPrompt()
{

    fun createAlertPrompt(reason: String, context: Context, inputUser: ((String) -> (Unit))? = null)
    {
        val builder = AlertDialog.Builder(context)
        val title = specificTitle(reason)
        val input = EditText(context)
        builder.setTitle(title)
        input.setHint(specificHint(reason))
        builder.setView(input)
        builder.setView(input)
        builder.setPositiveButton("Aceptar") { _, _ ->
            TranslaterSpToEn().translate(input.text.toString()) { translatedText ->
                inputUser?.invoke(translatedText.toString())
            }
        }
        showAlert(builder.create())
    }


    private fun specificHint(reason: String): String {
        var hint = ""
        when (reason) {
            "Food type" -> hint = "Ejemplo: Limon"
            "Food quantity" -> hint = "Ejemplo: 2"
            "Food or recipe" -> hint = "Ejemplo: comida o receta"
            "Recipe Type" -> hint = "Ejemplo: Espaguetis"
        }
        return (hint)
    }

    private fun specificTitle(reason: String): String
    {
        var title: String = ""
        when (reason) {
            "Food type" -> title = "Introduce que tipo de comida es la foto"
            "Food quantity" -> title = "Introduce la cantidad de alimentos que hay en la foto"
            "Recipe or Food" -> title = "¿Es un ingrediente o receta lo que hay en la foto?"
            "Recipe Type" -> title = "Introduce el tipo de receta"
        }
        return (title)
    }

    private fun showAlert(builder: AlertDialog)
    {
        val dialog: AlertDialog = builder
        dialog.show()
    }
}