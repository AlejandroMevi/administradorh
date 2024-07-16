package com.venturessoft.human.views.ui.fragments.Supervisor

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.venturessoft.human.R
import com.venturessoft.human.databinding.DialogNewCategoryBinding
import com.venturessoft.human.databinding.FragmentEditSupervisorsBinding
import com.venturessoft.human.databinding.FragmentSelectZonesBinding
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.models.request.ActualizarUsuarioRequest
import com.venturessoft.human.models.request.BusquedaZonaRequest
import com.venturessoft.human.models.response.ActualizarUsuarioResponse
import com.venturessoft.human.models.response.BusquedaZonaAdmiResponse
import com.venturessoft.human.models.response.BusquedaZonaResponse
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.adapters.SimpleAdapterEstacionesSelect
import com.venturessoft.human.views.adapters.SimpleAdapterZonesSelectEdit
import com.venturessoft.human.views.ui.activities.AnimotionLottie
import com.venturessoft.human.views.ui.fragments.Employe.RegisterEmployeeFragment
import com.venturessoft.human.views.ui.fragments.Zones.SelectZonesFragment
import com.venturessoft.human.views.ui.viewModels.EditSupervisorFragmentViewModel

class EditSupervisorFragment : Fragment() {

    private var checkStation: Boolean = false
    private var editSupervisorFragmentViewModel = EditSupervisorFragmentViewModel()
    private var busquedaZonaResponse = BusquedaZonaResponse()
    private var busquedaZonaAdmiResponse = BusquedaZonaAdmiResponse()
    var email: String? = null
    private var actualizaUsuarioResponse = ActualizarUsuarioResponse()
    private var listener: OnFragmentInteractionListener? = null
    private var mainInterface: MainInterface? = null
    var id: String? = ""
    lateinit var myContext: Activity

