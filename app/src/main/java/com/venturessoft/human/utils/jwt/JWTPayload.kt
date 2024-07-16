package com.venturessoft.human.utils.jwt

import java.util.*
class JWTPayload(val iss: String? = null,
                 val sub: String? = null,
                 val exp: Date? = null,
                 val nbf: Date? = null,
                 val iat: Date? = null,
                 val jti: String? = null,
                 val aud: List<String>? = null )