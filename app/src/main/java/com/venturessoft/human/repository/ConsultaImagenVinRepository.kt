package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.ConsultaImagenVinRequest
import com.venturessoft.human.models.response.ConsultaImagenVinResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultaImagenVinRepository : EventFirebaseCatch() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.IMAGEN)!!.create(ApiService::class.java)

    fun getConsultaImagen(consultarImagenVinRequest: ConsultaImagenVinRequest, responseLiveData: MutableLiveData<ConsultaImagenVinResponse>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>){

        retrofitClient.getConsultarImg(consultarImagenVinRequest).enqueue(
            object : Callback<ConsultaImagenVinResponse> {

                override fun onFailure(call: Call<ConsultaImagenVinResponse>?, t: Throwable?) {
                    sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_CONSULTA_EMPLEADO_HU)
                    isLoading.postValue(false)
                    codeError.postValue(600)
                }

                override fun onResponse(call: Call<ConsultaImagenVinResponse>?, response: Response<ConsultaImagenVinResponse>?) {
                    if (response!!.isSuccessful) {
                        isLoading.postValue(false)
                        responseLiveData.postValue(response.body())
                    }else{
                        validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_CONSULTA_EMPLEADO_HU)
                        codeError.postValue(response.code())
                    }
                }
            }
        )
    }
}