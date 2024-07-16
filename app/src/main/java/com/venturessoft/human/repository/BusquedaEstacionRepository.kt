package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.BusquedaEstacionRequest
import com.venturessoft.human.models.response.BusquedaEstacionResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import com.venturessoft.human.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class BusquedaEstacionRepository : ValidateAndRefreshJWTRepository() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!.create(ApiService::class.java)
    fun getBusquedaEstacion(busquedaEstacionRequest: BusquedaEstacionRequest, responseLiveData: MutableLiveData<BusquedaEstacionResponse>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {

        isLoading.postValue(true)

        validateAndRefreshJWT(callBackJWTValidateAndRefresh = object : CallBackJWTValidateAndRefresh {

            override fun onSuccess() {

                retrofitClient.getBusquedaEstacion(busquedaEstacionRequest, User.token).enqueue(

                    object : Callback<BusquedaEstacionResponse> {
                        override fun onFailure(call: Call<BusquedaEstacionResponse>?, t: Throwable?) {
                            sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_ADMINISTRAR_COMPRA)
                            isLoading.postValue(false)
                            codeError.postValue(600)
                        }

                        override fun onResponse(call: Call<BusquedaEstacionResponse>?, response: Response<BusquedaEstacionResponse>?) {
                            if (response!!.isSuccessful) {
                                isLoading.postValue(false)
                                responseLiveData.postValue(response.body())
                                response.body().codigo
                            } else if(response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                                getBusquedaEstacion(busquedaEstacionRequest, responseLiveData, isLoading, codeError)
                            }
                            else {
                                validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_ADMINISTRAR_COMPRA)
                                codeError.postValue(response.code())
                            }
                        }
                    }
                )
            }

            override fun onFailure(statusCode: Int) {
                isLoading.postValue(false)
                codeError.postValue(statusCode)
            }
        })
    }
}