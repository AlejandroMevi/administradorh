package com.venturessoft.human.models.response

import com.google.gson.annotations.SerializedName

class ConsultaEstacionResponse {
    var codigo: String = ""

    @SerializedName("sestacion")
    var sEstacion: ArrayList<sEstacion>? = null
}

