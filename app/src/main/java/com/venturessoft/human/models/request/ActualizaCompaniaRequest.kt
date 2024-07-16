package com.venturessoft.human.models.request
data class ActualizaCompaniaRequest(
    var nombreCia: String,
    var claveCia: String,
    var claveIdioma: String,
    var maximoEmpleados: Int,
    var clavePais: String,
    var razonSocial:String,
    var fotoLocal:String
)
