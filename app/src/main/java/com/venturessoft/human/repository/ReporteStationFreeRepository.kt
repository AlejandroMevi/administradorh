package com.venturessoft.human.repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.Response.DetailsFreeStationResponse
import com.venturessoft.human.models.Response.FreeStationResponse
import com.venturessoft.human.models.request.DetailsEstacionesLibresRequest
import com.venturessoft.human.models.response.*
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import com.venturessoft.human.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class ReporteStationFreeRepository : ValidateAndRefreshJWTRepository() {

    var retrofitClient: ApiService =
        RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!
            .create(ApiService::class.java)

    fun getReporteStationFree(
        reporteStationFreeRequest: ReporteStationFreeRequest,
        responseLiveData: MutableLiveData<FreeStationResponse>,
        isLoading: MutableLiveData<Boolean>,
        codeError: MutableLiveData<Int>
    ) {

        isLoading.postValue(true)

        validateAndRefreshJWT(callBackJWTValidateAndRefresh = object :
            CallBackJWTValidateAndRefresh {

            override fun onSuccess() {

                retrofitClient.getReportCheckDetails(reporteStationFreeRequest, User.token).enqueue(

                    object : Callback<FreeStationResponse> {

                        override fun onFailure(call: Call<FreeStationResponse>?, t: Throwable?) {
                            sendCustomEventCatch(
                                t,
                                EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_ADMINISTRAR_COMPRA
                            )
                            isLoading.postValue(false)
                            codeError.postValue(600)
                        }

                        override fun onResponse(
                            call: Call<FreeStationResponse>?,
                            response: Response<FreeStationResponse>?
                        ) {
                            if (response!!.isSuccessful) {
                                isLoading.postValue(false)
                                responseLiveData.postValue(response.body())
                                response.body().codigo
                            } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                                getReporteStationFree(
                                    reporteStationFreeRequest,
                                    responseLiveData,
                                    isLoading,
                                    codeError
                                )
                            } else {
                                validateHTTPStatusCode500(
                                    response.code(),
                                    EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_ADMINISTRAR_COMPRA
                                )
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

    fun getDetailsStationFree(
        detailsEstacionesLibresRequest: DetailsEstacionesLibresRequest,
        responseLiveData: MutableLiveData<DetailsFreeStationResponse>,
        isLoading: MutableLiveData<Boolean>,
        codeError: MutableLiveData<Int>
    ) {

        isLoading.postValue(true)

        validateAndRefreshJWT(callBackJWTValidateAndRefresh = object :
            CallBackJWTValidateAndRefresh {

            override fun onSuccess() {

                retrofitClient.getCheckDetails(detailsEstacionesLibresRequest, User.token).enqueue(

                    object : Callback<DetailsFreeStationResponse> {

                        override fun onFailure(
                            call: Call<DetailsFreeStationResponse>?,
                            t: Throwable?
                        ) {
                            sendCustomEventCatch(
                                t,
                                EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_ADMINISTRAR_COMPRA
                            )
                            isLoading.postValue(false)
                            codeError.postValue(600)
                        }

                        override fun onResponse(
                            call: Call<DetailsFreeStationResponse>?,
                            response: Response<DetailsFreeStationResponse>?
                        ) {
                            if (response!!.isSuccessful) {
                                isLoading.postValue(false)
                                responseLiveData.postValue(response.body())
                                response.body().codigo
                            } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                                getDetailsStationFree(
                                    detailsEstacionesLibresRequest,
                                    responseLiveData,
                                    isLoading,
                                    codeError
                                )
                            } else {
                                validateHTTPStatusCode500(
                                    response.code(),
                                    EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_ADMINISTRAR_COMPRA
                                )
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