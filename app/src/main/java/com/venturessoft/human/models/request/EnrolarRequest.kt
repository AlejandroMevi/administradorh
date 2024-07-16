package com.venturessoft.human.models.request

data class EnrolarRequest(
    var cia: String,
    var empleado: String,
    var imagen: String,
    var idioma: String?
)
