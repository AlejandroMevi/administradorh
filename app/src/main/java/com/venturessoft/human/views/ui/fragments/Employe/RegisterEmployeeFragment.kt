package com.venturessoft.human.views.ui.fragments.Employe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentRegisterEmployeeBinding
import com.venturessoft.human.databinding.FragmentSelectZonesBinding
import com.venturessoft.human.models.request.AltaEmpleadoRequest
import com.venturessoft.human.models.request.BusquedaZonaRequest
import com.venturessoft.human.models.request.ConsultaEstacionRequest
import com.venturessoft.human.models.response.AltaEmpleadoResponse
import com.venturessoft.human.models.response.BusquedaZonaResponse
import com.venturessoft.human.models.response.ConsultaEstacionResponse
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.adapters.SimpleAdapterEstacionesSelect
import com.venturessoft.human.views.adapters.SimpleAdapterZonesSelectEmployee
import com.venturessoft.human.views.ui.activities.AnimotionLottie
import com.venturessoft.human.views.ui.fragments.CameraAdmin.CameraAdminFragment
import com.venturessoft.human.views.ui.fragments.Stations.SelectedStationFragment
import com.venturessoft.human.views.ui.fragments.Zones.SelectZonesFragment
import com.venturessoft.human.views.ui.viewModels.RegisterEmployeeViewModel
import com.venturessoft.human.views.ui.viewModels.RegisterSupervisorFragmentViewModel

