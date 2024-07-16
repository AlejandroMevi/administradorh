package com.venturessoft.human.models

import java.io.Serializable

class User(
    var name: String = "",
    var employeeId: String = "",
    var company: String = "",
    var isAdmin: Boolean = false,
    var isExternal: Boolean = false,
    var nip: String? = "",
    var photoUrl: String? = null, // url of the image on Human's servers
    var photoLocalUri: String? = null, // uri of the image on the phone (used for calculating the hash sent to our Face API)
    var photoHash: String? = null, // hash of the original photo before any downscaling
    var priority: Int = 0,
    var ultimotipo: String? = null,
    var ultimachecada: String? = null,// ultimaChecada
    var tipochecada: String? = null//tipoChecada
) : Serializable