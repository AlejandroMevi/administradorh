package com.venturessoft.human.models.response

class EmpleadoItems {
    var idCia: Int = 0
    var idEmpleado: Long = 0
    var nombre: String = ""
    var apellidoPat: String = ""
    var apellidoMat: String? = null
    var fechaIngreso: String = ""
    var idEstacion: ArrayList<Int>? = null
    var idZona: ArrayList<Int>? = null
    var foto: String? = null
    var estatus: String = ""
    var correo: String = ""
    var descripcionZona: String = ""
    var urlAviso: String? = null
    var aviso: Boolean? = null
}
