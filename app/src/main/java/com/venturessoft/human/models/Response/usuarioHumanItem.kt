package com.venturessoft.human.models.response

class usuarioHumanItem {
    var cia: Long = 0
    var scia: List<sCia>? = null
    var cveCia: String = ""
    var nombre: String = ""
    var usuario: String = ""
    var tiempoBloqueoInicial: Int? = null
    var intentoBloqueoInicial: Int? = null
    var tiempoBloqueoReconocimiento: Int? = null
    var intentoBloqueoReconocimiento: Int? = null
    var enrolador: Boolean? = false
    var usuarioExterno: Boolean? = false
    var foto: String? = ""
    var statusFoto: Boolean? = false
    var numeroEmpleado: Long = 0
    var estaciones: Boolean? = false
}