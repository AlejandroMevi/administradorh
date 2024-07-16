package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.GetAvisoPrivacidadRequest
import com.venturessoft.human.models.response.GetAvisoPrivacidadResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetAvisoRepository : EventFirebaseCatch() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.REPORTE_VINCULADO)!!.create(ApiService::class.java)
    fun getAviso(getAvisoPrivacidadRequest: GetAvisoPrivacidadRequest, responseLiveData: MutableLiveData<GetAvisoPrivacidadResponse>, isLoading: MutableLiveData<Boolean>, codeError : MutableLiveData<Int>) {

        isLoading.postValue(true)

        retrofitClient.getAvisoPrivacidad(getAvisoPrivacidadRequest).enqueue(object : Callback<GetAvisoPrivacidadResponse> {

            override fun onFailure(call: Call<GetAvisoPrivacidadResponse>?, t: Throwable?) {
                sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_GET_AVISO)
                isLoading.postValue(false)
                codeError.postValue(600)
            }

            override fun onResponse(call: Call<GetAvisoPrivacidadResponse>?, response: Response<GetAvisoPrivacidadResponse>?) {
                if (response!!.isSuccessful) {
                    isLoading.postValue(false)
                    responseLiveData.postValue(response.body())
                    response.body().codigo
                }else{
                    validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_GET_AVISO)
                    codeError.postValue(response.code())
                }
            }
        })
    }
}