class RegisterEmployeeFragment : androidx.fragment.app.Fragment() {
    private var mListener: OnFragmentInteractionListener? = null
    private var registerEmployeeFragmentViewModel = RegisterEmployeeViewModel()
    private var altaEmpleado = AltaEmpleadoResponse()
    private var consultaEstacionResponse = ConsultaEstacionResponse()
    private var registerSupervisorViewModel = RegisterSupervisorFragmentViewModel()
    private var busquedazonasResponse = BusquedaZonaResponse()
    private var itemsSeleccionadosVacios = IntArray(0)
    lateinit var adapter: SimpleAdapterEstacionesSelect
    private var mainInterface: MainInterface? = null
    lateinit var myContext: Activity
    var foto: String? = null
    private var fotoVacia: String = ""

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    companion object {
        var itemsSeleccionados = ArrayList<Int>()
        var itemsSeleccionadosE = ArrayList<Int>()

        lateinit var imageModelArrayList: ArrayList<SearchModel>
        lateinit var imageModelArrayListEmployee: ArrayList<SearchModel>
        private const val ARG_NAME_EMPLOYEE = "nameEmployee"
        private const val ARG_APPATERNO = "apPaterno"
        private const val ARG_APMATERNO = "apMaterno"
        private const val ARG_NEMPLEADO = "nEmpleado"
        private const val ARG_ARRAYDATA = "arrayData"
        fun newInstance(nameEmployeekt: String?, apPaterno: String?, apMaterno: String?, nEmpleadp: String?, itemsSeleccionados: ArrayList<Int>?): RegisterEmployeeFragment {
            val fragment = RegisterEmployeeFragment()
            val args = Bundle()
            args.putSerializable(ARG_NAME_EMPLOYEE, nameEmployeekt)
            args.putSerializable(ARG_APPATERNO, apPaterno)
            args.putSerializable(ARG_APMATERNO, apMaterno)
            args.putSerializable(ARG_NEMPLEADO, nEmpleadp)
            args.putSerializable(ARG_ARRAYDATA, itemsSeleccionados)
            fragment.arguments = args
            return fragment
        }
        var nEmpleadp: String? = null
        var nameEmploye: String? = null
        var apPaternoString: String? = null
        var apMaternoString: String? = null
        var fotoString: String? = null
        var nameZone : String? = null
        var idEstSelect: ArrayList<Int>? = null
        var idZonSelect: ArrayList<Int>? = null
    }
    private lateinit var binding : FragmentRegisterEmployeeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myContext = requireActivity()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentRegisterEmployeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onBack("backButtonRegisterEmployee")
        addObserver()
        loadServices()
        defaultData()
        loadPhoto()
        backButton()
        resetPersistence()
        saveData()
        listensButtons()
        showTextZones()
        showTextEstaciones()
        binding.estacionesSeleccinadas.setText(textSelect())
        binding.estacionesSeleccinadas.setOnClickListener {
            openDialogSelectStation()
        }
        if (User.scia!![0].fotoLocal == "S") binding.linearLayout7.visibility = View.GONE
        else binding.linearLayout7.visibility = View.VISIBLE
    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.registeremployee_titlee))
        if (AnimotionLottie.redirect == "ListEmployee") {
            AnimotionLottie.redirect = ""
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.Fragpricipal, EmployeeListFragment(), "WelcomeFragment").commit()
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
        if (context is MainInterface) {
            mainInterface = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }

    private fun defaultData() {
        foto = if (arguments != null) {
            requireArguments().getString("foto")
        }else{
            fotoString
        }
        binding.nameEmployee.setText(nameEmploye)
        binding.apMaternoEmployee.setText(apMaternoString)
        binding.apPaternoEmployee.setText(apPaternoString)
        binding.numEmployeRegister.setText(nEmpleadp)
        binding.zonasSeleccinadasR.setText(nameZone)
    }

    private fun loadDataPersistence() {
        nameEmploye = binding.nameEmployee.text.toString()
        apPaternoString = binding.apPaternoEmployee.text.toString()
        apMaternoString = binding.apMaternoEmployee.text.toString()
        nEmpleadp = binding.numEmployeRegister.text.toString()
        nameZone = binding.zonasSeleccinadasR.text.toString()
        idEstSelect = itemsSeleccionados
        idZonSelect = itemsSeleccionadosE
        fotoString = foto
    }

    private fun addObserver() {
        registerEmployeeFragmentViewModel.altaEmpleadoResponseMutableData.observe(viewLifecycleOwner
        ) {
            altaEmpleado = it
            if (altaEmpleado.codigo == "ET000") {
                successAltaEmpleado()
            } else {
                Utilities.loadMessageError(
                    altaEmpleado.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        registerEmployeeFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        registerEmployeeFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
        registerEmployeeFragmentViewModel.consultaEstacionResponseMutableData.observe(viewLifecycleOwner
        ) {
            consultaEstacionResponse = it
            if (consultaEstacionResponse.codigo == "ET000") {
                loadReciclerStations()
            } else {
                Utilities.loadMessageError(
                    consultaEstacionResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        registerSupervisorViewModel.busquedaZonaResponseMutableLiveData.observe(viewLifecycleOwner
        ) {
            busquedazonasResponse = it
            if (busquedazonasResponse.codigo == "ET000") {
                println("Success consulta Zonas")
                loadSelectedZ()
            } else {
                //Utilities.loadMessageError(busquedazonasResponse.codigo, requireActivity())
            }
        }

    }

    private fun loadServices() {
        val request = ConsultaEstacionRequest(User.idUsuario, null)
        registerEmployeeFragmentViewModel.getConsultaEstacion(request)
        val busquedaZonaRequest = BusquedaZonaRequest(idUsuario = User.idUsuario)
        registerSupervisorViewModel.getBusquedaZonasSuperService(busquedaZonaRequest)
    }
    private fun loadReciclerStations() {
        loadSelected()
    }
    private fun saveData() {
        binding.estacionesSeleccinadas.setText(itemsSeleccionados.size.toString())

        binding.saveEmployee.setOnClickListener {
            val nameEmployeekt = binding.nameEmployee.text.toString().trim()
            val apPaternokt = binding.apPaternoEmployee.text.toString().trim()
            val numEmployeekt = binding.numEmployeRegister.text.toString().trim()
            val checkFields = checkFields(nameEmployeekt.trim(), apPaternokt.trim(), numEmployeekt.trim())
            if (checkFields == "OK") {
                val requestData = validateRequest()
                registerEmployeeFragmentViewModel.getAltaEmpleadoService(requestData)
            } else {
                Toast.makeText(activity, checkFields, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateRequest(): AltaEmpleadoRequest {
        val request = AltaEmpleadoRequest(idCia = User.scia!![0].cia, idEmpleado = binding.numEmployeRegister.text.toString().toLong(),
                nombre = binding.nameEmployee.text.toString().trim(), apellidoPat = binding.apPaternoEmployee.text.toString().trim(),
                apellidoMat = if (binding.apMaternoEmployee.text.toString().isBlank().not()) {
                    binding.apMaternoEmployee.text.toString().trim()
                } else {
                    null
                },
                fechaIngreso = Utilities.dateToday(),
                idEstacion = if (itemsSeleccionados.size > 0) {
                    itemsSeleccionados
                } else {
                    itemsSeleccionadosVacios
                },
                foto = if (foto.isNullOrBlank().not()) {
                    foto
                } else {
                    fotoVacia
                },
            idZona = itemsSeleccionadosE.toString().replace("[","").replace("]","").trim(),
            correo = binding.emailAdminData.text.toString().trim().lowercase()
        )

        return request
    }

    private fun checkFields(nameEmployeekt: String, apPaternokt: String, numEmployeekt: String): String {

        if (numEmployeekt.isEmpty() || numEmployeekt.isBlank()) {
            return getString(R.string.error_employee_data_number_empty)

        }
        if (nameEmployeekt.isEmpty() || nameEmployeekt.isBlank()) {
            return getString(R.string.error_employee_data_name_empty)
        }
        if (apPaternokt.isEmpty() || apPaternokt.isBlank()) {
            return getString(R.string.error_employee_data_appaterno_empty)
        }



        return "OK"
    }

    private fun backButton() {
        binding.btnBackRegEmployee.setOnClickListener {
            //  UpdatePhotoAdminFragment.fotoBase64 = ""
            fotoString = ""
            resetPersistence()
            itemsSeleccionados.clear()
            itemsSeleccionadosE.clear()
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, EmployeeListFragment(), "WelcomeFragment")
                    .commit()
        }
    }

    private fun resetPersistence() {
        nEmpleadp = null
        nameEmploye = null
        apPaternoString = null
        apMaternoString = null
    }

    private fun successAltaEmpleado() {
        itemsSeleccionados.clear()
        itemsSeleccionadosE.clear()
        val intent = Intent(requireActivity(), AnimotionLottie::class.java)
        intent.putExtra(Constants.FROM_LOG_OUT, true)
        intent.putExtra("Redirect", "ListEmployee")
        startActivity(intent)
    }

    private fun textSelect(): String {

        val texto: String = if (SelectedStationFragment.itemsSeleccionados.size.toString().toInt() > 0) {
            SelectedStationFragment.itemsSeleccionados.size.toString() + " " + getString(R.string.side_menu_title_6)
        } else {
            ""
        }

        return texto
    }

    private fun loadPhoto() {
        if (foto.isNullOrBlank().not()) {
            val imagenBase64 = Base64.decode(foto, Base64.DEFAULT)
            val imagenconverBitmap = BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
            binding.employePhotoEnrroll.setImageBitmap(imagenconverBitmap)
            binding.photoLoadingAnimation.root.visibility = View.GONE
        } else {
            binding.employePhotoEnrroll.setImageResource(R.drawable.avartar_)
            binding.photoLoadingAnimation.root.visibility = View.GONE
        }

        binding.employeeAdmin.setOnClickListener {
            binding.btnBackRegEmployee.visibility = View.GONE
            loadDataPersistence()
            val cameraAdminFragment = CameraAdminFragment()
            val bundle = Bundle()
            bundle.putString("redirect", "RegisterEmployeeFragment")
            cameraAdminFragment.arguments = bundle
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, cameraAdminFragment, "WelcomeFragment")
                    .commit()
        }
    }
    private fun openDialogSelectStation() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogCustomList)
        val bind: FragmentSelectZonesBinding = FragmentSelectZonesBinding.inflate(layoutInflater)
        dialog.setView(bind.root)
        dialog.setCancelable(false)
        dialog.create()
        val alertDialog: AlertDialog = dialog.create()
        bind.searchTextField.isVisible = true
        bind.titleZones.text = getString(R.string.list_estaciones_title)
        bind.etFilter.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()){
                filter(text.toString(), bind.reciclerZones)
            }else{
                bind.reciclerZones.adapter = SimpleAdapterEstacionesSelect(requireActivity(), imageModelArrayList)
            }
        }
        bind.saveSelectedZones.setOnClickListener {
            alertDialog.dismiss()
            loadSelected()
        }
        val list = java.util.ArrayList<SearchModel>()
        if (consultaEstacionResponse.sEstacion != null) {
            for (i in 0 until consultaEstacionResponse.sEstacion!!.size) {
                val dataModel = SearchModel()
                dataModel.setNames(consultaEstacionResponse.sEstacion!![i].nombre)
                dataModel.setStatuss(consultaEstacionResponse.sEstacion!![i].status)
                dataModel.setIdd(consultaEstacionResponse.sEstacion!![i].idEstacion.toString())
                list.add(dataModel)
            }
            imageModelArrayList = list
            adapter = SimpleAdapterEstacionesSelect(requireActivity(), imageModelArrayList)
            bind.reciclerZones.adapter = adapter
        }
        try {
            alertDialog.show()
        } catch (e: IllegalStateException) {
            (bind.root.parent as ViewGroup).removeView(bind.root)
            alertDialog.show()
        }
    }
    private fun filter(text: String, reciclerZones: RecyclerView) {
        val filteredlist: ArrayList<SearchModel> = arrayListOf()
        for (item in imageModelArrayList) {
            if (!item.name.isNullOrEmpty()){
                if (item.name!!.lowercase().contains(text.lowercase())) {
                        filteredlist.add(item)
                    }
            }

        }
        if (filteredlist.isEmpty()) {
            reciclerZones.adapter = SimpleAdapterEstacionesSelect(requireActivity(), arrayListOf())
        } else {
            reciclerZones.adapter = SimpleAdapterEstacionesSelect(requireActivity(), filteredlist)
        }
    }
    private fun loadSelected() {
        if (itemsSeleccionados.size == 0) {
            binding.estacionesSeleccinadas.setText(itemsSeleccionados.size.toString() + " " + getString(R.string.one_select))
        } else if (itemsSeleccionados.size == 1) {
            binding.estacionesSeleccinadas.setText(itemsSeleccionados.size.toString() + " " + getString(R.string.one_select))
        } else if (itemsSeleccionados.size > 1) {
            binding.estacionesSeleccinadas.setText(itemsSeleccionados.size.toString() + " " + getString(R.string.many_select))
        }
    }
    fun showLoadingAnimation() {
        binding.loadingAnimationEmployee.visibility = View.VISIBLE
        requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    fun hideLoadingAnimation() {
        binding.loadingAnimationEmployee.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = requireActivity().packageName
        val identificadorMensaje = this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje>0) {
            Utilities.showDialog(requireActivity().getString(identificadorMensaje), requireActivity(),childFragmentManager)
        }
        else{
            Utilities.showDialog(code.toString(), requireActivity(),childFragmentManager)
        }
    }
    private fun listensButtons() {
        binding.zonasSeleccinadasR.setOnClickListener {
            loadZonasSelected()
        }
    }
    private fun loadZonasSelected() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogCustomList)
        val bind: FragmentSelectZonesBinding = FragmentSelectZonesBinding.inflate(layoutInflater)
        bind.searchTextField.isVisible = true
        bind.etFilter.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()){
                filterZones(text.toString(), bind.reciclerZones)
            }else{
                bind.reciclerZones.adapter = SimpleAdapterZonesSelectEmployee(requireActivity(), imageModelArrayListEmployee)
            }
        }
        dialog.setView(bind.root)
        dialog.setCancelable(false)
        dialog.create()
        val alertDialog: AlertDialog = dialog.create()
        bind.saveSelectedZones.setOnClickListener {
            alertDialog.dismiss()
            loadSelectedZ()
        }
        val lista = java.util.ArrayList<SearchModel>()

        for (i in 0 until busquedazonasResponse.totalRegistros) {
            val dataModel = SearchModel()
            dataModel.setIdd(busquedazonasResponse.sZona!![i].idZona.toString())
            dataModel.setNames(busquedazonasResponse.sZona!![i].descripcion)
            dataModel.setStatuss(busquedazonasResponse.sZona!![i].estatus)
            lista.add(dataModel)

        }
        imageModelArrayListEmployee = lista
        val simpleAdapter = SimpleAdapterZonesSelectEmployee(requireActivity().applicationContext,
            imageModelArrayListEmployee
        )
        bind.reciclerZones.adapter = simpleAdapter
        try {
            alertDialog.show()
        } catch (e: IllegalStateException) {
            (bind.root.parent as ViewGroup).removeView(bind.root)
            alertDialog.show()
        }
    }
    private fun filterZones(text: String, reciclerZones: RecyclerView) {
        val filteredlist: ArrayList<SearchModel> = arrayListOf()
        for (item in imageModelArrayListEmployee) {
            if (!item.name.isNullOrEmpty()){
                if (item.name!!.lowercase().contains(text.lowercase())) {
                    filteredlist.add(item)
                }
            }

        }
        if (filteredlist.isEmpty()) {
            reciclerZones.adapter = SimpleAdapterZonesSelectEmployee(requireActivity(), arrayListOf())
        } else {
            reciclerZones.adapter = SimpleAdapterZonesSelectEmployee(requireActivity(), filteredlist)
        }
    }
    private fun loadSelectedZ() {
        when (itemsSeleccionadosE.size) {
            0 -> {
                binding.zonasSeleccinadasR.setText(getString(R.string.one_zone))
                unlockButton()
            }
            1 -> {
                binding.zonasSeleccinadasR.setText(nameZone)
                unlockButton()
            }
            else -> {
                binding.zonasSeleccinadasR.setText(getString(R.string.no_zone))
                Toast.makeText(activity, getString(R.string.no_zone), Toast.LENGTH_SHORT).show()
                itemsSeleccionadosE.clear()
                lockButton()
            }
        }

    }
    private fun showTextZones() {

        if (SelectZonesFragment.nItems.size == 0) {
            binding.zonasSeleccinadasR.setText("")
        } else {
            binding.zonasSeleccinadasR.setText(SelectZonesFragment.nItems.size.toString() + " " + getString(R.string.side_menu_title_2))
        }
    }
    private fun showTextEstaciones(){
        val editEstacionesSeleccionadas = requireActivity().findViewById(R.id.estacionesSeleccinadas) as TextView

        if (SelectedStationFragment.itemsSeleccionados.size == 0) {
            binding.estacionesSeleccinadas.setText("")
        } else {
            binding.estacionesSeleccinadas.setText(SelectedStationFragment.itemsSeleccionados.size.toString() + " " + getString(R.string.side_menu_title_2))
        }
    }
    private fun lockButton(){
       binding.saveEmployee.isEnabled = false
    }
    private fun unlockButton(){
        binding.saveEmployee.isEnabled = true
    }
}
