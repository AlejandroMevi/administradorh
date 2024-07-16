package com.venturessoft.human.views.ui.fragments.AuditHistory

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentItemFailsGeneralErrorBinding
import com.venturessoft.human.models.response.itemAuditoriasDetalle
import com.venturessoft.human.utils.Utilities

class ItemFailsGeneralErrorFragment : Fragment() {
    private var photoCheck: String?=""
    private var movimiento: String? = ""
    private var mListener: OnFragmentInteractionListener? = null
    private var auditoriaDet: ArrayList<itemAuditoriasDetalle>? = null

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    private lateinit var binding : FragmentItemFailsGeneralErrorBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentItemFailsGeneralErrorBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun initView() {
        mListener?.onBack("FailsGeneralErrorFragment")
        loadData()
        listenButtons()
    }
    private fun listenButtons() {
        binding.btnBackFailGeneralError.setOnClickListener {
            loadFragment(ListAudtHistoryFragment())
        }
        binding.employePhoto.setOnClickListener {
            if (photoCheck!!.isNotEmpty()){
                viewImageD(photoCheck!!)

            }else{
                viewImageD(null)
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun loadData() {
        auditoriaDet = ListAudtHistoryFragment.detallesResponse.auditoriaDet
        loadMovimiento()
        binding.usernameText.text = if (auditoriaDet!![0].nombreEmp.contains("null")) auditoriaDet!![0].nombreEmp.replace("null","") else auditoriaDet!![0].nombreEmp
        binding.movText.text = movimiento + " " + auditoriaDet!![0].fechaMov
        binding.txtDateCheck.text =  auditoriaDet!![0].fechaChecada
        binding.idNumCompany.text = auditoriaDet!![0].numCia.toString()
        binding.idNumEmp.text = auditoriaDet!![0].numEmp.toString()
        binding.txtET032.text = "${auditoriaDet!![0].codigoEtime} :"
        binding.txtCodigo.text = txtCodigo(auditoriaDet!![0].codigoEtime)
        binding.device.text = auditoriaDet!![0].dispositivo
        photoCheck = auditoriaDet!![0].fotografia
        loadPhoto()
    }

    private fun loadPhoto() {
        val bitmap = Utilities.Base64StringToBitmap(auditoriaDet!![0].fotografia)
        binding.employePhoto.setImageBitmap(bitmap)
    }

    private fun loadMovimiento() {
        movimiento = if (auditoriaDet!![0].tipo == "E") {
            getString(R.string.checkin)
        } else {
            getString(R.string.checkout)
        }
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
    private fun viewImageD(photo: String?) {
        val fullScreenDialogFragment = ViewImageFragment(photo)
        fullScreenDialogFragment.show(
            requireActivity().supportFragmentManager,
            "FullScreenDialogFragment"
        )
    }
}