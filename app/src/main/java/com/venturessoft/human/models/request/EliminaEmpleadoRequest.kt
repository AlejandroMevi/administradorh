package com.venturessoft.human.models.request

data class EliminaEmpleadoRequest(
    var idCia: Long,
    var idEmpleado: Long,
    var idUsuario: Long
)
