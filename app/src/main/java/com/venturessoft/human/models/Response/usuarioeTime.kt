package com.venturessoft.human.models.response
class usuarioeTime {
    var idUsuario: Long? = null
    var nombreCompleto: String = ""
    var apellidoPat: String = ""
    var apellidoMat: String = ""
    var codigoTel: String = ""
    var telefono: String = ""
    var foto: String = ""
    var nombre: String = ""
    var fechaVigencia: String = ""
    var fechaActual: String = ""
    var cuentaPrueba: Boolean = false
    var expiracion: Boolean = false
    var costoEmpleado: Double = 0.0
    var tipoUsuario: Int = 0
    var scia: ArrayList<sCia>? = null
    var szona: ArrayList<sZonas>? = null

    //var aviso: Boolean? = false
    var estaciones: Boolean? = false
    var urlAviso: String? = ""
}