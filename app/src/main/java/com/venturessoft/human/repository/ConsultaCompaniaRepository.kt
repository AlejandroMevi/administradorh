package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.ConsultaCompaniaRequest
import com.venturessoft.human.models.response.ConsultaCompaniaResponse
import com.venturessoft.human.models.response.UserAccesoResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import com.venturessoft.human.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultaCompaniaRepository : EventFirebaseCatch() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!.create(ApiService::class.java)
    fun getConsultaCompania(consultaCompaniaRequest: ConsultaCompaniaRequest, responseLiveData: MutableLiveData<ConsultaCompaniaResponse>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {

        isLoading.postValue(true)

        val userAccesoResponse = UserAccesoResponse()

        retrofitClient.getConsultaCompañia(User.token, User.idCompani, User.idUsuario).enqueue(object : Callback<ConsultaCompaniaResponse> {

            override fun onFailure(call: Call<ConsultaCompaniaResponse>?, t: Throwable?) {
                sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_CONSULTA_COMPANIA)
                isLoading.postValue(false)
                codeError.postValue(600)
            }

            override fun onResponse(call: Call<ConsultaCompaniaResponse>?, response: Response<ConsultaCompaniaResponse>?) {
                if (response!!.isSuccessful) {
                    isLoading.postValue(false)
                    responseLiveData.postValue(response.body())
                    response.body().codigo
                }else{
                    validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_CONSULTA_COMPANIA)
                    codeError.postValue(response.code())
                }
            }
        })
    }
}