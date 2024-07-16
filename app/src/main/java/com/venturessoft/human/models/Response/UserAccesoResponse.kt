package com.venturessoft.human.models.response

class UserAccesoResponse(
    var token: String = "",
    var error: Boolean = false,
    var codigo: String = "",
    var acceso: UserAccessResponse = UserAccessResponse()
)