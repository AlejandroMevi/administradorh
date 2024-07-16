package com.venturessoft.human.models

import com.google.gson.annotations.SerializedName

open class ProfileAccessResponse : BaseResponse() {
    @SerializedName("informacion")
    var information: Information? = null

    class Information {
        @SerializedName("nombre")
        var name: String = "Missing employee name"

        @SerializedName("cia")
        var company: String = "Missing company name"

        @SerializedName("foto")
        var photoUrl: String? = null

        @SerializedName("enrolador")
        var isAdmin: Boolean = false

        @SerializedName("usuarioExterno")
        var isExternal: Boolean = false
    }
}