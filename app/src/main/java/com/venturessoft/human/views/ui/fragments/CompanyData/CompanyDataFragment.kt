package com.venturessoft.human.views.ui.fragments.CompanyData

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.venturessoft.human.R
import com.venturessoft.human.bd.BDPayments
import com.venturessoft.human.databinding.FragmentCompanyDataBinding
import com.venturessoft.human.models.request.ActualizaCompaniaRequest
import com.venturessoft.human.models.request.ConsultaCompaniaRequest
import com.venturessoft.human.models.response.ActualizaCompaniaResponse
import com.venturessoft.human.models.response.ActualizaFacturacionResponse
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.fragments.Administrator.AdminDataFragment
import com.venturessoft.human.views.ui.viewModels.CompanyDataFragmentViewModel

class CompanyDataFragment : Fragment() {
    private var companyDataViewModel = CompanyDataFragmentViewModel()
    private var actualizaCompaniaResponse = ActualizaCompaniaResponse()
    private var actualizaFacturacionResponse = ActualizaFacturacionResponse()
    private var mListener: OnFragmentInteractionListener? = null
    var id: String = ""
    private var codeCountry: String = ""
    private var enableUpdataFact = false
    private var claveCia: String = ""
    private var claveIdioma: String = ""
    private var cvePais: String = ""
    private var idUsuario: Int = 0
    private var nombreCia: String = ""
    private var rfc: String = ""
    private var razonSocial: String = ""
    private var maximoEmpleados: Int = 0
    private var mainInterface: MainInterface? = null

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    companion object {
        lateinit var myDataBase: BDPayments
    }
    private lateinit var binding: FragmentCompanyDataBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompanyDataBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun loadDefaultData() {
        if (arguments != null) {
            claveCia = requireArguments().getString("claveCia") ?: ""
            claveIdioma = requireArguments().getString("claveIdioma") ?: ""
            cvePais = requireArguments().getString("cvePais") ?: ""
            idUsuario = requireArguments().getInt("idUsuario")
            nombreCia = requireArguments().getString("nombreCia") ?: ""
            rfc = requireArguments().getString("rfc") ?: ""
            razonSocial = requireArguments().getString("razonSocial") ?: ""
            maximoEmpleados = requireArguments().getInt("maximoEmpleados")
        }
        binding.nameCompanyEdit.setText(nombreCia)
        binding.codeCompanyEdit.setText(claveCia)
        binding.bussinesNameEdit.setText(razonSocial)
        binding.employeNumbers.setText(maximoEmpleados.toString())
        binding.rfcEdit.setText(rfc)
        val rows = myDataBase.getPorIdUser(User.idUsuario.toString())

        while (rows.moveToNext()) {
            val countryDB: Int = rows.getString(3).toInt()
            if (countryDB == 62) {
                binding.rfcEdit.setText(getString(R.string.company_data_rfc_spain))
                binding.tilRfcEdit.hint = getString(R.string.company_data_rfc_spain)
            }
            if (countryDB == 28) {
                binding.rfcEdit.setText(getString(R.string.company_data_rfc_brasil))
                binding.tilRfcEdit.hint = getString(R.string.company_data_rfc_brasil)
            }
        }
        loadSpinnerCountries()
        binding.statusFotoLocal.isEnabled = false
        binding.tilNameCompanyEdit.isEnabled = false
        binding.tilCodeCompanyEdit.isEnabled = false
        binding.tilBussinesNameEdit.isEnabled = false
        binding.tilCountryCia.isEnabled = false
        binding.tilRfcEdit.isEnabled = false
        binding.tilEmployeNumbers.isEnabled = false
        if (User.scia!![0].fotoLocal == "S") binding.statusFotoLocal.isChecked = true
    }

    private fun initView() {
        myDataBase = BDPayments(requireActivity())
        mListener?.onBack("backButtonCompanyData")
        loadServices()
        addObservers()
        listenButtons()
        loadDataLoggedUser()
        loadDefaultData()
    }

