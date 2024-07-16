package com.venturessoft.human.models.request

data class ActualizarPasswordRequest(
    var email: String,
    var password: String,
    var passwordNuevo: String
)