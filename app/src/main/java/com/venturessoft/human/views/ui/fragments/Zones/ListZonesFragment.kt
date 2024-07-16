package com.venturessoft.human.views.ui.fragments.Zones

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.venturessoft.human.R
import com.venturessoft.human.databinding.DialogNewCategoryBinding
import com.venturessoft.human.databinding.FragmentZonesListBinding
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.models.request.ActualizaZonaRequest
import com.venturessoft.human.models.request.AltaZonaRequest
import com.venturessoft.human.models.request.BusquedaZonaRequest
import com.venturessoft.human.models.request.EliminaZonaRequest
import com.venturessoft.human.models.response.ActualizaZonaResponse
import com.venturessoft.human.models.response.AltaZonasResponse
import com.venturessoft.human.models.response.BusquedaZonaResponse
import com.venturessoft.human.models.response.ConsultaZonaResponse
import com.venturessoft.human.models.response.EliminaZonaResponse
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.bd.DBDemo
import com.venturessoft.human.views.adapters.SimpleAdapter
import com.venturessoft.human.views.ui.activities.AnimotionLottie
import com.venturessoft.human.views.ui.activities.SwipeToDeleteCallback
import com.venturessoft.human.views.ui.fragments.welcome.UpdateEnterpriceFragment
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import com.venturessoft.human.views.ui.viewModels.ListZonesFragmentViewModel

class ListZonesFragment : DialogFragment() {
    private var mListener: OnFragmentInteractionListener? = null
    private var listZonesFragmentViewModel = ListZonesFragmentViewModel()
    private var busquedaZonaResponse = BusquedaZonaResponse()
    private var altaZonaResponse = AltaZonasResponse()
    private var actualizaZonaResponse = ActualizaZonaResponse()
    private var consultaZonaResponse = ConsultaZonaResponse()
    private var mainInterface: MainInterface? = null
    var adapter: SimpleAdapter? = null

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    companion object {
        var eliminaZonaResponse = EliminaZonaResponse()
        var description: String = ""
        var idZona: Int? = 0
        var newEstatus: String = ""
        lateinit var imageModelArrayList: ArrayList<SearchModel>
        var idEdit: String = ""
        lateinit var myDataBase: DBDemo
        lateinit var myContext: Activity
        var descriptionN: String = ""
    }

