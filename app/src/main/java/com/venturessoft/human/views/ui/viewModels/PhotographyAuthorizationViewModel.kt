package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.models.Response.DetailsFreeStationResponse
import com.venturessoft.human.models.Response.FreeStationResponse
import com.venturessoft.human.models.Response.ListPhotoAuthResponse
import com.venturessoft.human.models.Response.PhotoAuthResponse
import com.venturessoft.human.models.request.DetailsEstacionesLibresRequest
import com.venturessoft.human.models.request.ListPhotoAuthRequest
import com.venturessoft.human.models.request.PhotoAuthRequest
import com.venturessoft.human.models.response.ReporteStationFreeRequest
import com.venturessoft.human.repository.PhotographyAuthorizationRepository

class PhotographyAuthorizationViewModel {

    var listPhotoData = MutableLiveData<ListPhotoAuthResponse>()
    var authorizationPhoto = MutableLiveData<PhotoAuthResponse>()

    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    fun getListPhotoService(listPhotoAuthRequest : ListPhotoAuthRequest) {
        isLoading.postValue(true)
        PhotographyAuthorizationRepository().getListPhoto(listPhotoAuthRequest, listPhotoData, isLoading, codeError)
    }
    fun authorizationPhotoService(photoAuthRequest: PhotoAuthRequest) {
        isLoading.postValue(true)
        PhotographyAuthorizationRepository().authorizationPhoto(photoAuthRequest, authorizationPhoto, isLoading, codeError)
    }
}