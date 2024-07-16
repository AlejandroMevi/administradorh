package com.venturessoft.human.models.response

data class sEstacion(
    var idEstacion: Int,
    var idZona: Int,
    var clvUnidad: String,
    var nombre: String,
    var latitud: Double,
    var longitud: Double,
    var rango: Float,
    var uuid: String?,
    var bssid: String?,
    var estacionLibre: Boolean,
    var status: String
)



