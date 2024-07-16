package com.venturessoft.human.views.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.venturessoft.human.R
import com.venturessoft.human.databinding.ActivityCompanyDataBinding
import com.venturessoft.human.models.request.AltaCompaniaRequest
import com.venturessoft.human.models.request.AltaUsuarioAdminRequest
import com.venturessoft.human.models.request.EliminarUsuario
import com.venturessoft.human.models.response.AltaCompaniaResponse
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.bd.DBDemo
import com.venturessoft.human.views.ui.viewModels.CreateAccountViewModel
class CompanyDataActivity : BaseActivity() {

    private lateinit var binding: ActivityCompanyDataBinding
    private val vmCreateAccount:CreateAccountViewModel by viewModels()
    private var altaCompaniaResponse = AltaCompaniaResponse()
    lateinit var myDataBase: DBDemo
    private var nameCompanykt: String = ""
    private var codeCompanykt: String = ""
    private var bussinesNamekt: String = ""
    private var employeNumber: String = ""
    private var maxEmployes: Int = 0
    private var rfckt: String = ""
    var countrykt: String = ""
    private var userData = AltaUsuarioAdminRequest()
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myDataBase = DBDemo(this)
        loadCountry()
        btnSave()
        addObserver()
        loadBack()
    }
    private fun loadBack() {
        binding.btnBackCompanyDataActi.setOnClickListener {
            finish()
        }
    }
    private fun addObserver() {
        vmCreateAccount.altaCompaniaResponseMutableData.observe(this) {
            if(it != null){
                altaCompaniaResponse = it
                if (altaCompaniaResponse.codigo == "ET000") {
                    val intent = Intent(this, AnimotionLottie::class.java)
                    intent.putExtra("email", userData.email)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    loadMessageError(altaCompaniaResponse.codigo)
                }
            }
        }
        vmCreateAccount.codeError.observe(this) {
            loadServerMessageError(it)
        }
    }
    private fun setData() {
        nameCompanykt = binding.nameCompany.text.toString()
        codeCompanykt = binding.codeCompany.text.toString()
        bussinesNamekt = binding.bussinesName.text.toString()
        employeNumber = binding.employeNumbers.text.toString()
        rfckt = binding.rfc.text.toString()
        maxEmployes = binding.employeNumbers.text.toString().toInt()
    }
    private fun btnSave() {
        binding.btnSaveAdmin.setOnClickListener {
            setData()
            val checkFields = checkFields(nameCompanykt, codeCompanykt, bussinesNamekt, rfckt, employeNumber)
            if (checkFields == "OK") {
                showLoadingAnimation()
                vmCreateAccount.getAltaUsuarioService(userData)
                vmCreateAccount.altaUsuarioAdminResponseMutableData.observe(this){
                    if(it != null){
                        if (it.codigo == "ET000"){
                            val request = AltaCompaniaRequest(
                                idUsuario = it.idUsuario,
                                nombreCia = nameCompanykt,
                                claveCia = codeCompanykt,
                                claveIdioma = "ES",
                                razonSocial = bussinesNamekt,
                                rfc = rfckt,
                                clavePais = "MX",
                                maximoEmpleados = maxEmployes,
                                fotoLocal = "N"
                            )
                            vmCreateAccount.getAltaCompaniaService(request)
                        }else{
                            val request = EliminarUsuario(it.idUsuario)
                            vmCreateAccount.getEliminarUsuario(request)
                            loadMessageError(altaCompaniaResponse.codigo)
                        }
                    }
                }
            } else {
                Toast.makeText(this, checkFields, Toast.LENGTH_SHORT).show()
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
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Utilities.countryList)
        adapter.setDropDownViewResource(R.layout.simple_spinner_etime)
        binding.country.adapter = adapter
        binding.country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.country.setBackgroundColor(Color.GRAY)
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val posicionPais = Utilities.countryList.indexOf(parent!!.getItemAtPosition(position).toString())
                countrykt = Utilities.codeCountry[posicionPais]
            }
        }
        try {
            val popup = Spinner::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val popupWindow = popup.get(binding.country) as ListPopupWindow
            popupWindow.height = 500
            popupWindow.setDropDownGravity(Gravity.BOTTOM)
        } catch (_: NoClassDefFoundError) { }
    }
    private fun loadMessageError(code: String) {
        val contextoPaquete: String = this.packageName
        val identificadorMensaje = this.resources.getIdentifier(code, "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(
                getString(identificadorMensaje),
                this@CompanyDataActivity,
                supportFragmentManager
            )
        } else {
            Utilities.showDialog("" + code, this@CompanyDataActivity,supportFragmentManager)
        }
        hideLoadingAnimation()
    }
    fun showLoadingAnimation() {
        binding.loadingAnimationCompanyData.root.visibility = View.VISIBLE
        binding.loadingAnimationHeaderCompanyData.visibility = View.VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    fun hideLoadingAnimation() {
        binding.loadingAnimationCompanyData.root.visibility = View.GONE
        binding.loadingAnimationHeaderCompanyData.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = this.packageName
        val indentificadorMensaje = this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        Utilities.showDialog(this.getString(indentificadorMensaje), this,supportFragmentManager)
    }
}
