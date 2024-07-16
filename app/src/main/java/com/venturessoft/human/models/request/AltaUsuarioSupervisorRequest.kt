package com.venturessoft.human.models.request

data class AltaUsuarioSupervisorRequest(
    var claveCia: String,
    var email: String,
    var nombre: String,
    var apellidoPat: String,
    var apellidoMat: String? = null,
    var zonas: ArrayList<Int>? = null,
    var estaciones: Boolean
)