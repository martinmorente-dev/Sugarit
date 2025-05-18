package com.martin_dev.sugarit.backend.utilites.validation

import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterSpToEn


class AlertMessage
{
    fun createAlert(reason: String, context: Context, inputUser: ((String) -> (Unit))? = null)
    {
        val builder = AlertDialog.Builder(context)
        val message = specificMessage(reason)
        builder.setTitle("Error")
        builder.setMessage(message)
        if (reason == "alergiesInfo")
        {
            builder.setTitle("Introduce tus alérgenos")
            val input = EditText(context)
            builder.setView(input)
            builder.setPositiveButton("Aceptar"){_,_ ->
                TranslaterSpToEn().translate(input.text.toString()) { translatedText ->
                    inputUser?.invoke(translatedText.toString())
                }
            }
        }
        else
            builder.setPositiveButton("Aceptar",null)
        showAlert(builder.create())
    }

    private fun specificMessage(reason: String): String
    {
        var message: String = ""
        when(reason)
        {
            "Empty email or password" -> message = "Debes introducir usuario y contraseña"
            "Wrong email" -> message = "El email introducido no es válido"
            "Registration Failed" -> message = "Hubo un fallo inesperado a la hora de crear el usuario"
            "This user exist" -> message = "El usuario introducido ya existe en el sistema"
            "Malformed password Length" -> message = "La contraseña debe tener 6 o más carácteres"
            "Malformed password Upper" -> message = "La contraseña debe tener al menos una mayúscula"
            "Malformed password Lower" -> message = "La contraseña debe tener al menos una minúscula"
            "Malformed password Digit" -> message = "La contraseña debe tener al menos un digito"
            "Malformed password Special Char" -> message = "La contraseña tiene que tener caracteres especiales"
            "Email not sended" -> message = "Ahora mismo no se pudo enviar el correo de verificación inténtelo más tarde"
            "No url found" -> message = "La receta que busca no está disponible en este momento"
            "No food detected" -> message = "No se ha detectado ningún alimento en la foto"
            "Wrong answer" -> message = "Tienes que introducir si la foto es una comida o receta"
            "Image not detected" -> message = "No se detecto ningun alimento o receta por favor asegurese que la foto es la deseada"
        }
        return(message)
    }

    private fun showAlert(builder: AlertDialog)
    {
        val dialog: AlertDialog = builder
        dialog.show()
    }
}