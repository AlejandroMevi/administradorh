package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.Response.ListPhotoAuthResponse
import com.venturessoft.human.models.Response.PhotoAuthResponse
import com.venturessoft.human.models.request.ListPhotoAuthRequest
import com.venturessoft.human.models.request.PhotoAuthRequest
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import com.venturessoft.human.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class PhotographyAuthorizationRepository : ValidateAndRefreshJWTRepository() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!.create(ApiService::class.java)

    fun getListPhoto(listPhotoAuthRequest : ListPhotoAuthRequest, responseLiveData: MutableLiveData<ListPhotoAuthResponse>,
                     isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {

        isLoading.postValue(true)

        validateAndRefreshJWT(callBackJWTValidateAndRefresh = object : CallBackJWTValidateAndRefresh {

            override fun onSuccess() {

                retrofitClient.getListPhoto(listPhotoAuthRequest.numCia, listPhotoAuthRequest.idUsuario, User.token).enqueue(

                    object : Callback<ListPhotoAuthResponse> {

                        override fun onFailure(call: Call<ListPhotoAuthResponse>?, t: Throwable?) {
                            sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_ADMINISTRAR_COMPRA)
                            isLoading.postValue(false)
                            codeError.postValue(600)
                        }

                        override fun onResponse(
                            call: Call<ListPhotoAuthResponse>?,
                            response: Response<ListPhotoAuthResponse>?
                        ) {
                            if (response!!.isSuccessful) {
                                isLoading.postValue(false)
                                responseLiveData.postValue(response.body())
                                response.body().codigo
                            } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                                getListPhoto(listPhotoAuthRequest, responseLiveData, isLoading, codeError)
                            } else {
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

    fun authorizationPhoto(
        photoAuthRequest: PhotoAuthRequest, responseLiveData: MutableLiveData<PhotoAuthResponse>,
        isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {

        isLoading.postValue(true)

        validateAndRefreshJWT(callBackJWTValidateAndRefresh = object :
            CallBackJWTValidateAndRefresh {

            override fun onSuccess() {

                retrofitClient.authorizationPhoto(photoAuthRequest, User.token).enqueue(

                    object : Callback<PhotoAuthResponse> {

                        override fun onFailure(call: Call<PhotoAuthResponse>?, t: Throwable?) {
                            sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_ADMINISTRAR_COMPRA)
                            isLoading.postValue(false)
                            codeError.postValue(600)
                        }

                        override fun onResponse(
                            call: Call<PhotoAuthResponse>?,
                            response: Response<PhotoAuthResponse>?
                        ) {
                            if (response!!.isSuccessful) {
                                isLoading.postValue(false)
                                responseLiveData.postValue(response.body())
                                response.body().codigo
                            } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                                authorizationPhoto(photoAuthRequest, responseLiveData, isLoading, codeError)
                            } else {
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