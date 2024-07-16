package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.BuscarEmpleadoVinRequest
import com.venturessoft.human.models.response.BuscarEmpleadoVinResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import com.venturessoft.human.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class BuscarEmpVinculadoRepository : ValidateAndRefreshJWTRepository() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!.create(ApiService::class.java)
    fun getBusquedaEmpleadoVinculado(buscarEmpleadoVinRequest: BuscarEmpleadoVinRequest, responseLiveData: MutableLiveData<BuscarEmpleadoVinResponse>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {

        isLoading.postValue(true)

        validateAndRefreshJWT(callBackJWTValidateAndRefresh = object : CallBackJWTValidateAndRefresh {

            override fun onSuccess() {
                retrofitClient.getBuscarEmpVinculado(buscarEmpleadoVinRequest.ciaNom,
                buscarEmpleadoVinRequest.numEmp, buscarEmpleadoVinRequest.fechaFoto, User.token).enqueue(

                    object : Callback<BuscarEmpleadoVinResponse> {
                        override fun onFailure(call: Call<BuscarEmpleadoVinResponse>?, t: Throwable?) {
                            sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_ADMINISTRAR_COMPRA)
                            isLoading.postValue(false)
                            codeError.postValue(600)
                        }

                        override fun onResponse(call: Call<BuscarEmpleadoVinResponse>?, response: Response<BuscarEmpleadoVinResponse>?) {
                            if (response!!.isSuccessful) {
                                isLoading.postValue(false)
                                responseLiveData.postValue(response.body())
                                response.body().codigo
                            } else if(response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                                getBusquedaEmpleadoVinculado(buscarEmpleadoVinRequest, responseLiveData, isLoading, codeError)
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