package com.venturessoft.human.models

import com.google.gson.annotations.SerializedName

open class FaceDetectResponse {
    @SerializedName("res")
    var detectResult: Boolean = false
}