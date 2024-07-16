package com.venturessoft.human.models.request

data class UserAccessRequest(
    var email: String = "",
    var password: String = "",
    var idioma: String = ""
)
