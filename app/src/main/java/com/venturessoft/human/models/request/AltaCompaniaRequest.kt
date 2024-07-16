package com.venturessoft.human.models.request

data class AltaCompaniaRequest(
    var idUsuario: Int,
    var nombreCia: String,
    var claveCia: String,
    var razonSocial: String,
    var claveIdioma: String,
    var rfc: String,
    var clavePais : String,
    var maximoEmpleados:Int,
    var fotoLocal: String
)


