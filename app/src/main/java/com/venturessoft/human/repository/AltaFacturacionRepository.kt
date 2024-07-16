package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.AltaFacturacionRequest
import com.venturessoft.human.models.response.AltaFacturacionResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import com.venturessoft.human.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AltaFacturacionRepository : EventFirebaseCatch() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!.create(ApiService::class.java)
    fun getAltaFacturacion(altaFacturacionRequest: AltaFacturacionRequest, responseLiveData: MutableLiveData<AltaFacturacionResponse?>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {

        isLoading.postValue(true)
        retrofitClient.getAltaFacturacion(altaFacturacionRequest, User.supertoken).enqueue(
            object : Callback<AltaFacturacionResponse> {

                override fun onFailure(call: Call<AltaFacturacionResponse>?, t: Throwable?) {
                    sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_FACTURACION)
                    isLoading.postValue(false)
                    codeError.postValue(600)
                }

                override fun onResponse(call: Call<AltaFacturacionResponse>?, response: Response<AltaFacturacionResponse>?) {
                    if (response!!.isSuccessful) {
                        isLoading.postValue(false)
                        responseLiveData.postValue(response.body())
                        response.body().codigo
                    }else{
                        validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_FACTURACION)
                        codeError.postValue(response.code())
                    }
                }
            }
        )
    }
}