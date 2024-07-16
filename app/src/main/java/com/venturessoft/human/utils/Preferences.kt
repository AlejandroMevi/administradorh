package com.venturessoft.human.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.venturessoft.human.models.User
import com.venturessoft.human.models.sharedpreferences.AvisoModel
import com.venturessoft.human.services.Lenguaje
import java.util.Locale

class Preferences {
    companion object {
        private const val APP_SETTINGS = "APP_SETTINGS"
        private const val KEY_SPLASH = "splash"
        private const val KEY_USER = "user"
        private const val KEY_LENGUAJE = "lenguaje"
        private const val KEY_AVISO = "aviso"

        private val SUPPORTED_LANGUAGES = arrayOf("en", "es", "pt")
        private const val DEFAULT_LANGUAGE = "es"
        private var LANGUAGE = "en"

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
        }

        fun showSplash(context: Context): Boolean {

            return getSharedPreferences(context).getBoolean(KEY_SPLASH, true)

        }

        fun editSplashStatus(edit: Boolean, context: Context) {
            val editor = getSharedPreferences(context).edit()
            editor.putBoolean(KEY_SPLASH, edit)
            editor.apply()
        }

        fun getUser(context: Context): User? {
            val gson = Gson()
            //println("Preferences-> "+gson)
            return try {
                gson.fromJson(getSharedPreferences(context).getString(KEY_USER, null), User::class.java)
            } catch (e: Exception) {
                null
            }
        }

        fun getLanguage(context: Context): Lenguaje? {
            val gson = Gson()
            //println("Preferences-> "+gson)
            return try {
                gson.fromJson(getSharedPreferences(context).getString(KEY_LENGUAJE, null), Lenguaje::class.java)
            } catch (e: Exception) {
                null
            }
        }



        fun editLenguaje(lenguaje: Lenguaje, context: Context) {
            val editor = getSharedPreferences(context).edit()
            val gson = Gson()
            val json = gson.toJson(lenguaje)
            editor.putString(KEY_LENGUAJE, json)
            editor.apply()
        }


        fun setLanguage(newLanguage: String) {
            LANGUAGE = if (SUPPORTED_LANGUAGES.contains(newLanguage.lowercase(Locale.ROOT))) {
                newLanguage
            } else {
                DEFAULT_LANGUAGE
            }
        }

        fun getAviso(context: Context?): AvisoModel? {
            val gson = Gson()
            return try {
                gson.fromJson(getSharedPreferences(context!!).getString(KEY_AVISO, null), AvisoModel::class.java)
            } catch (e: Exception) {
                null
            }
        }
        fun editAviso(aviso: AvisoModel, context: Context) {
            val editor = getSharedPreferences(context).edit()
            val gson = Gson()
            val json = gson.toJson(aviso)
            println("El aviso es:  JSON :$json")
            editor.putString(KEY_AVISO, json)
            editor.apply()
        }

    }
}
