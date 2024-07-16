package com.venturessoft.human.models.request

data class CambiarNipRequest(
    var claveCompania: String,
    var numEmp: Long,
    var campo1: String,
    var campo2: String,
    var nipActual: String,
    var nipNuevo: String,
    var idioma: String? = null
)



