package com.venturessoft.human.utils


class Constants {
    companion object {
        const val FROM_LOG_OUT = "fromLogOut"
        const val AVISO = "Aviso"
        // in-app request codes
        const val PERMISSION_FINE_LOCATION_REQUEST_CODE = 800
        const val PERMISSION_CAMERA_REQUEST_CODE = 801
        // TIME-OUTS
        const val LOCATION_SEARCH_TIMEOUT = 35000L
        const val SPLASH_TIME_OUT = 1000

        const val PREPRODUCTIVO = "Preproductivo"
        const val DESARROLLO = "Desarrollo"
        const val CONTROLCALIDAD = "ControlCalidad"
        //AnimotionLottie
        const val LOOTIE_ANIMATION = "lottieanimation/lotie_etime.json"
        const val ANIMOTIONLOTTIE_TIMEOUT = 800L
        const val EMAIL_ANIMOTIONLOTTIE_TIMEOUT = 3600L
    }
}
enum class TypeServices {
    DEFAULT,
    UNIFICADO,
    REPORTE_VINCULADO,
    IMAGEN,
    MICROSERVICIOS
}