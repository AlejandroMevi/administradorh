package com.venturessoft.human.models.request

data class ConsultaUsuarioRequest(
    var idUsuario: Long,
    var pagina: Int = 1,
    var numRegistros: Int = 200
)



