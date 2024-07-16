package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.ReporteChecadasRepository
import com.venturessoft.human.repository.ReporteOpcionRepository
import com.venturessoft.human.models.request.ReporteChecadasRequest
import com.venturessoft.human.models.request.ReporteOpcionRequest
import com.venturessoft.human.models.response.ReporteChecadasResponse
import com.venturessoft.human.models.response.ReporteOpcionResponse

class ReportByEmployeeViewModel {
    var reporteChecadasResponseMutableData = MutableLiveData<ReporteChecadasResponse>()
    var reporteopcionResponseMutableData = MutableLiveData<ReporteOpcionResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    fun getReporteChecadaService(reporteChecadasRequest: ReporteChecadasRequest) {
        isLoading.postValue(true)
        ReporteChecadasRepository().getReporteChecadas(reporteChecadasRequest, reporteChecadasResponseMutableData, isLoading,codeError)
    }

    fun getReporteEFileService(reporteOpcionRequest: ReporteOpcionRequest) {
        isLoading.postValue(true)
        println("Reporte = ReportByEmployeeViewModel")
        ReporteOpcionRepository().getReporteOpcion(reporteOpcionRequest, reporteopcionResponseMutableData, isLoading,codeError)
    }
}