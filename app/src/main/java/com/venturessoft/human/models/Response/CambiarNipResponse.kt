package com.venturessoft.human.models.response

import com.google.gson.annotations.SerializedName

class CambiarNipResponse {
    @SerializedName("return")
    var cambiarNipResultado: cambiarNipResultado? = null

    @SerializedName("codigo")
    var codigo: String = ""
}