package com.venturessoft.human.repository
import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.models.request.BusquedaZonaRequest
import com.venturessoft.human.models.response.BusquedaZonaAdmiResponse
import com.venturessoft.human.services.ApiService
import com.venturessoft.human.services.RetrofitClient
import com.venturessoft.human.utils.TypeServices
import com.venturessoft.human.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class BusquedaZonaAdmiRepository : ValidateAndRefreshJWTRepository() {

    var retrofitClient: ApiService = RetrofitClient.getRetrofitInstance(TypeServices.MICROSERVICIOS)!!.create(ApiService::class.java)
    fun getConsultaZonasAdmin(busquedaZonaRequest: BusquedaZonaRequest, responseLiveData: MutableLiveData<BusquedaZonaAdmiResponse>, isLoading: MutableLiveData<Boolean>, codeError: MutableLiveData<Int>) {

        isLoading.postValue(true)

        validateAndRefreshJWT(callBackJWTValidateAndRefresh = object : CallBackJWTValidateAndRefresh {

            override fun onSuccess() {
                retrofitClient.getBusquedaZonasAdmi(busquedaZonaRequest, User.token).enqueue(
                    object : Callback<BusquedaZonaAdmiResponse> {
                        override fun onFailure(call: Call<BusquedaZonaAdmiResponse>?, t: Throwable?) {
                            sendCustomEventCatch(t, EventVariableFirebase.EVENT_WS_ADMDES_FAILURE_ALTA_ADMINISTRAR_COMPRA)
                            isLoading.postValue(false)
                            codeError.postValue(600)
                        }

                        override fun onResponse(call: Call<BusquedaZonaAdmiResponse>?, response: Response<BusquedaZonaAdmiResponse>?) {
                            if (response!!.isSuccessful) {
                                isLoading.postValue(false)
                                responseLiveData.postValue(response.body())
                                response.body().codigo
                            } else if(response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                                getConsultaZonasAdmin(busquedaZonaRequest, responseLiveData, isLoading, codeError)
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