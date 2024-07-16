package com.venturessoft.human.models.response

data class sCiaItemConsulta(
    var clavePais:String,
    var idCompania:Int,
    var nombreCia: String,
    var claveCia: String,
    var claveIdioma: String,
    var maximoEmpleados: Int,
    var estatus: String,
    var rfc: String,
    var razonSocial:String
)