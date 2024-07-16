package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.AltaCompaniaRequest
import com.venturessoft.human.models.response.AltaCompaniaResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import com.venturessoft.human.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AltaCompaniaRepository : EventFirebaseCatch() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!.create(ApiService::class.java)
    fun getAltaCompania(altaCompaniaRequest: AltaCompaniaRequest, responseLiveData: MutableLiveData<AltaCompaniaResponse?>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {
        isLoading.postValue(true)
        retrofitClient.getAltaCompania(altaCompaniaRequest, User.supertoken).enqueue(
            object : Callback<AltaCompaniaResponse> {

                override fun onFailure(call: Call<AltaCompaniaResponse>?, t: Throwable?) {
                    sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_COMPANIA)
                    isLoading.postValue(false)
                    codeError.postValue(600)
                }

                override fun onResponse(call: Call<AltaCompaniaResponse>?, response: Response<AltaCompaniaResponse>?) {
                    if (response!!.isSuccessful) {
                        isLoading.postValue(false)
                        responseLiveData.postValue(response.body())
                        response.body().codigo
                    }else {
                        validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_COMPANIA)
                        codeError.postValue(response.code())
                    }
                }
            }
        )
    }
}