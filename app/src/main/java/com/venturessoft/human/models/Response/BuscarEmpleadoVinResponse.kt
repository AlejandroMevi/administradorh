package com.venturessoft.human.models.response

import com.google.gson.annotations.SerializedName

class BuscarEmpleadoVinResponse {
    @SerializedName("return")
    var returnn: returnItem? = null

    @SerializedName("codigo")
    var codigo: String = ""
    var errorMessage: String = ""
    var nombre: String = ""
    var numeroCompania: String = ""
    var foto: String = ""
    var error: Boolean? = true
}
