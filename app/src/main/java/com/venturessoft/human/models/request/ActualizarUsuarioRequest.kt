package com.venturessoft.human.models.request

data class ActualizarUsuarioRequest(
    var idUsuario: Long,
    var nip: String? = null,
    var email: String? = null,
    var nombre: String? = null,
    var apellidoPat: String? = null,
    var apellidoMat: String? = null,
    var telefono: Long? = null,
    var codigoTel: String? = null,
    var estatus: String? = null,
    var idZona: ArrayList<Int>? = null,
    var idZonasEliminar: ArrayList<Int>? = null,
    var aviso: Boolean? = null,
    var estaciones: Boolean? = null,
    var token: String? = null,
)
