package com.venturessoft.human.views.ui.activities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.google.firebase.messaging.FirebaseMessaging
import com.venturessoft.human.R
import com.venturessoft.human.databinding.ActivityLoginBinding
import com.venturessoft.human.models.request.AceptaAvisoRequest
import com.venturessoft.human.models.request.ActualizarUsuarioRequest
import com.venturessoft.human.models.request.ConsultaCompaniaRequest
import com.venturessoft.human.models.request.UserAccessRequest
import com.venturessoft.human.models.response.AceptarAvisoResponse
import com.venturessoft.human.models.response.ActualizarUsuarioResponse
import com.venturessoft.human.models.response.ConsultaCompaniaResponse
import com.venturessoft.human.models.response.GetAvisoPrivacidadResponse
import com.venturessoft.human.models.response.UserAccesoResponse
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.modals.ModalGeneric
import com.venturessoft.human.views.ui.viewModels.LoginViewModel

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var email: String = ""
    private var password: String = ""
    private var userAccessViewModel = LoginViewModel()
    private var userAccesoResponse = UserAccesoResponse()
    private var consultaCompaniaResponse = ConsultaCompaniaResponse()
    private var actualizaUsuarioResponse = ActualizarUsuarioResponse()
    private var aceptarAvisoResponse = AceptarAvisoResponse()
    private var getAvisoPrivacidadResponse = GetAvisoPrivacidadResponse()
    private var selectScia: Boolean = false
    private var itemsCiaNum = ArrayList<Long>()
    private var loggedEmail: String? = ""
    private var loggedPassword: String? = ""
    private val itemsCiaRason = ArrayList<String>()
    private var ciaSpinner:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadEmailRegister()
        messageLogout()
        initializeView()
        addObservers()
        loadIntent()
        Utilities.setupUI(binding.mainLayout, this)
    }
    private fun loadIntent() {
        if (intent.getStringExtra("email") != null) {
            binding.etEmail.setText(intent.getStringExtra("email"))
        }
        if (intent.getStringExtra("password") != null) {
            binding.etPassword.setText(intent.getStringExtra("password"))
        }
    }
    private fun addObservers() {
        userAccessViewModel.userAccessResponseMutableData.observe(this) {
            userAccesoResponse = it
            when (it.codigo) {
                "ET000" -> {
                    typeOfEnvironment()
                    User.email = binding.etEmail.text.toString().trim()
                }
                else -> {
                    Utilities.loadMessageError(userAccesoResponse.codigo, this,supportFragmentManager)
                }
            }
        }
        userAccessViewModel.codeError.observe(this) {
            loadServerMessageError(it,supportFragmentManager)
        }
        userAccessViewModel.codeErrorToken.observe(this) {
            println("codigo : $it")
        }
        userAccessViewModel.consultaCompaniaResponseMutableData.observe(this) {
            consultaCompaniaResponse = it
            if (consultaCompaniaResponse.codigo == "ET000") {
                succesResponse()
            } else {
                Utilities.loadMessageError(
                    consultaCompaniaResponse.codigo,
                    this,
                    supportFragmentManager
                )
            }
        }
        userAccessViewModel.actualizarUsuarioResponseMutableData.observe(this) {
            actualizaUsuarioResponse = it
            if (actualizaUsuarioResponse.codigo == "ET000") {
                afterValidateTerms()
            } else {
                Utilities.loadMessageError(
                    actualizaUsuarioResponse.codigo,
                    this,
                    supportFragmentManager
                )
            }
        }
        userAccessViewModel.actualizarTokenResponseMutableData.observe(this) {
            actualizaUsuarioResponse = it
            if (actualizaUsuarioResponse.codigo == "ET000") {
                println("Codigo Success enviar token" + actualizaUsuarioResponse.codigo)
            } else {
                Utilities.loadMessageError(actualizaUsuarioResponse.codigo, this, supportFragmentManager)
            }
        }
        userAccessViewModel.isLoading.observe(this) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        userAccessViewModel.aceptarAvisoResponseMutableLiveData.observe(this) {
            aceptarAvisoResponse = it
            if (aceptarAvisoResponse.codigo == "ET000") {
                nexViewVinculados()
            } else {
                Utilities.loadMessageError(
                    aceptarAvisoResponse.codigo,
                    this,
                    supportFragmentManager
                )
            }
        }
        userAccessViewModel.getAvisoResponseMutableLiveData.observe(this) {
            getAvisoPrivacidadResponse = it
            if (getAvisoPrivacidadResponse.codigo == "ET000" && getAvisoPrivacidadResponse.urlAviso!!.trim().isNotBlank()) {
                validateTerms()
            } else if (getAvisoPrivacidadResponse.urlAviso!!.trim().isBlank() || getAvisoPrivacidadResponse.codigo == "ET450" || getAvisoPrivacidadResponse.codigo == "ET451") {
                nexViewVinculados()
            } else {
                Utilities.loadMessageError(
                    getAvisoPrivacidadResponse.codigo!!,
                    this,
                    supportFragmentManager
                )
            }
        }
    }

    @SuppressLint("LogNotTimber")
    private fun loadTokenFirebaseMessage() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.i("Token Firebase", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val deviceToken: String = task.result
            Log.i("SE GENERA ", "TOKEN DESDE FIREBASE")
            Log.d("newToken", deviceToken)
            sendToken(deviceToken)
        }
    }
    private fun sendToken(deviceToken: String) {
        val requestActualizaUsuario = ActualizarUsuarioRequest(
            idUsuario = User.idUsuario,
            token = deviceToken
        )
        userAccessViewModel.getActualizarTokenService(requestActualizaUsuario)
    }
    private fun typeOfEnvironment() {
        User.ambiente = userAccesoResponse.acceso.ambiente
        User.token = userAccesoResponse.token
        User.urlAvisoPriv = userAccesoResponse.acceso.urlAvisoPrivacidad
        when (User.ambiente) {
            "HU" -> {
                if (userAccesoResponse.acceso.usuarioHuman!!.enrolador == true) {
                    User.nombreCompleto = userAccesoResponse.acceso.usuarioHuman!!.nombre
                    User.claveCia = userAccesoResponse.acceso.usuarioHuman!!.cveCia
                    User.idUsuario = userAccesoResponse.acceso.usuarioHuman!!.numeroEmpleado
                    User.estaciones = userAccesoResponse.acceso.usuarioHuman!!.estaciones
                    User.fotografia = userAccesoResponse.acceso.usuarioHuman?.foto.toString()
                    User.idUsuFree = userAccesoResponse.acceso.usuarioHuman?.usuario.toString()
                    if (userAccesoResponse.acceso.usuarioHuman!!.scia!!.size == 1) {
                        User.ciaVinculado =
                            userAccesoResponse.acceso.usuarioHuman!!.scia?.get(0)!!.cia
                        nexViewVinculados()
                    }
                    if (userAccesoResponse.acceso.usuarioHuman!!.scia!!.size > 1) {
                        loggedEmail = binding.etEmail.text.toString().trim()
                        loggedPassword = binding.etPassword.text.toString().trim()
                        showSpinnerCia()
                        loadSpinnerCia()
                    }
                } else {
                    Utilities.loadMessageError(
                        "a_login_error_access_user",
                        this,
                        supportFragmentManager
                    )
                }
            }
            "ET" -> {
                loadTokenFirebaseMessage()
                User.idUsuario = userAccesoResponse.acceso.usuarioeTime!!.idUsuario!!
                User.tipoUsuario = userAccesoResponse.acceso.usuarioeTime!!.tipoUsuario
                User.estaciones = userAccesoResponse.acceso.usuarioeTime!!.estaciones!!
                User.freeTrial = false
                User.costoEmpleado = userAccesoResponse.acceso.usuarioeTime?.costoEmpleado!!
                User.fechaVigencia = userAccesoResponse.acceso.usuarioeTime!!.fechaVigencia
                User.fechaActual = userAccesoResponse.acceso.usuarioeTime!!.fechaActual
                User.expiracion = userAccesoResponse.acceso.usuarioeTime!!.expiracion
                User.fotografia = userAccesoResponse.acceso.usuarioeTime!!.foto
                User.maximoEmpleados = userAccesoResponse.acceso.usuarioeTime!!.scia!![0].maximoEmpleados.toInt()
                User.idCompani = userAccesoResponse.acceso.usuarioeTime!!.scia!![0].cia
                afterValidateTerms()
            }
        }
    }
    private fun loadSpinnerCia() {
        itemsCiaRason.add(getString(R.string.a_login_text_select_cia))
        itemsCiaNum.add(0)
        for (i in 0 until userAccesoResponse.acceso.usuarioHuman?.scia!!.size) {
            itemsCiaRason.add(userAccesoResponse.acceso.usuarioHuman?.scia?.get(i)?.razonSocial?:"")
            itemsCiaNum.add(userAccesoResponse.acceso.usuarioHuman?.scia?.get(i)?.cia?:0)
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, itemsCiaRason)
        binding.spinnerCompany.setAdapter(adapter)
        binding.spinnerCompany.setText(itemsCiaRason[0],false)
        binding.spinnerCompany.setOnItemClickListener { _, _, position, _ ->
            ciaSpinner = itemsCiaNum[position]
            User.ciaSeleccionada = ciaSpinner
        }
        selectScia = true
    }
    private fun showSpinnerCia() {
        binding.linearCia.visibility = View.VISIBLE
    }
    private fun nexViewVinculados() {
        val intent = Intent(this@LoginActivity, NavigationDrawerActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
    private fun afterValidateTerms() {
        if (userAccesoResponse.acceso.usuarioeTime!!.scia != null) {
            val requestCompany = ConsultaCompaniaRequest(
                idUsuario = userAccesoResponse.acceso.usuarioeTime?.idUsuario!!,
                idCompania = userAccesoResponse.acceso.usuarioeTime!!.scia!![0].cia
            )
            userAccessViewModel.getConsultaCompania(requestCompany)
        } else {
            finishAffinity()
            val intent = Intent(this@LoginActivity, CompanyDataActivity::class.java)
            intent.putExtra("idUsuario", User.idUsuario)
            intent.putExtra("emailAdmin", binding.etEmail.text.toString())
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
    private fun validateTerms() {
        val mDialog = LayoutInflater.from(this).inflate(R.layout.layout_license, null)
        val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this).setView(mDialog)
        val mAlertDialog = mBuilder.show()
        val txtPolicy = findViewById<TextView>(R.id.txtPolicy)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val btnAccept = findViewById<Button>(R.id.btnAccept)
        val checkTerms = findViewById<CheckBox>(R.id.checkTerms)
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlertDialog.setCancelable(false)
        txtPolicy.text = HtmlCompat.fromHtml(
            getString(R.string.et_licence_terms_text_link),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
        btnAccept.setOnClickListener {
            if (checkTerms.isChecked) {
                showLoadingAnimation()
                loadServiceUpdateTerms()
                mAlertDialog.dismiss()
            }
        }
        txtPolicy.setOnClickListener {
            try {
                var urlAviso = ""
                if (User.ambiente == "HU") {
                    urlAviso = getAvisoPrivacidadResponse.urlAviso!!
                }
                if (User.ambiente == "ET") {
                    urlAviso = userAccesoResponse.acceso.usuarioeTime!!.urlAviso!!
                }
                val webpage = Uri.parse(urlAviso)
                if (urlAviso.trim().isNotBlank()) {
                    val myIntent = Intent(Intent.ACTION_VIEW, webpage)
                    startActivity(myIntent)
                }
            } catch (_: ActivityNotFoundException) { }
        }
    }
    private fun loadServiceUpdateTerms() {
        when (User.ambiente) {
            "HU" -> {
                val aceptaAvisoRequest = AceptaAvisoRequest(
                    numCia = userAccesoResponse.acceso.usuarioHuman!!.cia,
                    numEmp = userAccesoResponse.acceso.usuarioHuman!!.numeroEmpleado
                )
                userAccessViewModel.aceptarAvisoService(aceptaAvisoRequest)
            }
            "ET" -> {
                val requestActualizaUsuario = ActualizarUsuarioRequest(
                    idUsuario = User.idUsuario,
                    nombre = null,
                    apellidoPat = null,
                    apellidoMat = null,
                    telefono = null,
                    codigoTel = null,
                    aviso = true
                )
                userAccessViewModel.getActualizarUsuarioService(requestActualizaUsuario)
            }
        }
    }
    private fun loadServerMessageError(code: Int, supportFragmentManager: FragmentManager) {
        hideLoadingAnimation()
        val contextoPaquete: String = this.packageName
        val identificadorMensaje =
            this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(this.getString(identificadorMensaje), this,supportFragmentManager)
        } else {
            Utilities.showDialog(code.toString(), this,supportFragmentManager)
        }
    }
    private fun succesResponse() {
        User.idUsuario = userAccesoResponse.acceso.usuarioeTime!!.idUsuario!!
        User.email = binding.etEmail.text.toString().trim()
        User.nombreCompleto = userAccesoResponse.acceso.usuarioeTime!!.nombreCompleto
        User.nombre = userAccesoResponse.acceso.usuarioeTime!!.nombre
        User.apellidoPat = userAccesoResponse.acceso.usuarioeTime!!.apellidoPat
        User.apellidoMat = userAccesoResponse.acceso.usuarioeTime!!.apellidoMat
        User.codigoTel = userAccesoResponse.acceso.usuarioeTime!!.codigoTel
        User.telefono = userAccesoResponse.acceso.usuarioeTime!!.telefono
        User.tipoUsuario = userAccesoResponse.acceso.usuarioeTime!!.tipoUsuario
        User.scia = userAccesoResponse.acceso.usuarioeTime!!.scia
        User.szona = userAccesoResponse.acceso.usuarioeTime!!.szona
        User.claveCia = consultaCompaniaResponse.cia!![0].claveCia
        val intent = Intent(this@LoginActivity, NavigationDrawerActivity::class.java)
        startActivity(intent)
    }
    private fun messageLogout() {
        val isFromLogOut = intent.getBooleanExtra(Constants.FROM_LOG_OUT, false)
        if (isFromLogOut) {
            val modalGeneric = ModalGeneric.newFunction {
                val intent = Intent(this@LoginActivity, LoginActivity::class.java)
                intent.putExtra(Constants.FROM_LOG_OUT, true)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finishAffinity()
            }
            ModalGeneric().updateBinding(
                false,
                getString(R.string.side_menu_title_8),
                getString(R.string.message_successful_logout),
                getString(R.string.accept),
                null
            )
            modalGeneric.show(this.supportFragmentManager, "ModalBottomSheet")
        }
    }
    private fun loadEmailRegister() {
        binding.etEmail.setText(intent.getStringExtra("email"))
    }
    private fun initializeView() {
        binding.enterButton.setOnClickListener {
            clickLogin()
        }
        binding.forgotNipButton.setOnClickListener {
            clickForgotNip()
        }
        binding.createAccount.setOnClickListener {
            registerAdmin()
        }
        binding.etPassword.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(v1: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    clickLogin()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.passwordTextField.windowToken, 0)
                    return true
                }
                return false
            }
        })
        binding.forgotNipButton.text = HtmlCompat.fromHtml(
            getString(R.string.a_login_link_recover_password),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.createAccount.text = HtmlCompat.fromHtml(
            getString(R.string.a_login_link_new_account),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
    private fun clickForgotNip() {
        val intent = Intent(this@LoginActivity, ForgotNipActivity::class.java)
        startActivity(intent)
    }
    fun clickLogin() {
   /*     binding.etEmail.setText("jair_151@hotmail.com")
        binding.etPassword.setText("Password44$$")*/
        //binding.etEmail.setText("jsc@vsm.com")
        //binding.etPassword.setText("4027")
        email = binding.etEmail.text!!.toString().trim()
        password = binding.etPassword.text!!.toString().trim()
        val checkFieldsMessage = checkFields()
        if (checkFieldsMessage == "OK") {
            if (selectScia) {
                if (ciaSpinner == 0L) {
                    Toast.makeText(this@LoginActivity, getString(R.string.a_login_toast_select_cia), Toast.LENGTH_SHORT).show()
                } else {
                    User.ciaVinculado = ciaSpinner
                    nexViewVinculados()
                }
            } else {
                showLoadingAnimation()
                User.pass = password
                val userAccessRequest = UserAccessRequest(email, password, getString(R.string.languague).uppercase())
                userAccessViewModel.getuserAccessService(userAccessRequest)
            }
        } else {
            Toast.makeText(this, checkFieldsMessage, Toast.LENGTH_SHORT).show()
        }
    }
    fun showLoadingAnimation() {
        binding.progress.root.isVisible = true
    }
    fun hideLoadingAnimation() {
        binding.progress.root.isVisible = false
    }
    fun checkFields(): String {
        val correoValido = "@" in email
        if (email.isEmpty()) {
            return getString(R.string.a_login_error_email_empty)
        }
        if (password.isEmpty()) {
            return getString(R.string.a_login_error_nip_empty)
        }
        if (!correoValido) {
            return getString(R.string.a_login_error_email_valids)
        }
        return "OK"
    }
    private fun registerAdmin() {
        val intent = Intent(this@LoginActivity, CreateAccountActivity::class.java)
        intent.putExtra("email", binding.etEmail.text.toString())
        startActivity(intent)
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)
    }
}
