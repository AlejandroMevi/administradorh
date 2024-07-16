package com.venturessoft.human.models.response

import com.google.gson.annotations.SerializedName

class EnrolarResponse {
    @SerializedName("return")
    var returnEnrolar: returnEnrolarItem? = null
    var error: Boolean = true
    var resultado: String = ""
    var codigoError: String = ""
}
