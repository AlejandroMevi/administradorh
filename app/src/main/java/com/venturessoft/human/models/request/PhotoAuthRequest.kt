package com.venturessoft.human.models.request

data class PhotoAuthRequest(
    var idCia: Long,
    var idEmpleado: Long,
    var autoriza: String
)
