package com.venturessoft.human.models.request

data class ActualizaZonaRequest (
    var idUsuario:Long,
    var idZona:Int,
    var descripcion:String?=null,
    var estatus:String? = null
)


