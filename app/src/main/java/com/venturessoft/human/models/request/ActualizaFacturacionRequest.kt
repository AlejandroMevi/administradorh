package com.venturessoft.human.models.request

data class ActualizaFacturacionRequest(
    var idCia: Long,
    var claveCia: String? = null,
    var correo: String,
    var clavePais: String,
    var razonSocial: String,
    var rfc: String,
    var codigoTelefono: String? = null,
    var telefono: Long? = null,
    var estatus: String? = null,
    var estado: String,
    var ciudad: String,
    var colonia: String,
    var numeroExt: Int,
    var numeroInt: Int? = null,
    var calle: String
)