    private lateinit var binding: FragmentZonesListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDataBase = DBDemo(requireActivity())
        myContext = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentZonesListBinding.inflate(inflater, container, false)
        myContext = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenButton()
        addObservers()
        loadRequestZones()
    }

    private fun loadRequestZones() {
        val busquedaZonaRequest = BusquedaZonaRequest(idUsuario = User.idUsuario)
        listZonesFragmentViewModel.getBusquedaZonas(busquedaZonaRequest)
    }

    private fun freeTrial() {
        if (busquedaZonaResponse.sZona!!.size >= 1) {
            binding.linearFreeTrial.cardFree.isVisible = User.freeTrial
            binding.linearFreeTrial.actualizarAhora.setOnClickListener {
                val modalBottomSheet = UpdateEnterpriceFragment()
                modalBottomSheet.show(childFragmentManager, "ModalBottomSheet.TAG")
            }
        }
    }

    private fun listenButton() {
        mListener?.onBack("backMenu")
        binding.btnBackMenu.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.Fragpricipal, WelcomeFragment(), "WelcomeFragment").commit()
        }
        binding.editZoneList.setOnClickListener {
            loadDialog("editZones")
        }
        binding.addZonee.setOnClickListener {
            if (User.freeTrial) {
                if (busquedaZonaResponse.sZona?.size == null || busquedaZonaResponse.sZona.isNullOrEmpty()) {
                    loadDialog("addZones")
                } else {
                    if (busquedaZonaResponse.sZona!!.size >= 1) {
                        val modalBottomSheet = UpdateEnterpriceFragment()
                        modalBottomSheet.show(childFragmentManager, "ModalBottomSheet.TAG")
                    } else {
                        loadDialog("addZones")
                    }
                }
            } else {
                loadDialog("addZones")
            }
        }
        binding.deleteZone.setOnClickListener {
            val request = EliminaZonaRequest(idUsuario = User.idUsuario, idZona = idZona!!)
            listZonesFragmentViewModel.getEliminaZona(request)
        }
        binding.editZoneStatus.setOnClickListener {
            val request = ActualizaZonaRequest(
                idUsuario = User.idUsuario,
                idZona = idZona!!,
                descripcion = description,
                estatus = newEstatus
            )
            listZonesFragmentViewModel.getActualizaZona(request)
        }

        binding.etFilter.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                val busquedaZonaRequest = BusquedaZonaRequest(idUsuario = User.idUsuario)
                listZonesFragmentViewModel.getBusquedaZonas(busquedaZonaRequest)
            } else {
                val busquedaZonaRequest =
                    BusquedaZonaRequest(idUsuario = User.idUsuario, filtroZona = text.toString())
                listZonesFragmentViewModel.getBusquedaZonas(busquedaZonaRequest)
            }
        }
    }

    private fun loadDialog(nameButton: String) {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogCustomList)
        val bind: DialogNewCategoryBinding = DialogNewCategoryBinding.inflate(layoutInflater)
        dialog.setView(bind.root)
        dialog.setCancelable(false)
        dialog.create()
        val progressDialog: AlertDialog = dialog.create()
        if (nameButton == "editZones") {
            bind.titleZones.text = getString(R.string.zones_edy)
            bind.btnAdd.text = getString(R.string.edit)
            bind.descriptionZones.setText(descriptionN)
        }
        bind.btnCancel.setOnClickListener {
            progressDialog.dismiss()
        }
        bind.btnAdd.setOnClickListener {
            when (nameButton) {
                "addZones" -> {
                    if (bind.descriptionZones.text.toString().isNotEmpty() && bind.descriptionZones.text.toString().isNotBlank()) {
                        progressDialog.dismiss()
                        val request = AltaZonaRequest(User.idUsuario, bind.descriptionZones.text.toString())
                        listZonesFragmentViewModel.getAltaZona(request)
                    } else {
                        makeText(requireActivity(), getString(R.string.error_zone_data), LENGTH_SHORT).show()
                    }
                }
                "editZones" -> {
                    if (bind.descriptionZones.text.toString().isNotEmpty() && bind.descriptionZones.text.toString().isNotBlank()) {
                        val request = ActualizaZonaRequest(
                            idUsuario = User.idUsuario,
                            idZona = idEdit.toInt(),
                            descripcion = bind.descriptionZones.text.toString()
                        )
                        listZonesFragmentViewModel.getActualizaZona(request)
                        progressDialog.dismiss()
                        succcessZones()
                    } else {
                        makeText(requireActivity(), "Nombre invalido", LENGTH_SHORT).show()
                    }
                }
            }
        }
        try {
            progressDialog.show()
        } catch (e: IllegalStateException) {
            (bind.root.parent as ViewGroup).removeView(bind.root)
            progressDialog.show()
        }
    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.list_zones_title))
        if (AnimotionLottie.redirect == "ListZones") {
            AnimotionLottie.redirect = ""
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, ListZonesFragment(), "WelcomeFragment")
                .commit()
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
    fun eliminarZona(id: String) {
        idZona = id.toInt()
        val btnDeleteZona = myContext.findViewById<Button>(R.id.deleteZone)
        btnDeleteZona.callOnClick()
    }
    private fun editAllZones(id: String, descriptionName: String) {
        idEdit = id
        descriptionN = descriptionName
        val btnEdit = myContext.findViewById<ImageView>(R.id.editZoneList)
        btnEdit.callOnClick()
    }
    private fun addObservers() {
        listZonesFragmentViewModel.busquedaZonaResponseMutableData.observe(this) {
            busquedaZonaResponse = it
            if (busquedaZonaResponse.codigo == "ET000") {
                busquedaZonaResponse
                succcessZones()
                freeTrial()
            } else if (busquedaZonaResponse.codigo != "ET327") {
                Utilities.loadMessageError(
                    busquedaZonaResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            } else {
                if (adapter != null) {
                    adapter!!.refreshRecicler()
                }
                makeText(
                    requireActivity(),
                    "No hay ninguna zona con la letra seleccionada",
                    LENGTH_SHORT
                ).show()
            }
        }
        listZonesFragmentViewModel.altaZonaResponseMutableData.observe(this) {
            altaZonaResponse = it
            if (altaZonaResponse.codigo == "ET000") {
                val intent = Intent(requireActivity(), AnimotionLottie::class.java)
                intent.putExtra("Redirect", "ListZone")
                startActivity(intent)
            } else {
                loadMessageError(altaZonaResponse.codigo)
                succcessZones()
            }
        }
        listZonesFragmentViewModel.actualizaZonaResponseMutableData.observe(this) {
            actualizaZonaResponse = it
            if (actualizaZonaResponse.codigo == "ET000") {
                successUpdateZone()
            } else {
                loadMessageError(actualizaZonaResponse.codigo)
                succcessZones()
            }
        }
        listZonesFragmentViewModel.eliminaZonaResponseMutableData.observe(this) {
            eliminaZonaResponse = it
            if (eliminaZonaResponse.codigo == "ET000") {
                successDeleteZone()
                succcessZones()
            } else {
                loadMessageError(eliminaZonaResponse.codigo)
                succcessZones()
            }
        }
        listZonesFragmentViewModel.consultaZonaResponseMutableData.observe(this) {
            consultaZonaResponse = it
            if (consultaZonaResponse.codigo == "ET000") {
                queryZone()

            } else {
                loadMessageError(consultaZonaResponse.codigo)
            }
        }
        listZonesFragmentViewModel.isLoading.observe(this) {
            if (it) {
                showLoadingAnimation()

            } else {
                hideLoadingAnimation()
            }
        }
        listZonesFragmentViewModel.codeError.observe(this) {
            loadServerMessageError(it)
        }
    }
    private fun queryZone() {
        succcessZones()
    }
    private fun succcessZones() {
        if (busquedaZonaResponse.sZona != null) {
            loadDataRecicler()
        } else {
            binding.addZonee.callOnClick()
        }
    }
    private fun loadMessageError(code: String) {
        val contextoPaquete: String = requireActivity().packageName
        val indentificadorMensaje = requireActivity().applicationContext.resources.getIdentifier(code, "string", contextoPaquete)
        if (adapter != null) {
            adapter!!.refreshRecicler()
        }
        if (indentificadorMensaje > 0) {
            val dialogLogout = DialogGeneral(getString(R.string.advetencia), getString(indentificadorMensaje), getString(R.string.ok))
            dialogLogout.show(childFragmentManager, "")
        } else {
            val dialogLogout = DialogGeneral(getString(R.string.advetencia), code, getString(R.string.ok), null, {
                requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.Fragpricipal, ListZonesFragment(), "WelcomeFragment").commit()
                }
            )
            dialogLogout.show(childFragmentManager, "")
        }
    }
    private fun loadDataRecicler() {
        val list = java.util.ArrayList<SearchModel>()
        for (i in 0 until busquedaZonaResponse.sZona!!.size) {
            val dataModel = SearchModel()
            dataModel.setIdd(busquedaZonaResponse.sZona!![i].idZona.toString())
            dataModel.setNames(busquedaZonaResponse.sZona!![i].descripcion)
            dataModel.setStatuss(busquedaZonaResponse.sZona!![i].estatus)
            list.add(dataModel)
        }
        imageModelArrayList = list
        adapter = SimpleAdapter(requireActivity().applicationContext, imageModelArrayList)
        binding.reciclerZones.adapter = adapter
        binding.reciclerZones.addOnItemTouchListener(
            RecyclerTouchListener(
                requireActivity().applicationContext,
                binding.reciclerZones,
                object : ClickListener {
                    override fun onClick(view: View, position: Int) {}
                    override fun onLongClick(view: View?, position: Int) {
                    }
                }
            )
        )
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter!!.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.reciclerZones)
    }
    interface ClickListener {
        fun onClick(view: View, position: Int)
        fun onLongClick(view: View?, position: Int)
    }
    internal class RecyclerTouchListener(context: Context, recyclerView: RecyclerView, private val clickListener: ClickListener?) : RecyclerView.OnItemTouchListener {
        private val gestureDetector: GestureDetector
        init {
            gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }
                    override fun onLongPress(e: MotionEvent) {
                        val child = recyclerView.findChildViewUnder(e.x, e.y)
                        if (child != null && clickListener != null) {
                            clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                        }
                    }
                }
            )
        }
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }
        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        }
    }
    private fun successDeleteZone() {
        val dialogLogout = DialogGeneral(
            getString(R.string.good),
            getString(R.string.successful_change),
            getString(R.string.ok),
            null,
            {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.Fragpricipal, ListZonesFragment(), "WelcomeFragment").commit()
            }
        )
        dialogLogout.show(childFragmentManager, "")
    }
    private fun successUpdateZone() {
        val dialogLogout = DialogGeneral(
            getString(R.string.error),
            getString(R.string.successful_change),
            null,
            null,
            {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.Fragpricipal, ListZonesFragment(), "WelcomeFragment").commit()
            },
            {
                queryZone()
            }
        )
        dialogLogout.show(childFragmentManager, "")
    }
    fun editZone(idZone: Int, nameZone: String) {
        editAllZones(idZone.toString(), nameZone)
    }
    fun editZoneStatuss(id: Int, estatus: String, descriptionD: String) {
        newEstatus = estatus
        idZona = id
        description = descriptionD
        val editStatus = myContext.findViewById<Button>(R.id.editZoneStatus)
        editStatus.callOnClick()
    }
    fun showLoadingAnimation() { binding.loadingAnimationZoneFragment.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }
    fun hideLoadingAnimation() { binding.loadingAnimationZoneFragment.root.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = requireActivity().packageName
        val indentificadorMensaje =
            this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        val dialogLogout =
            DialogGeneral(getString(R.string.error), getString(indentificadorMensaje), null, null, {
                if (adapter != null) {
                    adapter!!.refreshRecicler()
                }
            }
            )
        dialogLogout.show(childFragmentManager, "")
    }
    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
}
