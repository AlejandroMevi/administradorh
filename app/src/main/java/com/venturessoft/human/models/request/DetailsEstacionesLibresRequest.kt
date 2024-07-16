package com.venturessoft.human.models.request

data class DetailsEstacionesLibresRequest(
    var numCia: String,
    var numEmp: Int,
    var idUsuario: String,
    var fechaInicio: String,
    var hora: String
    )
