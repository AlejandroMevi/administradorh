package com.venturessoft.human.views.ui.fragments.createAccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentDataCompanyBinding
import com.venturessoft.human.models.request.AltaCompaniaRequest
import com.venturessoft.human.models.request.AltaUsuarioAdminRequest
import com.venturessoft.human.models.request.ConsultaTokeNoLoginRequest
import com.venturessoft.human.models.response.AltaCompaniaResponse
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.bd.DBDemo
import com.venturessoft.human.views.ui.activities.AnimotionLottie
import com.venturessoft.human.views.ui.viewModels.CreateAccountViewModel

class DataCompanyFragment : Fragment() {

    private lateinit var binding: FragmentDataCompanyBinding
    private val vmCreateAccount: CreateAccountViewModel by activityViewModels()
    private var altaCompaniaResponse = AltaCompaniaResponse()
    lateinit var myDataBase: DBDemo
    private var nameCompanykt: String = ""
    private var codeCompanykt: String = ""
    private var bussinesNamekt: String = ""
    private var employeNumber: String = ""
    private var maxEmployees: Int = 0
    private var rfckt: String = ""
    private var countrykt: String = ""
    private var userData = AltaUsuarioAdminRequest()
    private var mainInterface: CreateAccountInterface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDataCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDataBase = DBDemo(requireContext())
        loadToken(false)
        loadCountry()
        btnSave()
        vmCreateAccount.dataUser.observe(viewLifecycleOwner){
            if (it != null){
                userData = it
            }
        }
        vmCreateAccount.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                mainInterface?.showLoading(true)
            } else {
                mainInterface?.showLoading(false)
            }
        }
    }

    private fun addObserver() {
        vmCreateAccount.altaCompaniaResponseMutableData.observe(viewLifecycleOwner) {
            if (it != null){
                altaCompaniaResponse = it
                if (altaCompaniaResponse.codigo == "ET000") {
                    val intent = Intent(requireContext(), AnimotionLottie::class.java)
                    intent.putExtra("email", userData.email)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    loadMessageError(altaCompaniaResponse.codigo)
                }
                vmCreateAccount.altaCompaniaResponseMutableData.removeObservers(viewLifecycleOwner)
                vmCreateAccount.altaCompaniaResponseMutableData.value = null
            }
        }
        vmCreateAccount.codeError.observe(viewLifecycleOwner) {
            if (it!=null){
                loadServerMessageError(it)
            }
        }
    }

    private fun setData() {
        nameCompanykt = binding.nameCompany.text.toString()
        codeCompanykt = binding.codeCompany.text.toString()
        bussinesNamekt = binding.bussinesName.text.toString()
        employeNumber = binding.employeNumbers.text.toString()
        rfckt = binding.rfc.text.toString()
        maxEmployees = if (binding.employeNumbers.text.isNullOrEmpty()) 0 else binding.employeNumbers.text.toString().toInt()
    }

    private fun btnSave() {
        binding.btnSaveAdmin.setOnClickListener {
            setData()
            val checkFields = checkFields(nameCompanykt, codeCompanykt.replace("\\s".toRegex(), ""), bussinesNamekt, rfckt, employeNumber)
            if (checkFields == "OK") {
                if (User.token.isEmpty()){
                    loadToken(true)
                }else{
                    funAltaUsuarioService()
                }
            } else {
                Toast.makeText(requireContext(), checkFields, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun funAltaUsuarioService(){
        vmCreateAccount.getAltaUsuarioService(userData)
        vmCreateAccount.altaUsuarioAdminResponseMutableData.observe(viewLifecycleOwner){
            if(it != null){
                if (it.codigo == "ET000" || it.codigo == "ET220" && it.idUsuario>0){
                    val request = AltaCompaniaRequest(
                        idUsuario = it.idUsuario,
                        nombreCia = nameCompanykt,
                        claveCia = codeCompanykt,
                        claveIdioma = "ES",
                        rfc = rfckt,
                        razonSocial = bussinesNamekt,
                        clavePais = countrykt,
                        maximoEmpleados = maxEmployees,
                        fotoLocal = if (binding.statusFotoLocal.isChecked) "S" else "N"
                    )
                    vmCreateAccount.getAltaCompaniaService(request)
                    addObserver()
                }else{
                    loadMessageError(it.codigo)
                }
                vmCreateAccount.altaUsuarioAdminResponseMutableData.removeObservers(viewLifecycleOwner)
                vmCreateAccount.altaUsuarioAdminResponseMutableData.value = null
            }
        }
    }

    private fun checkFields(nameCompany: String, codeCompany: String, bussinesName: String, rfc: String, employeNumber: String): String {
        if (nameCompany.isBlank()) {
            return getString(R.string.error_company_name_empty)
        }
        if (codeCompany.isBlank()) {
            return getString(R.string.error_company_code_company)
        }
        if (codeCompany.contains(" ")) {
            return getString(R.string.error_company_code_company)
        }
        if (bussinesName.isBlank()) {
            return getString(R.string.error_company_bussines_empty)
        }
        if (rfc.isBlank()) {
            return getString(R.string.error_company_rfc_empty)
        }
        if (employeNumber.isEmpty()){
            return getString(R.string.error_supervisor_data_employe_number_empty)
        }
        return "OK"
    }

    private fun loadCountry() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, Utilities.countryList)
        binding.country.setAdapter(adapter)
        countrykt = Utilities.codeCountry[0]
        binding.country.setOnItemClickListener { parent, _, position, _ ->
            val posicionPais = Utilities.countryList.indexOf(parent.getItemAtPosition(position).toString())
            countrykt = Utilities.codeCountry[posicionPais]
            if (binding.statusFotoLocal.isChecked){
            }else{
                binding.statusFotoLocal.isChecked = countrykt == "ES"
            }
        }
    }

    private fun loadMessageError(code: String) {
        val contextoPaquete: String = requireContext().packageName
        val identificadorMensaje = this.resources.getIdentifier(code, "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(getString(identificadorMensaje), requireActivity(), childFragmentManager)
        } else {
            Utilities.showDialog("" + code, requireActivity(), childFragmentManager)
        }
    }

    private fun loadServerMessageError(code: Int) {
        val contextoPaquete: String = requireContext().packageName
        val indentificadorMensaje = this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        Utilities.showDialog(this.getString(indentificadorMensaje), requireContext(),childFragmentManager)
    }

    private fun loadToken(startService:Boolean) {
        val requestConsultaTokenNoLogin = ConsultaTokeNoLoginRequest()
        vmCreateAccount.getConsultaTokenNoLogin(requestConsultaTokenNoLogin)
        if (startService){
            funAltaUsuarioService()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CreateAccountInterface) {
            mainInterface = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }

}