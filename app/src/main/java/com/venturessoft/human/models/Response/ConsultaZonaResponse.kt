package com.venturessoft.human.models.response

import com.google.gson.annotations.SerializedName

class ConsultaZonaResponse {
    //Se agreoó @SerializedName "codigo"
    @SerializedName("codigo")
    var codigo: String = ""

    //Se agreoó @SerializedName "totalRegistros"+
    @SerializedName("totalRegistros")
    var totalRegistros: Int = 0

    @SerializedName("sZona")
    var zZona: ArrayList<sZonas>? = null
}
