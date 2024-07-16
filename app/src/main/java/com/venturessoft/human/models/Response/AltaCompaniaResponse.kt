package com.venturessoft.human.models.response

import com.google.gson.annotations.SerializedName

class AltaCompaniaResponse {
    @SerializedName("codigo")
    var codigo: String = ""

    @SerializedName("idCompania")
    var idCompania: Long = 0
}