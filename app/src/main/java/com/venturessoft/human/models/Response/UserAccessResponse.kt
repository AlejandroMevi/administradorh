package com.venturessoft.human.models.response

import com.google.gson.annotations.SerializedName

class UserAccessResponse {
    @SerializedName("codigo")
    var codigo: String = ""
    var ambiente: String = ""
    var urlAvisoPrivacidad: String = ""

    @SerializedName("usuarioeTime")
    var usuarioeTime: usuarioeTime? = null
    var usuarioHuman: usuarioHumanItem? = null
}
