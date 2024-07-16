package com.venturessoft.human.services

import com.venturessoft.human.utils.TypeServices
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    companion object {
        private const val TIME: Long = 120

        private var retrofit: Retrofit? = null
        private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        private var okHttpClient = OkHttpClient.Builder()
                .connectTimeout(TIME, TimeUnit.SECONDS)
                .readTimeout(TIME, TimeUnit.SECONDS)
                .writeTimeout(TIME, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()
        fun getRetrofitInstance(tipo: TypeServices): Retrofit? {

            if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP || android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP_MR1){

                okHttpClient = UnsafeClient.getUnsafeOkHttpClient()
                        .connectTimeout(TIME, TimeUnit.SECONDS)
                        .readTimeout(TIME, TimeUnit.SECONDS)
                        .writeTimeout(TIME, TimeUnit.SECONDS)
                        .build()
            }

            var URLTypeServices = ""
            when(tipo) {
                TypeServices.DEFAULT -> {
                    URLTypeServices = URL.URL_BASE
                }
                TypeServices.UNIFICADO -> {
                    URLTypeServices = URL.URL_BASE_UNIFICADO
                }
                TypeServices.REPORTE_VINCULADO -> {
                    URLTypeServices = URL.URL_BASE_REPORTE_VIN
                }
                TypeServices.IMAGEN ->{
                    URLTypeServices = URL.URL_BASE_IMAGEN
                }
                TypeServices.MICROSERVICIOS ->{
                    URLTypeServices = URL.URL_MICROSERVICIOS
                }
            }

            retrofit = Retrofit.Builder()
                .baseUrl(URLTypeServices)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit
        }
    }
}