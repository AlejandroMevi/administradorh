package com.venturessoft.human.views.ui.fragments.Employe

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentEditEmployeeBinding
import com.venturessoft.human.databinding.FragmentSelectZonesBinding
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.models.User
import com.venturessoft.human.models.request.ActualizaEmpleadoRequest
import com.venturessoft.human.models.request.BusquedaZonaRequest
import com.venturessoft.human.models.request.ConsultaEstacionRequest
import com.venturessoft.human.models.response.ActualizaEmpleadoResponse
import com.venturessoft.human.models.response.BusquedaZonaResponse
import com.venturessoft.human.models.response.ConsultaEstacionResponse
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.bd.DBDemo
import com.venturessoft.human.views.adapters.SimpleAdapterEstacionesSelectEdit
import com.venturessoft.human.views.adapters.SimpleAdapterZonesSelectEmployeeEdit
import com.venturessoft.human.views.ui.activities.AnimotionLottie
import com.venturessoft.human.views.ui.fragments.CameraAdmin.CameraAdminFragment
import com.venturessoft.human.views.ui.fragments.UpdatePhotoAdmin.UpdatePhotoAdminFragment
import com.venturessoft.human.views.ui.viewModels.EditEmployeeFragmentViewModel
import com.venturessoft.human.views.ui.viewModels.RegisterSupervisorFragmentViewModel