    private fun addObservers() {
        companyDataViewModel.actualizarCompaniaResponseMutableData.observe(viewLifecycleOwner) {
            actualizaCompaniaResponse = it
            if (actualizaCompaniaResponse.codigo == "ET000") {
                successActualizaFacturacion()
            } else {
                Utilities.loadMessageError(
                    actualizaCompaniaResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        companyDataViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
        companyDataViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
    }

    private fun loadServices() {
        val requestConsultaCompania = ConsultaCompaniaRequest(User.idUsuario, User.scia!![0].cia)
        companyDataViewModel.getConsultaCompaniaData(requestConsultaCompania)
    }

    private fun loadSpinnerCountries() {
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, Utilities.countryList)
        binding.countryCia.setAdapter(adapter)
        binding.countryCia.setOnItemClickListener { _, _, position, _ ->
            codeCountry = Utilities.codeCountry[position]
            binding.statusFotoLocal.isChecked = codeCountry == "ES"
        }
        val item = Utilities.codeCountry.indexOf(cvePais)
        if (item > 0){
            binding.statusFotoLocal.isChecked = codeCountry == "ES"
            binding.countryCia.setText(Utilities.countryList[item],false)
        }else{
            binding.countryCia.setText(Utilities.countryList[0],false)
        }
    }
    private fun listenButtons() {
        binding.btnBackCompanyData.setOnClickListener {
            if (actualizaFacturacionResponse.codigo == "ET000") {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, AdminDataFragment(), "WelcomeFragment")
                    .commit()
            }
            if (!enableUpdataFact) {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, AdminDataFragment(), "WelcomeFragment")
                    .commit()
            } else {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, CompanyDataFragment(), "WelcomeFragment")
                    .commit()
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

    private fun loadDataLoggedUser() {


        binding.swEdit.setOnCheckedChangeListener { compoundButton, _ ->
            if (compoundButton.isChecked){
                if (User.scia!![0].fotoLocal == "S") binding.statusFotoLocal.isChecked = true
                binding.statusFotoLocal.isEnabled = true
                binding.tilNameCompanyEdit.isEnabled = true
                binding.tilCodeCompanyEdit.isEnabled = true
                binding.tilBussinesNameEdit.isEnabled = true
                binding.tilCountryCia.isEnabled = true
                binding.btnEdit.isVisible = true
            }else{
                binding.btnEdit.isVisible = false
                loadDefaultData()
            }
        }

        binding.btnEdit.setOnClickListener {
            val validateFields = checkFields(
                binding.nameCompanyEdit.text.toString(),
                binding.codeCompanyEdit.text.toString(),
                binding.bussinesNameEdit.text.toString(),
                binding.rfcEdit.text.toString(),
                binding.countryCia.text.toString(),
                binding.employeNumbers.text.toString(),
            )
            if (validateFields == "OK") {
                val requestActualizaCompania = ActualizaCompaniaRequest(
                    binding.nameCompanyEdit.text.toString(),
                    binding.codeCompanyEdit.text.toString(),
                    "EN",
                    binding.employeNumbers.text.toString().toInt(),
                    codeCountry,
                    binding.bussinesNameEdit.text.toString(),
                    if (binding.statusFotoLocal.isChecked) "S" else "N"
                )
                companyDataViewModel.getActualizarCompaniaService(requestActualizaCompania)
            } else {
                Toast.makeText(requireActivity(), validateFields, Toast.LENGTH_SHORT).show()
                binding.tilNameCompanyEdit.isEnabled = true
                binding.tilCodeCompanyEdit.isEnabled = true
                binding.tilBussinesNameEdit.isEnabled = true
                binding.tilCountryCia.isEnabled = true
            }
        }
    }

    fun checkFields(
        companykt: String,
        codeCompanykt: String,
        bussinesNamekt: String,
        rfckt: String,
        countrykt: String,
        numEmployes: String,
    ): String {
        if (companykt.isBlank()) {
            return getString(R.string.error_company_name_empty)
        }
        if (codeCompanykt.isBlank()) {
            return getString(R.string.error_company_code_company)
        }
        if (codeCompanykt.contains(" ")) {
            return getString(R.string.error_company_code_company)
        }
        if (bussinesNamekt.isBlank()) {
            return getString(R.string.error_company_bussines_empty)
        }
        if (rfckt.isBlank()) {
            return getString(R.string.error_company_rfc_empty)
        }
        if (numEmployes.isBlank()) {
            return getString(R.string.error_supervisor_data_employe_number_empty)
        }
        return "OK"
    }

    fun showLoadingAnimation() {
        binding.loadingAnimationCompanyEdit.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideLoadingAnimation() {
        binding.loadingAnimationCompanyEdit.root.visibility = View.GONE
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
            Utilities.showDialog(code.toString(), requireActivity(),childFragmentManager)
        }
    }

    private fun successActualizaFacturacion() {
        binding.statusFotoLocal.isEnabled = false
        binding.tilNameCompanyEdit.isEnabled = false
        binding.tilCodeCompanyEdit.isEnabled = false
        binding.tilBussinesNameEdit.isEnabled = false
        binding.tilCountryCia.isEnabled = false
        binding.tilRfcEdit.isEnabled = false
        binding.tilEmployeNumbers.isEnabled = false
        val dialogLogout = DialogGeneral(
            getString(R.string.good),
            getString(R.string.successful_change),
            requireContext().getString(R.string.ok)
        )
        dialogLogout.show(childFragmentManager,"")
    }

    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.data_company_form_title))
    }
    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
}