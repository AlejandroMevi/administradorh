package com.venturessoft.human.models.request

data class ActualizaEmpleadoRequest(
    var nombre: String? = null,
    var apellidoPat: String? = null,
    var apellidoMat: String? = null,
    var fechaIngreso: String? = null,
    var idEstacion: ArrayList<Int>? = null,
    var foto: String? = null,
    var estatus: String? = null,
    var idEstacionEliminar: ArrayList<Int>? = null,
    var idZona: String? = null,
    var correo :String?=null
)