class EditEmployeeFragment : Fragment() {
    private var editEmployeeFragmentViewModel = EditEmployeeFragmentViewModel()
    private var actualizaEmpleadoResponse = ActualizaEmpleadoResponse()
    private var consultaEstacionResponseULogin = ConsultaEstacionResponse()
    private var consultaEstacionResponseEmployee = ConsultaEstacionResponse()
    private var busquedazonasResponse = BusquedaZonaResponse()
    private var listener: OnFragmentInteractionListener? = null
    private var mainInterface: MainInterface? = null
    lateinit var myDataBase: DBDemo
    var id: String? = ""
    private var registerSupervisorViewModel = RegisterSupervisorFragmentViewModel()
    lateinit var adapter: SimpleAdapterEstacionesSelectEdit
    lateinit var myContext: Activity

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    companion object {
        lateinit var imageModelArrayList: ArrayList<SearchModel>
        var itemsSeleccionados=ArrayList<Int>()
        var itemsDeseleccionados = ArrayList<Int>()
        var itemsSeleccionadosE = ArrayList<Int>()
        var nameZone : String = ""
        lateinit var imageModelArrayListEmployeeEdit: ArrayList<SearchModel>
        var foto: String? = ""
        var correo: String? = ""
        var fotoVacia: String = ""
        private const val ARG_USER = "userInfo"
        private const val ARG_NAME_EMPLOYEE = "nameEmployee"
        private const val ARG_APPATERNO = "apPaterno"
        private const val ARG_APMATERNO = "apMaterno"
        private const val ARG_NEMPLEADO = "nEmpleado"
        private const val ARG_ARRAYDATA = "arrayData"
        fun newInstance(loggedUser: User, nameEmployeekt: String?, apPaterno: String?, apMaterno: String?, nEmpleadp: String?, itemsSeleccionados: ArrayList<Int>?): EditEmployeeFragment {
            val fragment = EditEmployeeFragment()
            val args = Bundle()
            args.putSerializable(ARG_USER, loggedUser)
            args.putSerializable(ARG_NAME_EMPLOYEE, nameEmployeekt)
            args.putSerializable(ARG_APPATERNO, apPaterno)
            args.putSerializable(ARG_APMATERNO, apMaterno)
            args.putSerializable(ARG_NEMPLEADO, nEmpleadp)
            args.putSerializable(ARG_ARRAYDATA, itemsSeleccionados)
            fragment.arguments = args
            return fragment
        }
        private var goChangePhoto: Boolean = false
        var nEmpleadp: String? = null
        var nameEmploye: String? = null
        var apPaterno: String? = null
        var apMaterno: String? = null
        var desZona: String? = null
        var arrayData: ArrayList<Int>? = null
        var idEstSelect: ArrayList<Int>? = null
        var idZonSelect: ArrayList<Int>? = null
    }
    private lateinit var binding : FragmentEditEmployeeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myContext = requireActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEditEmployeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.onBack("backListMantenanceEmployee")
        backListEmployee()
        loadData()
        loadDataPersistencia()
        loadServices()
        addObservers()
        loadSelectedZ()
        unlockButton()
        cargarZona()
        binding.estacionesSeleccinadasEdit.setOnClickListener {
            openDialogSelectStation()
        }
        binding.zonasSeleccinadas.setOnClickListener {
            loadZonasSelected()
        }
        binding.photoLoadingAnimationEdit.root.visibility = View.GONE
        if (com.venturessoft.human.utils.User.scia!![0].fotoLocal == "S") binding.linearLayout6.visibility = View.GONE
        else binding.linearLayout6.visibility = View.VISIBLE
    }
    private fun loadDataPersistencia() {
        nEmpleadp = binding.numEmployeeEdit.text.toString()
        nameEmploye = binding.nameEmployeeEdit.text.toString()
        apPaterno = binding.apPaternoEmployeeEdit.text.toString()
        apMaterno = binding.apMaternoEmployeeEdit.text.toString()
        desZona = binding.zonasSeleccinadas.text.toString()
        idEstSelect = itemsSeleccionados
        idZonSelect = itemsSeleccionadosE
    }
    private fun loadServices() {
        val request = ConsultaEstacionRequest(com.venturessoft.human.utils.User.idUsuario, null)
        editEmployeeFragmentViewModel.getConsultaEstacion(request)
        val busquedaZonaRequest = BusquedaZonaRequest(idUsuario = com.venturessoft.human.utils.User.idUsuario)
        registerSupervisorViewModel.getBusquedaZonasSuperService(busquedaZonaRequest)
    }
    private fun addObservers() {
        editEmployeeFragmentViewModel.actualizaEmpleadoResponseMutableData.observe(viewLifecycleOwner
        ) {
            actualizaEmpleadoResponse = it
            if (actualizaEmpleadoResponse.codigo == "ET000") {
                successActualizaEmpleado()
            } else {
                Utilities.loadMessageError(
                    actualizaEmpleadoResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }

        editEmployeeFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        editEmployeeFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
        editEmployeeFragmentViewModel.consultaEstacionResponseMutableData.observe(viewLifecycleOwner
        ) {
            consultaEstacionResponseULogin = it
            if (consultaEstacionResponseULogin.codigo == "ET000") {
                loadDataStationsSelected()

            } else {
                Utilities.loadMessageError(
                    consultaEstacionResponseULogin.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        editEmployeeFragmentViewModel.consultaEstacionEmployeeResponseMutableData.observe(viewLifecycleOwner
        ) {
            consultaEstacionResponseEmployee = it
            if (consultaEstacionResponseEmployee.codigo == "ET000") {
            } else {
                Utilities.loadMessageError(
                    consultaEstacionResponseEmployee.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        registerSupervisorViewModel.busquedaZonaResponseMutableLiveData.observe(viewLifecycleOwner
        ) {
            busquedazonasResponse = it
            if (busquedazonasResponse.codigo == "ET000") {
                loadDataZoneSelected()
            } else {
                Utilities.loadMessageError(
                    busquedazonasResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
    }

    private fun loadDataStationsSelected() {
        if (EmployeeListFragment.estaciones != null) {
            for (i in 0 until EmployeeListFragment.estaciones!!.size) {
                itemsSeleccionados.add(EmployeeListFragment.estaciones!![i])
            }
        }
        loadSelected()
    }
    private fun loadDataZoneSelected() {
        if (EmployeeListFragment.zonas != null) {
            for (i in 0 until EmployeeListFragment.zonas!!.size) {
                itemsSeleccionadosE.add(EmployeeListFragment.zonas!![i])
            }
        }
        loadSelecetZones()
    }
    private fun loadSelecetZones(){
        when(itemsSeleccionadosE.size){
            0 ->    binding.zonasSeleccinadas.setText(" " + getString(R.string.none_select))
            1 ->    binding.zonasSeleccinadas.setText(desZona)
            else -> binding.zonasSeleccinadas.setText("Selecciona solo una zona")
        }
    }
    private fun cargarZona(){
        val zonasSeleccinadas = binding.zonasSeleccinadas
        zonasSeleccinadas.setText(desZona)
    }

    private fun successActualizaEmpleado() {
        itemsSeleccionados.clear()
        itemsSeleccionadosE.clear()
        val dialogLogout = DialogGeneral(
            getString(R.string.good),
            getString(R.string.successful_change),
            requireContext().getString(R.string.ok),
            null,
            {
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.Fragpricipal, EmployeeListFragment(), "WelcomeFragment").commit()
            }
        )
        dialogLogout.show(childFragmentManager,"")
    }

    private fun loadData() {
        binding.estacionesSeleccinadasEdit.setText(itemsSeleccionados.size.toString())

        if (arguments != null) {
            binding.nameEmployeeEdit.setText(requireArguments().getString("nameEmployee"))
            binding.apPaternoEmployeeEdit.setText(
                requireArguments().getString("apPaternoEmployee")!!.lowercase())
            binding.apMaternoEmployeeEdit.setText(requireArguments().getString("apMaternoEmployee"))
            binding.numEmployeeEdit.setText(requireArguments().getString("numEmployeRegister"))
            binding.zonasSeleccinadas.setText(requireArguments().getString("descripcionZona"))
            binding.emailAdminData.setText(requireArguments().getString("correo"))
            foto = requireArguments().getString("foto")
        } else {
            binding.nameEmployeeEdit.setText(nameEmploye)
            binding.apPaternoEmployeeEdit.setText(apPaterno)
            binding.apMaternoEmployeeEdit.setText(apMaterno)
            binding.numEmployeeEdit.setText(nEmpleadp)
            binding.zonasSeleccinadas.setText(desZona)
            itemsSeleccionados = idEstSelect!!
            itemsSeleccionadosE = idZonSelect!!
        }
        if (foto.isNullOrBlank().not()) {
            val imagenBase64 = Base64.decode(foto, Base64.DEFAULT)
            val imagenconverBitmap = BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
            binding.employePhotoEnrrollEdi.setImageBitmap(imagenconverBitmap)
        }
        binding.saveEmployeeEdit.setOnClickListener {
            if (com.venturessoft.human.utils.User.scia!![0].fotoLocal == "S") foto = ""
            val checkFields = checkFiels()
            if (checkFields == "OK") {
                val request = ActualizaEmpleadoRequest(nombre = binding.nameEmployeeEdit.text.toString().trim(), apellidoPat = binding.apPaternoEmployeeEdit.text.toString().trim(),
                    apellidoMat = if (binding.apMaternoEmployeeEdit.text.toString().isNotEmpty()) {
                        binding.apMaternoEmployeeEdit.text.toString().trim()
                } else {
                    ""
                }, idEstacion = if (itemsSeleccionados.size > 0) {
                    itemsSeleccionados
                } else {
                    null
                }, foto = if (foto.isNullOrBlank().not()) {
                    foto
                } else {
                    fotoVacia
                },idEstacionEliminar =if (itemsDeseleccionados.size>0) itemsDeseleccionados else null,
                    idZona = if (itemsSeleccionadosE.size>0) itemsSeleccionadosE.toString().replace("[","").replace("]","").trim() else null,
                    correo = binding.emailAdminData.text.toString().trim()
                )
                editEmployeeFragmentViewModel.putActualizaEmpleadoService(request, idCia = com.venturessoft.human.utils.User.scia!![0].cia,
                    idEmpleado = binding.numEmployeeEdit.text.toString().toLong(),idUsuario = com.venturessoft.human.utils.User.idUsuario,)
            } else {
                Toast.makeText(requireActivity(), checkFields, Toast.LENGTH_SHORT).show()
            }
        }

        binding.employeeAdminEdit.setOnClickListener {
            loadDataPersistencia()
            goChangePhoto = true
            val cameraAdminFragment = CameraAdminFragment()
            val bundle = Bundle()
            bundle.putString("redirect", "EditEmployeeFragment")
            cameraAdminFragment.arguments = bundle
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, cameraAdminFragment, "WelcomeFragment")
                    .commit()
        }
    }

    private fun checkFiels(): String {
        if (binding.numEmployeeEdit.text.isNullOrEmpty()) {
            return getString(R.string.error_employee_data_number_empty)
        }
        if (binding.nameEmployeeEdit.text.isNullOrEmpty()) {
            return getString(R.string.error_employee_data_name_empty)
        }
        if (binding.apPaternoEmployeeEdit.text.isNullOrEmpty()) {
            return getString(R.string.error_employee_data_appaterno_empty)
        }
        return "OK"
    }

    private fun backListEmployee() {
        binding.btnBackRegEmployeeEdit.setOnClickListener {
            itemsSeleccionados.clear()
            itemsSeleccionadosE.clear()
            UpdatePhotoAdminFragment.fotoBase64 = ""
            goChangePhoto = false
            EmployeeListFragment.foto = null
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, EmployeeListFragment(), "ListaEstaciones")
                    .commit()
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
        if (context is MainInterface) {
            mainInterface = context
        }
    }
    override fun onDetach() {
        super.onDetach()
        listener = null
        mainInterface = null
    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.employee_edit))
        if (AnimotionLottie.redirect == "ListEmployee") {
            AnimotionLottie.redirect = ""
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, EmployeeListFragment(), "WelcomeFragment")
                    .commit()
        }
        if (goChangePhoto) {
            loadData()
        }
    }
    private fun openDialogSelectStation() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogCustomList)
        val bind: FragmentSelectZonesBinding = FragmentSelectZonesBinding.inflate(layoutInflater)
        bind.searchTextField.isVisible = true
        bind.etFilter.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()){
                filter(text.toString(), bind.reciclerZones)
            }else{
                bind.reciclerZones.adapter = SimpleAdapterEstacionesSelectEdit(requireActivity(), imageModelArrayList)
            }
        }
        dialog.setView(bind.root)
        dialog.setCancelable(false)
        dialog.create()
        val alertDialog: AlertDialog = dialog.create()
        bind.titleZones.text = getString(R.string.list_estaciones_title)
        bind.saveSelectedZones.setOnClickListener {
            alertDialog.dismiss()
            loadSelected()
        }
        val list = java.util.ArrayList<SearchModel>()
        if (consultaEstacionResponseULogin.sEstacion != null) {
            for (i in 0 until consultaEstacionResponseULogin.sEstacion!!.size) {
                val dataModel = SearchModel()
                dataModel.setNames(consultaEstacionResponseULogin.sEstacion!![i].nombre)
                dataModel.setStatuss(consultaEstacionResponseULogin.sEstacion!![i].status)
                dataModel.setIdd(consultaEstacionResponseULogin.sEstacion!![i].idEstacion.toString())
                list.add(dataModel)
            }
            imageModelArrayList = list
            adapter = SimpleAdapterEstacionesSelectEdit(requireActivity(), imageModelArrayList)
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
            reciclerZones.adapter = SimpleAdapterEstacionesSelectEdit(requireActivity(), arrayListOf())
        } else {
            reciclerZones.adapter = SimpleAdapterEstacionesSelectEdit(requireActivity(), filteredlist)
        }
    }
    private fun loadSelected() {
        val estacionesSeleccinadasEdit = myContext.findViewById<TextView>(R.id.estacionesSeleccinadasEdit)
        if (itemsSeleccionados.size == 0) {
            estacionesSeleccinadasEdit.text = " "+getString(R.string.none_select)
        } else if (itemsSeleccionados.size == 1) {
            estacionesSeleccinadasEdit.text = itemsSeleccionados.size.toString() +" "+ getString(R.string.one_select)
        } else  if (itemsSeleccionados.size>1) {
            estacionesSeleccinadasEdit.text = itemsSeleccionados.size.toString() + " "+getString(R.string.many_select)
        }
    }
    fun showLoadingAnimation() {
        binding.loadingAnimationSuperEdit.root.isVisible = true
    }
    fun hideLoadingAnimation() {
        binding.loadingAnimationSuperEdit.root.isVisible = false
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
    private fun loadZonasSelected() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogCustomList)
        val bind: FragmentSelectZonesBinding = FragmentSelectZonesBinding.inflate(layoutInflater)
        bind.searchTextField.isVisible = true
        bind.etFilter.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()){
                filterZones(text.toString(), bind.reciclerZones)
            }else{
                bind.reciclerZones.adapter = SimpleAdapterZonesSelectEmployeeEdit(requireActivity(),
                    imageModelArrayListEmployeeEdit
                )
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
        imageModelArrayListEmployeeEdit = lista
        val simpleAdapter = SimpleAdapterZonesSelectEmployeeEdit(requireActivity().applicationContext,
            imageModelArrayListEmployeeEdit
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
        for (item in imageModelArrayListEmployeeEdit) {
            if (!item.name.isNullOrEmpty()){
                if (item.name!!.lowercase().contains(text.lowercase())) {
                    filteredlist.add(item)
                }
            }

        }
        if (filteredlist.isEmpty()) {
            reciclerZones.adapter = SimpleAdapterZonesSelectEmployeeEdit(requireActivity(), arrayListOf())
        } else {
            reciclerZones.adapter = SimpleAdapterZonesSelectEmployeeEdit(requireActivity(), filteredlist)
        }
    }
    private fun loadSelectedZ() {
        val zonasSeleccinadas = myContext.findViewById<TextView>(R.id.zonasSeleccinadas)
        when (itemsSeleccionadosE.size) {
            0 -> {
                zonasSeleccinadas.text = getString(R.string.one_zone)
                lockButton()
            }
            1 -> {
                zonasSeleccinadas.text =  nameZone
                unlockButton()
            }
            else -> {
                zonasSeleccinadas.text = getString(R.string.no_zone)
                Toast.makeText(activity, getString(R.string.no_zone), Toast.LENGTH_SHORT).show()
                itemsSeleccionadosE.clear()
                lockButton()
            }
        }
    }
    private fun lockButton(){
        binding.saveEmployeeEdit.isEnabled = false
    }
    private fun unlockButton(){
        binding.saveEmployeeEdit.isEnabled = true
    }
}

