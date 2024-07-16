package com.venturessoft.human.models.request

data class ConsultaCompaniaRequest(
    var idUsuario: Long,
    var idCompania: Long? = null
)