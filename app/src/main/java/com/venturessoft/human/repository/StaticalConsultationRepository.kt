package com.venturessoft.human.Repository

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.response.EmpleadosPayResponse
import com.venturessoft.human.models.request.ConsultaEstadisticaRequest
import com.venturessoft.human.models.response.ConsultaEstadisticaResponse
import com.venturessoft.human.repository.CallBackJWTValidateAndRefresh
import com.venturessoft.human.repository.ValidateAndRefreshJWTRepository
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import com.venturessoft.human.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class StaticalConsultationRepository : ValidateAndRefreshJWTRepository() {
    var retrofitClient = RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!
        .create(ApiService::class.java)

    fun getStaticalConsultation(
        staticalConsultationRequest: ConsultaEstadisticaRequest,
        responseLiveData: MutableLiveData<ConsultaEstadisticaResponse>,
        isLoading: MutableLiveData<Boolean>,
        codeError: MutableLiveData<Int>
    ) {
        isLoading.postValue(true)

        validateAndRefreshJWT(callBackJWTValidateAndRefresh = object :
            CallBackJWTValidateAndRefresh {
            override fun onSuccess() {
                retrofitClient.getStatisticalConsultation(staticalConsultationRequest, User.token).enqueue(

                        object : Callback<ConsultaEstadisticaResponse> {
                            override fun onFailure(
                                call: Call<ConsultaEstadisticaResponse>?,
                                t: Throwable?
                            ) {
                                //   println("El servicio no esta disponible AltaCompañia: " + t!!.cause)
                                sendCustomEventCatch(
                                    t,
                                    EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_ADMINISTRAR_COMPRA
                                )
                                isLoading.postValue(false)
                                codeError.postValue(600)

                            }

                            override fun onResponse(
                                call: Call<ConsultaEstadisticaResponse>?,
                                response: Response<ConsultaEstadisticaResponse>?
                            ) {
                                if (response!!.isSuccessful) {
                                    isLoading.postValue(false)
                                    responseLiveData.postValue(response.body())
                                    response.body().codigo
                                } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                                    getStaticalConsultation(
                                        staticalConsultationRequest,
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

    fun getSaldo(
        idCompania: Long,
        responseLiveData: MutableLiveData<EmpleadosPayResponse>,
        isLoading: MutableLiveData<Boolean>,
        codeError: MutableLiveData<Int>
    ) {
        isLoading.postValue(true)

        validateAndRefreshJWT(callBackJWTValidateAndRefresh = object :
            CallBackJWTValidateAndRefresh {
            override fun onSuccess() {
                retrofitClient.getSaldo(User.token, idCompania).enqueue(

                    object : Callback<EmpleadosPayResponse> {
                        override fun onFailure(call: Call<EmpleadosPayResponse>?, t: Throwable?) {
                            //   println("El servicio no esta disponible AltaCompañia: " + t!!.cause)
                            sendCustomEventCatch(
                                t,
                                EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_ADMINISTRAR_COMPRA
                            )
                            isLoading.postValue(false)
                            codeError.postValue(600)

                        }

                        override fun onResponse(
                            call: Call<EmpleadosPayResponse>?,
                            response: Response<EmpleadosPayResponse>?
                        ) {
                            if (response!!.isSuccessful) {
                                isLoading.postValue(false)
                                responseLiveData.postValue(response.body())
                                response.body().codigo
                            } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                                getSaldo(idCompania, responseLiveData, isLoading, codeError)
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