package com.venturessoft.human.models.request

data class AltaUsuarioAdminRequest (
    var nombre:String = "",
    var cuentaPrueba:Boolean = true,
    var codigoTel :String = "",
    var uuid:String = "",
    var email:String = "",
    var apellidoMat:String = "",
    var telefono:String = "",
    var apellidoPat:String = "",
)
