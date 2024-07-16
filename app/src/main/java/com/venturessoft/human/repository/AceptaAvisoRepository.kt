package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.AceptaAvisoRequest
import com.venturessoft.human.models.response.AceptarAvisoResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AceptaAvisoRepository : EventFirebaseCatch() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.REPORTE_VINCULADO)!!.create(ApiService::class.java)

    fun getAceptaAviso(aceptaAvisoRequest: AceptaAvisoRequest, responseLiveData: MutableLiveData<AceptarAvisoResponse>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {
        //  FakeInterceptor.IS_ENABLE = true
        isLoading.postValue(true)
        retrofitClient.getAceptaAviso(aceptaAvisoRequest).enqueue(
            object : Callback<AceptarAvisoResponse> {

                override fun onFailure(call: Call<AceptarAvisoResponse>?, t: Throwable?) {
                    sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ACEPTAR_AVISO)
                    isLoading.postValue(false)
                    codeError.postValue(600)
                }

                override fun onResponse(call: Call<AceptarAvisoResponse>?, response: Response<AceptarAvisoResponse>?) {
                    if (response!!.isSuccessful) {
                        isLoading.postValue(false)
                        responseLiveData.postValue(response.body())
                        response.body().codigo
                    } else {
                        validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ACEPTAR_AVISO)
                        codeError.postValue(response.code())
                    }
                }
            }
        )
    }
}