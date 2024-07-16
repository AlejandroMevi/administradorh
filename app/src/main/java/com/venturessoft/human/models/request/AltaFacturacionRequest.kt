package com.venturessoft.human.models.request

data class AltaFacturacionRequest(
    var idCia: Long,
    var clavePais: String,
    var razonSocial: String,
    var rfc: String,
    var codigoTelefono: Int? = 0,
    var telefono: Long? = 0,
    var estado: String,
    var ciudad: String,
    var colonia: String,
    var numeroExt: Int,
    var numeroInt: Int? = 0,
    var calle: String,
    var correo: String
)

