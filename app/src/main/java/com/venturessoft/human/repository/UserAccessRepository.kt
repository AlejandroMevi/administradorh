package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.UserAccessRequest
import com.venturessoft.human.models.response.UserAccesoResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAccessRepository : EventFirebaseCatch() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!.create(ApiService::class.java)
    fun getUserAccess(userAccessRequest: UserAccessRequest, responseLiveData: MutableLiveData<UserAccesoResponse>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {

        isLoading.postValue(true)

        retrofitClient.getUserAccess(userAccessRequest.email, userAccessRequest.password).enqueue(
            object : Callback<UserAccesoResponse> {

                override fun onFailure(call: Call<UserAccesoResponse>, t: Throwable) {
                    sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ACCESO_USUARIO)
                    isLoading.postValue(false)
                    codeError.postValue(600)
                }

                override fun onResponse(call: Call<UserAccesoResponse>, response: Response<UserAccesoResponse>) {
                    isLoading.postValue(false)
                    if (response.isSuccessful) {
                        responseLiveData.postValue(response.body())
                        response.body().codigo
                    } else {
                        validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ACCESO_USUARIO)
                        codeError.postValue(response.code())
                    }
                }
            }
        )
    }
}