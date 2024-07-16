package com.venturessoft.human.models


import com.google.gson.annotations.SerializedName

open class LoginResponse : BaseResponse() {
    @SerializedName("informacion")
    var information: Information? = null

    class Information {
        @SerializedName("nombre")
        var name: String = "Missing employee name"

        @SerializedName("cia")
        var company: String = "Missing company name"

        @SerializedName("enrolador")
        var isAdmin: Boolean = false

        @SerializedName("usuarioExterno") //usuarioExterno
        var isExternal: Boolean = false

        @SerializedName("tiempoBloqueoInicial")
        var timeLoginBlocked: Int = -1

        @SerializedName("intentoBloqueoInicial")
        var maxLoginAttempts: Int = -1

        @SerializedName("tiempoBloqueoReconocimiento")
        var timeRecognitionBlock: Int = -1

        @SerializedName("intentoBloqueoReconocimiento")
        var maxRecognitionAttempts: Int = -1

        @SerializedName("foto")
        var photoUrl: String? = null

        @SerializedName("prioridad")
        var priority: Int = 0

        @SerializedName("ultimaChecada")
        var ultimaChecada: String? = null

        @SerializedName("tipoChecada")
        var tipoChecada: String? = null

        @SerializedName("idioma")
        var idioma: String? = null
    }

    @SerializedName("beacons")
    var bcn: Beacons? = null

    class Beacons {
        @SerializedName("beacon")
        var bc: Beacon? = null

        class Beacon {
            @SerializedName("id")
            var id = String()
        }
    }
}