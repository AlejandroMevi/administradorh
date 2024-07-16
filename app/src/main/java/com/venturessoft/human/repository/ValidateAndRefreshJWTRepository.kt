package com.venturessoft.human.repository

import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.response.UserAccesoResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.jwt.JWTUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class ValidateAndRefreshJWTRepository : EventFirebaseCatch()  {
    fun validateAndRefreshJWT(callBackJWTValidateAndRefresh: CallBackJWTValidateAndRefresh){
        val jwtUtils = JWTUtils(User.token)
        if (jwtUtils.isExpired(0)) {
            this.consumerWSAccessUser(callBackJWTValidateAndRefresh = object : CallBackJWTValidateAndRefresh {
                override fun onSuccess() {
                    callBackJWTValidateAndRefresh.onSuccess()
                }
                override fun onFailure(statusCode: Int) {
                    callBackJWTValidateAndRefresh.onFailure(statusCode)
                }
            })
        } else {
            callBackJWTValidateAndRefresh.onSuccess()
        }
    }

    private fun consumerWSAccessUser(callBackJWTValidateAndRefresh: CallBackJWTValidateAndRefresh){
        RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!.create(ApiService::class.java).getUserAccess(User.email, User.pass).enqueue(
            object : Callback<UserAccesoResponse> {

                override fun onFailure(call: Call<UserAccesoResponse>, t: Throwable) {
                    sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ACCESO_USUARIO)
                    callBackJWTValidateAndRefresh.onFailure(600)
                }

                override fun onResponse(call: Call<UserAccesoResponse>, response: Response<UserAccesoResponse>) {
                    if (response.isSuccessful) {
                        User.token = response.body().token
                        callBackJWTValidateAndRefresh.onSuccess()
                    } else {
                        validateHTTPStatusCode500(response.code(), EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ACCESO_USUARIO)
                        callBackJWTValidateAndRefresh.onFailure(response.code())
                    }
                }
            }
        )
    }
}

interface CallBackJWTValidateAndRefresh {
    fun onSuccess()
    fun onFailure(statusCode: Int)
}