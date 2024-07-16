package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.EnrolarRequest
import com.venturessoft.human.models.response.EnrolarResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CambiarImagenVinculadoRepository : EventFirebaseCatch() {

    var retrofitClient = RetrofitClient.getRetrofitInstance(TypeServices.IMAGEN)!!.create(ApiService::class.java)

    fun getCambiarImagen(enrolarRequest: EnrolarRequest, responseLiveData: MutableLiveData<EnrolarResponse>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {

        retrofitClient.getCambiarImagen(enrolarRequest).enqueue(

            object : Callback<EnrolarResponse> {

                override fun onFailure(call: Call<EnrolarResponse>?, t: Throwable?) {
                    sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ACTUALIZA_EMPLEADO_HU)
                    isLoading.postValue(false)
                    codeError.postValue(600)
                }

                override fun onResponse(call: Call<EnrolarResponse>?, response: Response<EnrolarResponse>?) {
                    if (response!!.isSuccessful) {
                        isLoading.postValue(false)
                        responseLiveData.postValue(response.body())
                        response.body().resultado
                    }else{
                        validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ACTUALIZA_EMPLEADO_HU)
                        codeError.postValue(response.code())
                    }
                }
            }
        )
    }
}