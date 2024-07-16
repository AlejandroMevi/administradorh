package com.venturessoft.human.models

import com.google.gson.annotations.SerializedName
open class BaseResponse {
    @SerializedName("error")
    var error: Boolean = true

    @SerializedName("resultado")
    var result: String = "Error"
}