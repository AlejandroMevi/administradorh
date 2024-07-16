package com.venturessoft.human.models.request

data class ListaAuditoriaRequest(
    var fechaFin: String,
    var fechaInicio: String,
    var numCia: Long,
    var idUsuario: Long,
    var numEmp: Long?
)
