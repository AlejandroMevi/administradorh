package com.venturessoft.human.views.ui.fragments.createAccount

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.venturessoft.human.databinding.FragmentDataUserBinding
import com.venturessoft.human.models.request.AltaUsuarioAdminRequest
import com.venturessoft.human.utils.AlgorithmAdapter
import com.venturessoft.human.utils.CountriesNumber
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.viewModels.CreateAccountViewModel
import java.util.Locale
import java.util.Random
import java.util.UUID

class DataUserFragment : Fragment(){

    private lateinit var binding: FragmentDataUserBinding
    private val vmCreateAccount: CreateAccountViewModel by activityViewModels()
    private var mainInterface: CreateAccountInterface? = null
    private var nameAdminkt: String = ""
    private var apPaternokt: String = ""
    private var apMaternokt: String = ""
    private var phonekt: String = ""
    private var emailkt: String = ""
    private var emailadminkt: String = ""
    private var codePhone: String = ""
    var codigo: String = ""
    var rfc: String = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDataUserBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setIntentData()
        loadSpinnerCodePhone()
        save()
    }
    private fun setIntentData() {
        emailkt = binding.emailAdminData.text.toString()
        phonekt = binding.phoneNumberAdminn.text.toString().trim()
        nameAdminkt = binding.nameAdmin.text.toString()
        apPaternokt = binding.apPaterno.text.toString()
        apMaternokt = binding.apMaterno.text.toString()
        emailadminkt = binding.emailAdminData.text.toString().lowercase(Locale.getDefault())
    }
    private fun save() {
        binding.btnNext.setOnClickListener {
            setIntentData()
            val validateData = checkFields()
            if (validateData == "OK") {
                val requestAltaUsuarioSAdmin = requestValidate()
                vmCreateAccount.dataUser.value = requestAltaUsuarioSAdmin
                mainInterface?.loadFragment(DataCompanyFragment(), "")
            } else {
                Toast.makeText(requireContext(), validateData, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun loadSpinnerCodePhone() {
        var positionCountry = 0
        val dataCounttries = CountriesNumber().getCountries()
        dataCounttries.forEachIndexed { index, countryPhone ->
            if (codePhone == countryPhone.cveTelefono){
                positionCountry = index
            }
        }
        val adapter = AlgorithmAdapter(requireContext(), CountriesNumber().getCountries())
        binding.phoneCodeSpinnerAdmin.setAdapter(adapter)
        codePhone = CountriesNumber().getCountries()[0].cveTelefono
        binding.phoneCodeSpinnerAdmin.setOnItemClickListener { _, _, i, _ ->
            binding.tilPhoneCodeSpinnerAdmin.prefixText = CountriesNumber().getCountries()[i].iso3
            binding.phoneCodeSpinnerAdmin.setText(CountriesNumber().getCountries()[i].cveTelefono)
            codePhone = CountriesNumber().getCountries()[i].cveTelefono
        }
        binding.tilPhoneCodeSpinnerAdmin.prefixText = CountriesNumber().getCountries()[positionCountry].iso3
        binding.phoneCodeSpinnerAdmin.setText(CountriesNumber().getCountries()[positionCountry].cveTelefono)
    }

    override fun onResume() {
        super.onResume()
        childFragmentManager.clearBackStack("")
    }
    private fun checkFields(): String {
        val correoValido = "@" in emailadminkt
        if (nameAdminkt.isEmpty()) {
            return getString(com.venturessoft.human.R.string.error_admin_name_empty)
        }
        if (apPaternokt.isBlank()) {
            return getString(com.venturessoft.human.R.string.error_admin_appaterno_empty)
        }
        if (emailadminkt.isEmpty()) {
            return getString(com.venturessoft.human.R.string.error_admin_correo_empty)
        }
        if (!correoValido) {
            return getString(com.venturessoft.human.R.string.a_login_error_email_valids)
        }
        return "OK"
    }
    @SuppressLint("HardwareIds")
    private fun requestValidate(): AltaUsuarioAdminRequest {
        val deviceId: String = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        return AltaUsuarioAdminRequest(
            email = emailkt.lowercase(),
            nombre = nameAdminkt,
            apellidoPat = apPaternokt,
            apellidoMat = apMaternokt,
            telefono = phonekt,
            codigoTel = codePhone,
            cuentaPrueba = true,
            uuid = deviceId
        )
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