    companion object {
        lateinit var imageModelArrayList: ArrayList<SearchModel>
        var itemsSeleccionados = ArrayList<Int>()
        var itemsDeseleccionados = ArrayList<Int>()
        var itemsSeleccionadosSuper = ArrayList<Int>()
        var idEdit: String? = ""
        var datosEditadosAfterListZones = false
        var nombre: String? = ""
        var apPaterno: String? = ""
        var apMaterno: String? = ""
        var password: String? = ""
        var idZona: String = ""
        var numRegistros: String = "9000"
    }

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    private lateinit var binding: FragmentEditSupervisorsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myContext = requireActivity()
        itemsSeleccionados.clear()
        itemsSeleccionadosSuper.clear()
        itemsDeseleccionados.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditSupervisorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.onBack("backEditSuper")
        btnBackListSuper()
        addObservers()
        loadServices()
        listensButtons()
        defaultDisabledData()
        loadData()
        binding.zonasSeleccinadasEdit.setOnClickListener {
            loadZonasSelected()
        }
    }

    private fun loadServices() {
        val busquedaZonaRequest = BusquedaZonaRequest(ListaSupervisorFragment.idSelected.toLong())
        editSupervisorFragmentViewModel.getBusquedaZonas(busquedaZonaRequest)
    }

    private fun addObservers() {
        editSupervisorFragmentViewModel.busquedaZonaResponseMutableData.observe(viewLifecycleOwner) {
            busquedaZonaResponse = it
            if (busquedaZonaResponse.codigo == "ET000" || busquedaZonaResponse.codigo == "ET327") { //){
                successConsultaZonas()
            } else if (busquedaZonaResponse.codigo != "ET327") {
                Utilities.loadMessageError(it.codigo, requireActivity(), childFragmentManager)
            } else {
                successConsultaZonas()
            }
        }
        editSupervisorFragmentViewModel.busquedaZonaAdmiResponseMutableData.observe(
            viewLifecycleOwner
        ) {
            busquedaZonaAdmiResponse = it
            if (busquedaZonaAdmiResponse.codigo == "ET000" || busquedaZonaAdmiResponse.codigo == "ET327") {
                successConsultaZonasAdmin()
            } else if (busquedaZonaAdmiResponse.codigo != "ET327") {
                Utilities.loadMessageError(
                    busquedaZonaResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        editSupervisorFragmentViewModel.actualizarUsuarioResponseMutableData.observe(
            viewLifecycleOwner
        ) {
            actualizaUsuarioResponse = it
            if (actualizaUsuarioResponse.codigo == "ET000") {
                successActualizaUsuario()
            } else {
                Utilities.loadMessageError(
                    actualizaUsuarioResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        editSupervisorFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        editSupervisorFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
    }

    private fun successConsultaZonasAdmin() {
        if (itemsSeleccionadosSuper.size > 0) {
            for (i in 0 until itemsSeleccionadosSuper.size) {
                itemsSeleccionados.add(itemsSeleccionadosSuper[i])
            }
        }
        loadSelected()
    }

    private fun successConsultaZonas() {
        if (busquedaZonaResponse.sZona != null) {
            for (i in 0 until busquedaZonaResponse.sZona!!.size) {
                itemsSeleccionadosSuper.add(busquedaZonaResponse.sZona!![i].idZona!!)
            }
        }
        loadAdminRequest()
    }

    private fun loadAdminRequest() {
        val busquedaZonasRequest = BusquedaZonaRequest(User.idUsuario)
        editSupervisorFragmentViewModel.getConsultaZonasAdmin(busquedaZonasRequest)
    }

    private fun successActualizaUsuario() {
        val dialogLogout = DialogGeneral(
            getString(R.string.good),
            getString(R.string.successful_change),
            null,
            null,
            {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.Fragpricipal, ListaSupervisorFragment(), "WelcomeFragment")
                    .commit()
            }
        )
        dialogLogout.show(childFragmentManager, "")
    }

    private fun listensButtons() {
        binding.statusEstacion.setOnClickListener {
            checkStation = binding.statusEstacion.isChecked
        }
        binding.btnSaveSupervisorEdit.setOnClickListener {
            val checkFields = checkFields(
                binding.supernameEdit.text.toString(),
                binding.superApPaternoEdit.text.toString(),
                binding.superEmailEdit.text.toString()
            )
            if (checkFields == "OK") {
                val request = validateRequest()
                editSupervisorFragmentViewModel.getActualizarUsuarioService(request)
            } else {
                Toast.makeText(requireActivity(), checkFields, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateRequest(): ActualizarUsuarioRequest {
        val cero = ArrayList<Int>()
        cero.add(0)
        val request = ActualizarUsuarioRequest(
            idUsuario = ListaSupervisorFragment.idSelected.toLong(),
            email = binding.superEmailEdit.text.toString(),
            nombre = binding.supernameEdit.text.toString(),
            apellidoPat = binding.superApPaternoEdit.text.toString(),
            apellidoMat = binding.superApMaternoEdit.text.toString().ifBlank { "" },
            telefono = null,
            codigoTel = null,
            estatus = null,
            idZona = if (itemsSeleccionados.size > 0) {
                itemsSeleccionados
            } else {
                null
            },
            idZonasEliminar = if (itemsDeseleccionados.size > 0) {
                itemsDeseleccionados
            } else {
                cero
            },
            estaciones = checkStation
        )
        return request
    }

    private fun loadData() {
        if (SelectZonesFragment.nItems.size == 0) {
            binding.zonasSeleccinadasEdit.setText("")
        } else {
            binding.zonasSeleccinadasEdit.setText(SelectZonesFragment.nItems.size.toString())
        }
        if (arguments != null) {
            id = requireArguments().getString("idSelected")
            idEdit = requireArguments().getString("idSelected")
            binding.supernameEdit.setText(requireArguments().getString("nameSuper"))
            binding.superApPaternoEdit.setText(requireArguments().getString("apPaterno"))
            binding.superApMaternoEdit.setText(requireArguments().getString("apMaterno"))
            binding.superEmailEdit.setText(requireArguments().getString("correo"))
            this.email = requireArguments().getString("correo")
            checkStation = requireArguments().getBoolean("estaciones", false)
            binding.statusEstacion.isChecked = checkStation
            binding.statusEstacion.isChecked = checkStation
        }
    }

    private fun checkFields(nombre: String, apPaterno: String, correo: String): String {
        if (nombre.isEmpty() || nombre.isBlank()) {
            return getString(R.string.error_supervisor_data_name_empty)
        }
        if (apPaterno.isEmpty() || apPaterno.isBlank()) {
            return getString(R.string.error_supervisor_data_appat_empty)
        }
        if (binding.superEmailEdit.text.toString().isEmpty()) {
            return getString(R.string.error_admin_correo_empty)
        }
        if (!correo.contains('@')) {
            binding.superEmailEdit.text
            binding.superEmailEdit.setText("")
            return getString(R.string.a_login_error_email_valids)
        }
        return "OK"
    }

    private fun defaultDisabledData() {
        binding.supernameEdit.isEnabled = true
        binding.superApPaternoEdit.isEnabled = true
        binding.superApMaternoEdit.isEnabled = true
        binding.superEmailEdit.isEnabled = true
        binding.tilsuperPasswordEdit.isEnabled = true
        binding.tilsuperPasswordConfirmEdit.isEnabled = true
        binding.superWichEdit.isEnabled = true
        binding.btnSaveSupervisorEdit.isEnabled = true
        binding.btnSaveSupervisorEdit.visibility = View.VISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainInterface) {
            mainInterface = context
        }
        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        mainInterface = null
    }

    private fun btnBackListSuper() {
        binding.btnBackSuperEdit.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.Fragpricipal, ListaSupervisorFragment(), "ListaEstaciones").commit()
        }
    }

    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.supervisor_edit))
        if (AnimotionLottie.redirect == "ListaSupervisor") {
            AnimotionLottie.redirect = ""
            datosEditadosAfterListZones = false
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.Fragpricipal, ListaSupervisorFragment(), "ListaEstaciones").commit()
        }
    }

    private fun loadZonasSelected() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogCustomList)
        val bind: FragmentSelectZonesBinding = FragmentSelectZonesBinding.inflate(layoutInflater)
        bind.searchTextField.isVisible = true
        bind.etFilter.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()){
                filter(text.toString(), bind.reciclerZones)
            }else{
                bind.reciclerZones.adapter = SimpleAdapterZonesSelectEdit(requireActivity(), imageModelArrayList)
            }
        }
        dialog.setView(bind.root)
        dialog.setCancelable(false)
        dialog.create()
        val alertDialog: AlertDialog = dialog.create()
        bind.saveSelectedZones.setOnClickListener {
            alertDialog.dismiss()
            loadSelected()
        }
        val list = java.util.ArrayList<SearchModel>()
        if (busquedaZonaAdmiResponse.totalRegistros > 0) {
            for (i in 0 until busquedaZonaAdmiResponse.totalRegistros) {
                val dataModel = SearchModel()
                dataModel.setIdd(busquedaZonaAdmiResponse.sZona!![i].idZona.toString())
                dataModel.setNames(busquedaZonaAdmiResponse.sZona!![i].descripcion)
                dataModel.setStatuss(busquedaZonaAdmiResponse.sZona!![i].estatus)
                list.add(dataModel)
            }
            imageModelArrayList = list
            val simpleAdapter = SimpleAdapterZonesSelectEdit(
                requireActivity().applicationContext,
                imageModelArrayList
            )
            bind.reciclerZones.adapter = simpleAdapter
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
            reciclerZones.adapter = SimpleAdapterZonesSelectEdit(requireActivity(), arrayListOf())
        } else {
            reciclerZones.adapter = SimpleAdapterZonesSelectEdit(requireActivity(), filteredlist)
        }
    }
    private fun loadSelected() {
        val zonasSeleccinadas = myContext.findViewById<TextView>(R.id.zonasSeleccinadasEdit)
        when (itemsSeleccionados.size) {
            0 -> zonasSeleccinadas.text = " " + getString(R.string.none_select)
            1 -> zonasSeleccinadas.text = itemsSeleccionados.size.toString() + " " + getString(R.string.one_select)
            else -> zonasSeleccinadas.text = itemsSeleccionados.size.toString() + " " + getString(R.string.many_select)
        }
    }

    fun showLoadingAnimation() {
        binding.loadingAnimationEditSuper.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideLoadingAnimation() {
        binding.loadingAnimationEditSuper.root.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = requireActivity().packageName
        val identificadorMensaje =
            this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(
                requireActivity().getString(identificadorMensaje),
                requireActivity(),
                childFragmentManager
            )
        } else {
            Utilities.showDialog(code.toString(), requireActivity(), childFragmentManager)
        }
    }
}
