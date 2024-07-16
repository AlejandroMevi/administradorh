package com.venturessoft.human.views.ui.fragments.Supervisor

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentRegisterSupervisorBinding
import com.venturessoft.human.databinding.FragmentSelectZonesBinding
import com.venturessoft.human.models.request.AltaUsuarioSupervisorRequest
import com.venturessoft.human.models.request.BusquedaZonaRequest
import com.venturessoft.human.models.response.AltaUsuarioSupervisorResponse
import com.venturessoft.human.models.response.BusquedaZonaResponse
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.bd.DBDemo
import com.venturessoft.human.views.adapters.SimpleAdapterZonesSelect
import com.venturessoft.human.views.adapters.SimpleAdapterZonesSelectEdit
import com.venturessoft.human.views.adapters.SimpleAdapterZonesSelectEmployee
import com.venturessoft.human.views.ui.activities.AnimotionLottie
import com.venturessoft.human.views.ui.fragments.Employe.RegisterEmployeeFragment
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import com.venturessoft.human.views.ui.fragments.Zones.SelectZonesFragment
import com.venturessoft.human.views.ui.viewModels.RegisterSupervisorFragmentViewModel

class RegisterSupervisorFragment : androidx.fragment.app.Fragment() {
    private var checkStation:Boolean? = false
    private var altaSupervisorResponse = AltaUsuarioSupervisorResponse()
    private var busquedazonasResponse = BusquedaZonaResponse()
    private var registerSupervisorViewModel = RegisterSupervisorFragmentViewModel()
    private var mListener: OnFragmentInteractionListener? = null
    private var mainInterface: MainInterface? = null
    lateinit var myContext: Activity
    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    companion object {
        lateinit var imageModelArrayList: ArrayList<SearchModel>
        var itemsSeleccionados = ArrayList<Int>()
        var dataAfterZones = false
        var supernameAfterZones: String? = null
        var superApPaternoAfterZones: String? = null
        var superApMaternoAfterZones: String? = null
        var superEmailAfterZones: String? = null
    }
    private lateinit var binding : FragmentRegisterSupervisorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myContext = requireActivity()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterSupervisorBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onBack("backButonRegisteMethod")
        initView()
        addObserver()
        loadServices()
        listensButtons()
        showTextZones()
    }
    private fun loadServices() {
        val busquedaZonaRequest = BusquedaZonaRequest(idUsuario = User.idUsuario)
        registerSupervisorViewModel.getBusquedaZonasSuperService(busquedaZonaRequest)
    }
    private fun listensButtons() {
        binding.zonasSeleccinadas.setOnClickListener {
            loadZonasSelected()
        }
        binding.btnSaveSupervisor.setOnClickListener {
            val checkFields = checkFields(binding.supername.text.toString(), binding.superApPaterno.text.toString(), binding.superEmail.text.toString())
            if (checkFields == "OK") {
                val request = requestAltaUsuarioSuper()
                registerSupervisorViewModel.getAltaUsuarioSuperService(request)
            } else {
                Toast.makeText(activity, checkFields, Toast.LENGTH_LONG).show()
            }
        }
        binding.statusEstacion.setOnClickListener {
            checkStation = binding.statusEstacion.isChecked
        }
        binding.btnRegisterSuper.setOnClickListener {
            val registrosObtenidos = DBDemo(requireActivity()).getNumTotalRegistrosEmployeeSupervisor()
            if (registrosObtenidos > 0) {
            } else {
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.Fragpricipal, ListaSupervisorFragment(), "WelcomeFragment").commit()
            }
        }
    }
    private fun showTextZones() {
        if (SelectZonesFragment.nItems.size == 0) {
            binding.zonasSeleccinadas.setText("")
        } else {
            binding.zonasSeleccinadas.setText("${SelectZonesFragment.nItems.size} ${getString(R.string.side_menu_title_2)}")
        }
    }
    private fun addObserver() {
        registerSupervisorViewModel.altaUsuarioSuperResponseMutableData.observe(viewLifecycleOwner) {
            altaSupervisorResponse = it
            if (altaSupervisorResponse.codigo == "ET000") {
                successAltaUsuarioSuper()
            } else {
                Utilities.loadMessageError(altaSupervisorResponse.codigo, requireActivity(), childFragmentManager)
            }
        }
        registerSupervisorViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        registerSupervisorViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
        registerSupervisorViewModel.busquedaZonaResponseMutableLiveData.observe(viewLifecycleOwner) {
            busquedazonasResponse = it
        }
    }
    private fun successAltaUsuarioSuper() {
        val intent = Intent(requireActivity(), AnimotionLottie::class.java)
        intent.putExtra("Redirect", "ListSupervisor")
        startActivity(intent)
    }
    private fun loadZonasSelected() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogCustomList)
        val bind: FragmentSelectZonesBinding = FragmentSelectZonesBinding.inflate(layoutInflater)
        bind.searchTextField.isVisible = true
        bind.etFilter.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()){
                filterZones(text.toString(), bind.reciclerZones)
            }else{
                bind.reciclerZones.adapter = SimpleAdapterZonesSelect(requireActivity(), imageModelArrayList)
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
        for (i in 0 until busquedazonasResponse.totalRegistros) {
            val dataModel = SearchModel()
            dataModel.setIdd(busquedazonasResponse.sZona!![i].idZona.toString())
            dataModel.setNames(busquedazonasResponse.sZona!![i].descripcion)
            dataModel.setStatuss(busquedazonasResponse.sZona!![i].estatus)
            list.add(dataModel)
        }
        imageModelArrayList = list
        val simpleAdapter = SimpleAdapterZonesSelect(requireActivity().applicationContext, imageModelArrayList)
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
        for (item in imageModelArrayList) {
            if (!item.name.isNullOrEmpty()){
                if (item.name!!.lowercase().contains(text.lowercase())) {
                    filteredlist.add(item)
                }
            }

        }
        if (filteredlist.isEmpty()) {
            reciclerZones.adapter = SimpleAdapterZonesSelect(requireActivity(), arrayListOf())
        } else {
            reciclerZones.adapter = SimpleAdapterZonesSelect(requireActivity(), filteredlist)
        }
    }
    private fun loadSelected() {
        when (itemsSeleccionados.size) {
            0 -> {
                binding.zonasSeleccinadas.setText(" " + getString(R.string.none_select))
            }
            1 -> {
                binding.zonasSeleccinadas.setText(itemsSeleccionados.size.toString() + " " + getString(R.string.one_select))
            }
            else -> {
                binding.zonasSeleccinadas.setText(itemsSeleccionados.size.toString() + " " + getString(R.string.many_select))
            }
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
    fun initView() {
        if (dataAfterZones) {
            binding.supername.setText(supernameAfterZones)
            binding.superApPaterno.setText(superApPaternoAfterZones)
            binding.superApMaterno.setText(superApMaternoAfterZones)
            binding.superEmail.setText(superEmailAfterZones)
        }
    }
    private fun requestAltaUsuarioSuper(): AltaUsuarioSupervisorRequest {
        var request = AltaUsuarioSupervisorRequest(nombre = binding.supername.text.toString(), claveCia = User.claveCia, email = binding.superEmail.text.toString(), apellidoPat = binding.superApPaterno.text.toString(), apellidoMat = binding.superApMaterno.text.toString(), zonas = itemsSeleccionados,estaciones = checkStation!!)
        if (binding.superApMaterno.text.toString().isEmpty() && itemsSeleccionados.size == 0) {
            request = AltaUsuarioSupervisorRequest(nombre = binding.supername.text.toString(), claveCia = User.claveCia, email = binding.superEmail.text.toString(), apellidoPat = binding.superApPaterno.text.toString(), apellidoMat = null,estaciones = checkStation!!)
        }
        if (binding.superApMaterno.text.toString().isEmpty() && itemsSeleccionados.size > 0) {
            request = AltaUsuarioSupervisorRequest(nombre = binding.supername.text.toString(), claveCia = User.claveCia, email = binding.superEmail.text.toString(), apellidoPat = binding.superApPaterno.text.toString(), apellidoMat = null, zonas = itemsSeleccionados, estaciones = checkStation!!)
        }
        return request
    }
    private fun checkFields(nombreSuper: String, apPaterno: String, correo: String): String {
        if (nombreSuper.isEmpty() || nombreSuper.isEmpty()) {
            return getString(R.string.error_supervisor_data_name_empty)
        }
        if (apPaterno.isEmpty() || apPaterno.isBlank()) {
            return getString(R.string.error_supervisor_data_appat_empty)
        }
        if (binding.superEmail.text.toString().isEmpty() || binding.superEmail.text.toString().isBlank()) {
            return getString(R.string.error_admin_correo_empty)
        }
        if (!correo.contains('@')) {
            binding.superEmail.text
            binding.superEmail.setText("")
            return getString(R.string.a_login_error_email_valids)
        }
        return "OK"
    }
    fun showLoadingAnimation() {
        binding.loadingAnimationAltaSuper.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    fun hideLoadingAnimation() {
        binding.loadingAnimationAltaSuper.root.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.registersupervisor_title))
        if (AnimotionLottie.redirect == "ListSupervisor") {
            AnimotionLottie.redirect = ""
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.Fragpricipal, ListaSupervisorFragment(), "WelcomeFragment").commit()
        }
    }
    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = requireActivity().packageName
        val identificadorMensaje = this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(requireActivity().getString(identificadorMensaje), requireActivity(),childFragmentManager)
        } else {
            Utilities.showDialog(code.toString(), requireActivity(),childFragmentManager)
        }
    }
    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
}
