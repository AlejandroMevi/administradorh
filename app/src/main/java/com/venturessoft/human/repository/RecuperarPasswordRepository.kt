package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.RecuperarPasswordRequest
import com.venturessoft.human.models.response.RecuperarPasswordResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import com.venturessoft.human.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecuperarPasswordRepository : EventFirebaseCatch() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!.create(ApiService::class.java)
    fun getRecuperarPassword(
        recuperarPasswordRequest: RecuperarPasswordRequest,
        responseLiveData: MutableLiveData<RecuperarPasswordResponse>,
        isLoading: MutableLiveData<Boolean>,
        codeError: MutableLiveData<Int>,
        token: String
    ) {
        isLoading.postValue(true)
        retrofitClient.getRecuperarPassword(recuperarPasswordRequest,token).enqueue(object : Callback<RecuperarPasswordResponse> {
            override fun onFailure(call: Call<RecuperarPasswordResponse>?, t: Throwable?) {
                sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_RECUPERA_PASSWORD)
                isLoading.postValue(false)
                codeError.postValue(600)
            }
            override fun onResponse(call: Call<RecuperarPasswordResponse>?, response: Response<RecuperarPasswordResponse>?) {
                isLoading.postValue(false)
                if (response!!.isSuccessful) {
                    responseLiveData.postValue(response.body())
                }else{
                    validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_RECUPERA_PASSWORD)
                    codeError.postValue(response.code())
                }
            }
        })
    }
}