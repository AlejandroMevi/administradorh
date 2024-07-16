package com.venturessoft.human.models.response

import com.google.gson.annotations.SerializedName

class ConsultaEstadisticaResponse {
    @SerializedName("codigo")
    var codigo: String = ""

    @SerializedName("diasEstadistica")
    var diasEstadisticaList: ArrayList<DiasEstadisticaItem>? = null
}