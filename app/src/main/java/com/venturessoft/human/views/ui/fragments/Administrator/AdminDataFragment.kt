package com.venturessoft.human.views.ui.fragments.Administrator

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentEditAdministratorBinding
import com.venturessoft.human.models.request.ActualizarUsuarioRequest
import com.venturessoft.human.models.request.ConsultaCompaniaRequest
import com.venturessoft.human.models.response.ActualizarUsuarioResponse
import com.venturessoft.human.models.response.ConsultaCompaniaResponse
import com.venturessoft.human.utils.AlgorithmAdapter
import com.venturessoft.human.utils.CountriesNumber
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.fragments.CompanyData.CompanyDataFragment
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import com.venturessoft.human.views.ui.viewModels.AdminDataFragmentViewModel

class AdminDataFragment : Fragment() {
    private var mListener: OnFragmentInteractionListener? = null
    private var adminDataFragmentViewModel = AdminDataFragmentViewModel()
    private var actualizaUsuarioResponse = ActualizarUsuarioResponse()
    private var consultaCompaniaResponse = ConsultaCompaniaResponse()
    private var codeEditEmployeText: String = "+52"
    private var edit_email: Boolean? = false
    private var nip: String? = ""
    private var mainInterface: MainInterface? = null
    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    private lateinit var binding : FragmentEditAdministratorBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEditAdministratorBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBack()
        mListener?.onBack("backAdmin")
        loadData()
        listensButtons()
        addObserver()
    }
    private fun listensButtons() {
        binding.swEdit.setOnCheckedChangeListener { compoundButton, _ ->
            if (compoundButton.isChecked){
                binding.tilNameAdmin.isEnabled = true
                binding.tilApPaterno.isEnabled = true
                binding.tilApMaterno.isEnabled = true
                binding.tilPhoneCodeSpinnerAdmin.isEnabled = true
                binding.tilPhoneNumberAdminn.isEnabled = true
                binding.tilEmailAdminData.isEnabled = true
                binding.btnEdit.isVisible = true
            }else{
                binding.btnEdit.isVisible = false
                loadData()
            }
        }

        binding.btnEdit.setOnClickListener {
            val checkFields = checkFields(
                binding.nameAdmin.text.toString(),
                binding.apPaterno.text.toString(),
                binding.emailAdminData.text.toString()
            )
            if (checkFields == "OK") {
                when (edit_email) {
                    true -> {
                        loadDialogUserPassword()
                    }
                    false -> {
                        val requestActualizaUsuario = requestActualizaUsuario()
                        adminDataFragmentViewModel.getActualizarUsuarioService(requestActualizaUsuario)
                    }
                    else -> {}
                }
            } else {
                Toast.makeText(requireActivity(), checkFields, Toast.LENGTH_SHORT).show()
                binding.emailAdminData.isEnabled = true
                binding.nameAdmin.isEnabled = true
                binding.apPaterno.isEnabled = true
                binding.apMaterno.isEnabled = true
                binding.phoneNumberAdminn.isEnabled = true
            }
        }

        binding.btnNextDataCompany.setOnClickListener {
            val requestConsultaCompania = ConsultaCompaniaRequest(User.idUsuario, User.scia!![0].cia)
            adminDataFragmentViewModel.getConsultaCompaniaData(requestConsultaCompania)
        }
    }
    private fun loadDialogUserPassword() {
        val dialogLayout = requireActivity().layoutInflater.inflate(R.layout.dialog_user_password_admin, null)
        val dialogPositiveButton = dialogLayout.findViewById<Button>(R.id.dialogPositiveButton)
        val dialogNegativeButton = dialogLayout.findViewById<Button>(R.id.dialogNegativeButton)
        val tvPassword = dialogLayout.findViewById<TextView>(R.id.edtPassword)
        val dialogConfirmPassword = Dialog(requireActivity())
        dialogConfirmPassword.setContentView(dialogLayout)
        dialogConfirmPassword.setCancelable(false)
        dialogConfirmPassword.show()
        dialogConfirmPassword.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogPositiveButton.setOnClickListener {
            val tvNip = tvPassword.text.toString().trim()
            if (validPassword(nip = tvNip)) {
                nip = tvNip
                val requestActualizaUsuario = requestActualizaUsuario()
                adminDataFragmentViewModel.getActualizarUsuarioService(requestActualizaUsuario)
                dialogConfirmPassword.dismiss()
            }
        }
        dialogNegativeButton.setOnClickListener {
            binding.emailAdminData.setText(User.email)
            Toast.makeText(requireContext(),getString(R.string.error_new_nip_empty),Toast.LENGTH_SHORT).show()
            dialogConfirmPassword.dismiss()
        }
    }
    private fun validPassword(nip: String): Boolean {
        if (nip.isBlank()) {
            Toast.makeText(requireContext(), getString(R.string.error_new_nip_empty), Toast.LENGTH_SHORT).show()
        }
        return true
    }
    private fun addObserver() {
        adminDataFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
        adminDataFragmentViewModel.actualizarUsuarioResponseMutableData.observe(viewLifecycleOwner) {
            actualizaUsuarioResponse = it
            if (actualizaUsuarioResponse.codigo == "ET000") {
                successUserUpdate()
            } else {
                binding.emailAdminData.setText(User.email)
                Utilities.loadMessageError(
                    actualizaUsuarioResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        adminDataFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        adminDataFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
        adminDataFragmentViewModel.consultaCompaniaResponseMutableData.observe(viewLifecycleOwner) {
            consultaCompaniaResponse = it
            if (consultaCompaniaResponse.codigo == "ET000") {
                successConsultaFacturacion()
            } else {
                Utilities.loadMessageError(
                    consultaCompaniaResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
    }

    private fun successConsultaFacturacion() {
        val companyDataFragment = CompanyDataFragment()
        val dataContainer = Bundle()
        dataContainer.putString("claveCia", consultaCompaniaResponse.cia!![0].claveCia)
        dataContainer.putString("claveIdioma", consultaCompaniaResponse.cia!![0].claveIdioma)
        dataContainer.putString("cvePais", consultaCompaniaResponse.cia!![0].clavePais)
        dataContainer.putInt("idUsuario", consultaCompaniaResponse.cia!![0].idCompania)
        dataContainer.putString("nombreCia", consultaCompaniaResponse.cia!![0].nombreCia)
        dataContainer.putString("rfc", consultaCompaniaResponse.cia!![0].rfc)
        dataContainer.putString("razonSocial", consultaCompaniaResponse.cia!![0].razonSocial)
        dataContainer.putInt("maximoEmpleados", consultaCompaniaResponse.cia!![0].maximoEmpleados)
        companyDataFragment.arguments = dataContainer
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.Fragpricipal, companyDataFragment, "WelcomeFragment")
            .commit()
    }
    private fun successUserUpdate() {
        User.nombre = binding.nameAdmin.text.toString()
        User.apellidoPat = binding.apPaterno.text.toString()
        User.apellidoMat = binding.apMaterno.text.toString()
        User.telefono = binding.phoneNumberAdminn.text.toString()
        User.codigoTel = codeEditEmployeText
        User.email = binding.emailAdminData.text.toString()
        val dialogLogout = DialogGeneral(
            getString(R.string.good),
            getString(R.string.successful_change),
            getString(R.string.ok)
        )
        dialogLogout.show(childFragmentManager,"")
    }
    private fun requestActualizaUsuario(): ActualizarUsuarioRequest {
        val request: ActualizarUsuarioRequest
        if (edit_email == true) {
            request = ActualizarUsuarioRequest(
                idUsuario = User.idUsuario,
                nip = nip,
                nombre = binding.nameAdmin.text.toString(),
                apellidoPat = binding.apPaterno.text.toString(),
                apellidoMat = validateNodeApMat(),
                telefono = validateNodeTelefono(),
                codigoTel = codeEditEmployeText,
                email = binding.emailAdminData.text.toString().trim()
            )
        } else {
            request = ActualizarUsuarioRequest(
                idUsuario = User.idUsuario,
                nombre = binding.nameAdmin.text.toString(),
                apellidoPat = binding.apPaterno.text.toString(),
                apellidoMat = validateNodeApMat(),
                telefono = validateNodeTelefono(),
                codigoTel = codeEditEmployeText
            )
        }
        return request
    }
    private fun validateNodeTelefono(): Long {
        if (binding.phoneNumberAdminn.text.toString().isNotEmpty()) {
            return binding.phoneNumberAdminn.text.toString().toLong()
        }
        return 0
    }
    private fun validateNodeApMat(): String {
        if (binding.apMaterno.text.toString().isNotBlank()) {
            return binding.apMaterno.text.toString()
        }
        return "*"
    }
    private fun loadPhoneCode() {
        if (codeEditEmployeText.isEmpty()) {
            codeEditEmployeText = User.codigoTel
        }
        var positionCountry = 0
        val dataCounttries = CountriesNumber().getCountries()
        dataCounttries.forEachIndexed { index, countryPhone ->
            if (codeEditEmployeText == countryPhone.cveTelefono){
                positionCountry = index
            }
        }
        val adapter = AlgorithmAdapter(requireContext(), CountriesNumber().getCountries())
        binding.phoneCodeSpinnerAdmin.setAdapter(adapter)
        binding.phoneCodeSpinnerAdmin.setOnItemClickListener { _, _, i, _ ->
            binding.tilPhoneCodeSpinnerAdmin.prefixText = CountriesNumber().getCountries()[i].iso3
            binding.phoneCodeSpinnerAdmin.setText(CountriesNumber().getCountries()[i].cveTelefono)
            codeEditEmployeText = CountriesNumber().getCountries()[i].cveTelefono
        }
        binding.tilPhoneCodeSpinnerAdmin.prefixText = CountriesNumber().getCountries()[positionCountry].iso3
        binding.phoneCodeSpinnerAdmin.setText(CountriesNumber().getCountries()[positionCountry].cveTelefono)
    }
    private fun loadData() {
        binding.tilNameAdmin.isEnabled = false
        binding.tilApPaterno.isEnabled = false
        binding.tilApMaterno.isEnabled = false
        binding.tilPhoneCodeSpinnerAdmin.isEnabled = false
        binding.tilPhoneNumberAdminn.isEnabled = false
        binding.tilEmailAdminData.isEnabled = false
        codeEditEmployeText
        binding.nameAdmin.setText(User.nombre)
        binding.apPaterno.setText(User.apellidoPat)
        binding.apMaterno.setText(User.apellidoMat)
        binding.phoneNumberAdminn.setText(User.telefono)
        codeEditEmployeText = User.codigoTel
        binding.emailAdminData.setText(User.email)
        loadPhoneCode()
    }

    private fun checkFields(nameCompanyy: String, apPaterno: String, emailadminkt: String): String {

        val correoValido = "@" in emailadminkt

        if (nameCompanyy.isEmpty() && nameCompanyy.isBlank()) {
            return getString(R.string.error_admin_name_empty)
        }
        if (apPaterno.isBlank() && apPaterno.isEmpty()) {
            return getString(R.string.error_admin_appaterno_empty)
        }
        if (emailadminkt.isEmpty() && emailadminkt.isBlank()) {
            return getString(R.string.error_admin_correo_empty)
        }
        if (!correoValido) {
            return getString(R.string.a_login_error_email_valids)
        }
        if (User.email.trim() != emailadminkt) {
            edit_email = true
        }
        return "OK"
    }

    private fun btnBack() {
        binding.btnRegisterAdmin.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.Fragpricipal, WelcomeFragment(), "WelcomeFragment")
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
    fun showLoadingAnimation() {
        binding.adminLayoutFragment.visibility = View.VISIBLE
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }
    fun hideLoadingAnimation() {
        binding.adminLayoutFragment.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.data_admin_form_title))
    }
    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
}
