package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.ConsultaTokeNoLoginRequest
import com.venturessoft.human.models.response.ConsultaTokeNoLoginResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import com.venturessoft.human.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultaTokenNoLoginRepository  : EventFirebaseCatch() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!.create(ApiService::class.java)
    fun getConsultaToken(consultaTokeNoLoginRequest: ConsultaTokeNoLoginRequest, responseLiveData: MutableLiveData<ConsultaTokeNoLoginResponse?>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {
        isLoading.postValue(true)
        retrofitClient.getConsultaTokenNoLogin(consultaTokeNoLoginRequest, "Basic SHVtYW46VCEyZURrVHdYNE1BaHNuWlNCZnBwWCpWelZ1a05T").enqueue(object : Callback<ConsultaTokeNoLoginResponse> {

            override fun onFailure(call: Call<ConsultaTokeNoLoginResponse>?, t: Throwable?) {
                sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_USUARIO_ADMIN)
                isLoading.postValue(false)
                codeError.postValue(600)
            }

            override fun onResponse(call: Call<ConsultaTokeNoLoginResponse>?, response: Response<ConsultaTokeNoLoginResponse>?) {
                if (response!!.isSuccessful) {
                    isLoading.postValue(false)
                    responseLiveData.postValue(response.body())
                    response.body()
                    User.supertoken = response.body().token
                } else {
                    validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_USUARIO_ADMIN)
                    codeError.postValue(response.code())
                }
            }
        })
    }
}