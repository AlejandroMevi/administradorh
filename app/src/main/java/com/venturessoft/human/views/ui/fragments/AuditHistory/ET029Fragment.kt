package com.venturessoft.human.views.ui.fragments.AuditHistory

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentET029Binding
import com.venturessoft.human.models.BeaconListModel
import com.venturessoft.human.models.response.itemAuditoriasDetalle
import com.venturessoft.human.views.adapters.AdapterBeaconList

class ET029Fragment : Fragment() {
    private var movimiento: String?=""
    private var adapter: AdapterBeaconList? = null
    private var auditoriaDet: ArrayList<itemAuditoriasDetalle>? = null
    private var mListener: OnFragmentInteractionListener? = null
    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    companion object {
        lateinit var beaconListModel: ArrayList<BeaconListModel>
    }
    private lateinit var binding : FragmentET029Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentET029Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }
    private fun loadMovimiento() {
        movimiento = if (auditoriaDet!![0].tipo == "E"){
            getString(R.string.checkin)
        }else{
            getString(R.string.checkout)
        }
    }

    private fun initView() {
        mListener?.onBack("ET029Fragment")
        loadData()
        listenButtons()
    }

    private fun listenButtons() {
        binding.btnBackET029.setOnClickListener {
            loadFragment(ListAudtHistoryFragment())
        }
    }

    private fun loadData() {
        auditoriaDet = ListAudtHistoryFragment.detallesResponse.auditoriaDet
        loadMovimiento()
        binding.usernameText.text = if (auditoriaDet!![0].nombreEmp.contains("null")) auditoriaDet!![0].nombreEmp.replace("null","") else auditoriaDet!![0].nombreEmp
        binding.movText.text = movimiento+ " "+ auditoriaDet!![0].fechaMov
        binding.txtDateCheck.text = auditoriaDet!![0].fechaChecada
        binding.idNumCompany.text = auditoriaDet!![0].numCia.toString()
        binding.idNumEmp.text = auditoriaDet!![0].numEmp.toString()
        binding.txtBeaconsCheck.text = auditoriaDet!![0].beacon01
        binding.device.text = auditoriaDet!![0].dispositivo

        if (auditoriaDet!![0].beacon01.isEmpty()){
            binding.imgBeacon.visibility = View.GONE
        }
        binding.txtET.text = "${auditoriaDet!![0].codigoEtime} : "
        binding.txtCodigo.text = txtCodigo(auditoriaDet!![0].codigoEtime)
        styleRecycler()
    }

    private fun styleRecycler() {
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_recicler)!!)
        binding.recyclerBeacons.addItemDecoration(itemDecorator)
        binding.recyclerBeacons.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireActivity())
        loadRecicler()
    }

    private fun loadRecicler() {
        val list = java.util.ArrayList<BeaconListModel>()
        for (i in 0 until auditoriaDet!![0].estaciones!!.size) {
            val beaconListModel = BeaconListModel()
            beaconListModel.setNamess(auditoriaDet!![0].estaciones?.get(i)!!.nombre)
            beaconListModel.setUuids(auditoriaDet!![0].estaciones?.get(i)!!.beacon)
            list.add(beaconListModel)
        }
        beaconListModel = list
        adapter = AdapterBeaconList(requireActivity().applicationContext, beaconListModel)
        binding.recyclerBeacons.adapter = adapter

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
    }

    private fun loadFragment(fragmet: Fragment) {
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, fragmet, "WelcomeFragment")
                .commit()
    }
    private fun txtCodigo(code: String): String {
        val contextoPaquete: String = requireActivity().packageName
        val identificadorMensaje = this.resources.getIdentifier(code, "string", contextoPaquete)
        return if (identificadorMensaje > 0) {
            getString(identificadorMensaje)
        } else {
            code
        }
    }

}