package com.venturessoft.human.views.ui.fragments.ReportFragment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentReportDetailsStationFreeBinding
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.reportesEstacionesLibres.ui.EstacionesLibresAdapter
import com.venturessoft.human.models.Response.DetailsFreeStationResponse
import com.venturessoft.human.models.Response.EstacionesEmpItem
import com.venturessoft.human.models.request.DetailsEstacionesLibresRequest
import com.venturessoft.human.models.response.EstacionLibreDetalleResponse
import com.venturessoft.human.models.response.EstacionesEmpResponse
import com.venturessoft.human.models.response.ReporteStationFreeRequest
import com.venturessoft.human.models.response.ResponseGeneral
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.Utilities.Companion.observeOnce
import com.venturessoft.human.views.adapters.AdapterListStationFreeDetails
import com.venturessoft.human.views.ui.fragments.Filter.FilterReportFragment
import com.venturessoft.human.views.ui.viewModels.ReportDetailsStationFreeFragmentViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_DATA_INITIAL = "fechaInicio"
private const val ARG_DATA_FINISH = "fechaFin"
private const val ARG_NUM_CIA = "numCia"
private const val ARG_NUM_EMP = "numEmp"
class ReportDetailsStationFree : Fragment(), EstacionesLibresAdapter.OnClickListener {
    private var param1: String? = null
    private var param2: String? = null
    private var dateInitial: String? = null
    private var dateFinish: String? = null
    private var numCia: String? = null
    private var numEmp: Int? = 0
    private var reportDetailsStationFree = ReportDetailsStationFreeFragmentViewModel()
    private var responseDetails: DetailsFreeStationResponse? = null
    private var mListener: ReportFragment.OnFragmentInteractionListener? = null
    private var mainInterface: MainInterface? = null

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    private lateinit var binding: FragmentReportDetailsStationFreeBinding
    private lateinit var listaReportes: ArrayList<EstacionesEmpItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            dateInitial = it.getString(ARG_DATA_INITIAL)
            dateFinish = it.getString(ARG_DATA_FINISH)
            numCia = it.getString(ARG_NUM_CIA)
            numEmp = it.getInt(ARG_NUM_EMP)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportDetailsStationFreeBinding.inflate(inflater, container, false)
        mListener?.onBack("backMenuReportStationFree")
        binding.employeereportreciclerstation.setHasFixedSize(true)
        binding.employeereportreciclerstation.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                requireActivity(),
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )
        binding.employeereportreciclerstation.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
        this.addObservers()
        this.consultReportStationFree()
        this.activeBack()
        return binding.root
    }

    private fun consultReportStationFree() {
        val listEmployee = mutableListOf<Long>()
        listEmployee.clear()
        if (this.numEmp?.toLong().toString() != "0"){
            this.numEmp?.toLong()?.let { listEmployee.add(it) }
        }else{
            listEmployee.clear()
        }
        when (User.ambiente) {
            "HU" -> {
                val reportStationFree = ReporteStationFreeRequest(
                    this.dateInitial.toString(), this.dateFinish,
                    listEmployee, this.numCia.toString(), idUsuario = User.idUsuFree
                )
                reportDetailsStationFree.getReportStationFreeService(reportStationFree)
            }
            "ET" -> {
                val reportStationFree = ReporteStationFreeRequest(
                    this.dateInitial.toString(), this.dateFinish,
                    listEmployee, this.numCia.toString(), idUsuario = User.idUsuario.toString()
                )
                reportDetailsStationFree.getReportStationFreeService(reportStationFree)
            }
        }
    }

    private fun actionItemReportStationFree() {
        listStationFreeDetails.clear()
        for (employee in responseGeneralReport.estacionesEmp!!) {
            for (employeeStation in employee.estacionLibreDetalle!!) {
                employeeStation.idEmployee = employee.numEmp.toString()
                employeeStation.nameEmployee = employee.nombreEmp
                employeeStation.fotoEmployee = employee.foto
                listStationFreeDetails.add(employeeStation)
            }

        }
        binding.employeereportreciclerstation.adapter = AdapterListStationFreeDetails(
            requireActivity(),
            listStationFreeDetails,
            childFragmentManager,
            this
        )
    }

    private fun addObservers() {
        reportDetailsStationFree.reporteDetailsStationFree.observe(
            viewLifecycleOwner
        ) { response ->
            if (response.codigo == "ET000") {
                if (response != null) {
                    if (response.estacionesEmp!!.isNotEmpty()) {
                        val list = java.util.ArrayList<EstacionesEmpItem>()
                        for (i in response.estacionesEmp.indices) {
                            val dataModel = EstacionesEmpItem()
                            dataModel.fechaChecada = response.estacionesEmp[i]?.fechaChecada
                            dataModel.horaChecada = response.estacionesEmp[i]?.horaChecada
                            dataModel.numEmp = response.estacionesEmp[i]?.numEmp
                            dataModel.estacion = response.estacionesEmp[i]?.estacion
                            dataModel.numCia = response.estacionesEmp[i]?.numCia
                            dataModel.nombreEmp = response.estacionesEmp[i]?.nombreEmp
                            dataModel.tipo = response.estacionesEmp[i]?.tipo
                            list.add(dataModel)
                            listaReportes = list
                            setDataList(listaReportes)
                        }
                    }
                }
            } else {
                Utilities.loadMessageError(
                    response.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }

        reportDetailsStationFree.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                this.showLoadingAnimation()
            } else {
                this.hideLoadingAnimation()
            }
        }

        reportDetailsStationFree.codeError.observe(viewLifecycleOwner) {
            this.loadServerMessageError(it)
        }
    }

    private fun setDataList(listaUsuarios: ArrayList<EstacionesEmpItem>) {
        binding.employeereportreciclerstation.adapter =
            EstacionesLibresAdapter(listaUsuarios, this)
    }

    fun showLoadingAnimation() {
        binding.loadingAnimationStationFree.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideLoadingAnimation() {
        binding.loadingAnimationStationFree.root.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = requireActivity().packageName
        val identificadorMensaje =
            this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(requireActivity().getString(identificadorMensaje), requireActivity(),childFragmentManager)
        } else {
            Utilities.showDialog(code.toString(), requireActivity(),childFragmentManager)
        }
    }

    private fun activeBack() {
        binding.btnBackListReportStationFree.setOnClickListener {
            val filterReport = FilterReportFragment()
            val bundle = Bundle()
            bundle.putString("ReportSelect", getString(R.string.reportsmenufragment_item_3))
            filterReport.arguments = bundle
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, filterReport, "WelcomeFragment")
                .commit()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
        mainInterface = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ReportFragment.OnFragmentInteractionListener) {
            mListener = context
        }
        if (context is MainInterface) {
            mainInterface = context
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
                validatePermission()
            }
        }
    }

    private fun validatePermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    companion object {
        lateinit var pagerEmployeeReport: ViewPager
        var responseGeneralReport = ResponseGeneral<ArrayList<EstacionesEmpResponse>>()
        var listStationFreeDetails = ArrayList<EstacionLibreDetalleResponse>()
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportDetailsStationFree().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(estacionesEmpItem: EstacionesEmpItem) {
        detailsServices(estacionesEmpItem)
    }

    private fun detailsServices(estacionesEmpItem: EstacionesEmpItem) {
        reportDetailsStationFree.dataDetailsFreeStation.value = null
        var request : DetailsEstacionesLibresRequest? = null
        when (User.ambiente) {
            "HU" -> {
                 request =
                    DetailsEstacionesLibresRequest(
                        estacionesEmpItem.numCia.toString(),
                        estacionesEmpItem.numEmp!!,
                        User.idUsuFree,
                        estacionesEmpItem.fechaChecada.toString(),
                        estacionesEmpItem.horaChecada.toString()
                    )
            }
            "ET" -> {
                 request =
                    DetailsEstacionesLibresRequest(
                        estacionesEmpItem.numCia.toString(),
                        estacionesEmpItem.numEmp!!,
                        User.idUsuario.toString(),
                        estacionesEmpItem.fechaChecada.toString(),
                        estacionesEmpItem.horaChecada.toString()
                    )
            }
        }

        if (request != null) {
            reportDetailsStationFree.getDetailsStationFreeService(request)
        }
        reportDetailsStationFree.dataDetailsFreeStation.observeOnce(viewLifecycleOwner) { response ->
            if (response != null) {
                responseDetails = response
                loadDetailsFragment(estacionesEmpItem, responseDetails)
            }
        }

    }
    private fun loadDetailsFragment(
        estacionesEmpItem: EstacionesEmpItem,
        r: DetailsFreeStationResponse? = null
    ) {
        val a = r?.detalle?.get(0)?.latitud?.toDouble()
        val b = r?.detalle?.get(0)?.longitud?.toDouble()
        val fullScreenDialogFragment = DetallesEstacionesLibresFragment(estacionesEmpItem, r, a, b)
        fullScreenDialogFragment.show(
            requireActivity().supportFragmentManager,
            "FullScreenDialogFragment"
        )
    }
    private fun clearServiceDetails() {
        reportDetailsStationFree.dataDetailsFreeStation.removeObservers(viewLifecycleOwner)
    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.reportsmenufragment_item_3))
    }
}