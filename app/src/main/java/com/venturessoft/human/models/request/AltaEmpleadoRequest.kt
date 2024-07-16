package com.venturessoft.human.models.request

data class AltaEmpleadoRequest(
    var idCia: Long,
    var idEmpleado:Long,
    var nombre: String,
    var apellidoPat: String,
    var apellidoMat: String?= null,
    var fechaIngreso: String,
    var idEstacion: Any? = null,
    var foto: String? = null,
    var idZona: String,
    var correo: String? = null,
    )
