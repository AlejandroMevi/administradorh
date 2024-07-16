package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.models.Response.DetailsFreeStationResponse
import com.venturessoft.human.models.Response.FreeStationResponse
import com.venturessoft.human.models.request.DetailsEstacionesLibresRequest
import com.venturessoft.human.repository.ReporteStationFreeRepository
import com.venturessoft.human.models.response.EstacionesEmpResponse
import com.venturessoft.human.models.response.ReporteStationFreeRequest
import com.venturessoft.human.models.response.ResponseGeneral

class ReportDetailsStationFreeFragmentViewModel {

    var reporteDetailsStationFree = MutableLiveData<FreeStationResponse>()
    var dataDetailsFreeStation = MutableLiveData<DetailsFreeStationResponse>()

    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()
    fun getReportStationFreeService(reporteStationFreeRequest: ReporteStationFreeRequest) {
        isLoading.postValue(true)
        ReporteStationFreeRepository().getReporteStationFree(reporteStationFreeRequest, reporteDetailsStationFree, isLoading, codeError)
    }
    fun getDetailsStationFreeService(detailsEstacionesLibresRequest: DetailsEstacionesLibresRequest) {
        isLoading.postValue(true)
        ReporteStationFreeRepository().getDetailsStationFree(detailsEstacionesLibresRequest, dataDetailsFreeStation, isLoading, codeError)
    }
}