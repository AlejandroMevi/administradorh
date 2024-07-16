package com.venturessoft.human.models.response

import com.google.gson.annotations.SerializedName

class ConsultaFacturacionResponse {
    var codigo: String = ""

    @SerializedName("sfacturacion")
    var sfacturacion: ArrayList<sFacturacion>? = null
}

