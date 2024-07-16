package com.venturessoft.human.models.response

class ConsultaUsuarioResponse {
    var codigo: String = ""
    var idUsuario: Int = 0
    var nombre: String = ""
    var apellidoPat: String = ""
    var apellidoMat: String? = null
    var codigoTel: String? = null
    var telefono: String? = null
    var totalRegistros: Int = 0
    var supervisor: ArrayList<SupervisorResponse>? = null
}



