package com.venturessoft.human.models.response

import com.google.gson.annotations.SerializedName

class BusquedaZonaAdmiResponse {
    @SerializedName("codigo")
    var codigo: String = ""

    @SerializedName("zonas")
    var sZona: ArrayList<sZonas>? = null

    @SerializedName("idZona")
    var idZona: Int = 0

    @SerializedName("totalRegistros")
    var totalRegistros: Int = 0
}