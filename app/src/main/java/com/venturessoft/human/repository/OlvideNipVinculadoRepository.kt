package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.models.request.OlvideNipRequest
import com.venturessoft.human.models.response.OlvideNipResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OlvideNipVinculadoRepository : EventFirebaseCatch() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.DEFAULT)!!.create(ApiService::class.java)
    fun getOlvideNip(olvideNipRequest: OlvideNipRequest, responseLiveData: MutableLiveData<OlvideNipResponse>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {
        //sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ACTUALIZAR_FACTURACION)
        //validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ACTUALIZAR_FACTURACION)
        retrofitClient.getOlvideNip(olvideNipRequest).enqueue(object : Callback<OlvideNipResponse> {

            override fun onFailure(call: Call<OlvideNipResponse>?, t: Throwable?) {
                isLoading.postValue(false)
                codeError.postValue(600)
            }

            override fun onResponse(call: Call<OlvideNipResponse>?, response: Response<OlvideNipResponse>?) {
                if (response!!.isSuccessful) {
                    isLoading.postValue(false)
                    responseLiveData.postValue(response.body())
                    response.body().returnOlvideNip!!.resultado
                }else{
                    codeError.postValue(response.code())
                }
            }
        })
    }
}