package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.AltaUsuarioAdminRequest
import com.venturessoft.human.models.request.EliminarUsuario
import com.venturessoft.human.models.response.AltaUsuarioAdminResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import com.venturessoft.human.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AltaUsuarioAdminRepository : EventFirebaseCatch() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!.create(ApiService::class.java)
    fun getAltaUsuarioAdmin(altaUsuarioAdminRequest: AltaUsuarioAdminRequest, responseLiveData: MutableLiveData<AltaUsuarioAdminResponse?>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {
        isLoading.postValue(true)
        retrofitClient.getAltaUsuarioAdmin(altaUsuarioAdminRequest, User.supertoken).enqueue(
            object : Callback<AltaUsuarioAdminResponse> {

                override fun onFailure(call: Call<AltaUsuarioAdminResponse>?, t: Throwable?) {
                    sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_USUARIO_ADMIN)
                    isLoading.postValue(false)
                    codeError.postValue(600)
                }

                override fun onResponse(call: Call<AltaUsuarioAdminResponse>?, response: Response<AltaUsuarioAdminResponse>?) {
                    if (response!!.isSuccessful) {
                        isLoading.postValue(false)
                        responseLiveData.postValue(response.body())
                        response.body().codigo
                    } else {
                        validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_USUARIO_ADMIN)
                        codeError.postValue(response.code())
                    }
                }
            }
        )
    }

    fun getEliminarUsuario(eliminarUsuario: EliminarUsuario, responseLiveData: MutableLiveData<AltaUsuarioAdminResponse?>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {
        isLoading.postValue(true)
        retrofitClient.getDeleteUser(eliminarUsuario, User.supertoken).enqueue(
            object : Callback<AltaUsuarioAdminResponse> {

                override fun onFailure(call: Call<AltaUsuarioAdminResponse>?, t: Throwable?) {
                    sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_USUARIO_ADMIN)
                    isLoading.postValue(false)
                    codeError.postValue(600)
                }

                override fun onResponse(call: Call<AltaUsuarioAdminResponse>?, response: Response<AltaUsuarioAdminResponse>?) {
                    if (response!!.isSuccessful) {
                        isLoading.postValue(false)
                        responseLiveData.postValue(response.body())
                        response.body().codigo
                    } else {
                        validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_USUARIO_ADMIN)
                        codeError.postValue(response.code())
                    }
                }
            }
        )
    }
}