package com.venturessoft.human.views.ui.activities

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.multidex.BuildConfig
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.venturessoft.human.R
import com.venturessoft.human.databinding.ActivitySplashBinding
import com.venturessoft.human.models.sharedpreferences.AvisoModel
import com.venturessoft.human.services.Lenguaje
import com.venturessoft.human.services.URL
import com.venturessoft.human.utils.*
import com.venturessoft.human.utils.Constants.Companion.LOOTIE_ANIMATION
import java.util.*

class SplashActivity : BaseActivity() {
    private var lenguaje: Lenguaje? = null
    private var len = Lenguaje()
    private lateinit var binding: ActivitySplashBinding
    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.FLEXIBLE

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackbarForCompleteUpdate()
        }
    }
    val request: NetworkRequest = NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()

    companion object {
        const val MY_CHANNEL_ID = "myChannel"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        binding.lottieSuccess.setAnimation(LOOTIE_ANIMATION)
        setContentView(binding.root)
        lenguaje = Preferences.getLanguage(this.applicationContext)
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        if (updateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.registerListener(installStateUpdatedListener)
        }
        showProgress()
    }
    private fun checkUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when (updateType) {
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> info.isFlexibleUpdateAllowed
                else -> false
            }
            if (isUpdateAvailable && isUpdateAllowed) {
                appUpdateManager.startUpdateFlowForResult(info, updateType, this, 123)
            } else {
                checkSession()
            }
        }
        appUpdateManager.appUpdateInfo.addOnFailureListener {
            checkSession()
        }
    }
    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(binding.root, getString(R.string.actualizacion_desc), Snackbar.LENGTH_INDEFINITE).apply {
            setAction(getString(R.string.restart)) { appUpdateManager.completeUpdate() }
            setActionTextColor(getColor(R.color.colorPrimary))
            show()
        }
    }
    override fun onResume() {
        super.onResume()
        if (updateType == AppUpdateType.IMMEDIATE) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(info, updateType, this, 123)
                } else {
                    checkSession()
                }
            }
            appUpdateManager.appUpdateInfo.addOnFailureListener {
                checkSession()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (updateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            123 -> {
                checkSession()
            }
        }
    }
    @Suppress("DEPRECATION")
    private fun reloadLaguage() {
        if (lenguaje != null) {
            len.idioma = "en"
            val locale = Locale(lenguaje!!.idioma)
            Locale.setDefault(locale)
            val config = this.baseContext.resources.configuration
            config.locale = locale
            this.baseContext.resources.updateConfiguration(config, this.baseContext.resources.displayMetrics)
            val context: Context = MyContextWrapper.wrap(this, len.idioma)
            resources.updateConfiguration(context.resources.configuration, context.resources.displayMetrics)
        }
        checkUpdate()
    }

    @SuppressLint("SetTextI18n")
    private fun configVersionEnvironmentApp(ambiente:String) {
        if (ambiente == "Preproductivo"){
            binding.txtViewEnvironment.isVisible = false
            binding.txtDevelopType.isVisible = false
        }
        binding.txtDevelopType.text = ambiente
        binding.txtViewEnvironment.text = "${getString(R.string.version)} ${BuildConfig.BUILD_TYPE} ${getString(R.string.compilacion)} ${BuildConfig.VERSION_CODE}"
        binding.txtViewVersionApp.text = BuildConfig.VERSION_NAME
        binding.lottieSuccess.setAnimation(LOOTIE_ANIMATION)
        reloadLaguage()
    }

    private fun showProgress() { startActivity() }
    private fun startActivity() {
        val permissionsArray = mutableListOf<String>()
        val grented = PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != grented) {
                permissionsArray.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != grented) {
            permissionsArray.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != grented) {
            permissionsArray.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != grented) {
            permissionsArray.add(Manifest.permission.CAMERA)
        }
        if (permissionsArray.isEmpty()) {
            getSSID()
        } else {
            ActivityCompat.requestPermissions(this, permissionsArray.toTypedArray(), 1)
        }
    }
    private fun checkSession() {
        binding.lottieSuccess.playAnimation()
        binding.lottieSuccess.addAnimatorUpdateListener { valueAnimator ->
            val progress = (valueAnimator.animatedValue as Float * 100).toInt()
            if (progress >= 50) {
                binding.root.setBackgroundResource(R.color.colorPrimary)
                binding.txtDevelopType.setTextColor(getColor(R.color.miWhite))
                binding.txtViewEnvironment.setTextColor(getColor(R.color.colorWhite))
                binding.txtViewVersionApp.setTextColor(getColor(R.color.colorWhite))
            }
        }
        binding.lottieSuccess.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }
            override fun onAnimationEnd(animation: Animator) {
                goToLoginActivity()          }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    private fun goToLoginActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(MY_CHANNEL_ID, "MySuperChannel", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Venturessoft"
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        Preferences.editSplashStatus(false, this)
        startActivity(intent)
        finish()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            var isGranted = true
            grantResults.forEach { permission ->
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false
                }
            }
            if (isGranted) {
                getSSID()
            } else {
                val dialogPermission = DialogGeneral(null,
                    "La app requiere de permisos, es necesario otorgarlos de manera manual para el correcto funcionamiento y posteriormente reintenté nuevamente",
                    null,
                    "Cerrar aplicacion",
                    {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", this@SplashActivity.packageName, null)
                        )
                        startActivity(intent)
                        this@SplashActivity.finish()
                    },
                    {
                        this@SplashActivity.finish()
                    }
                )
                dialogPermission.show(this.supportFragmentManager, "dialog")
            }
        }
    }
    private fun validateTerms() {
        val mDialog = LayoutInflater.from(this).inflate(R.layout.layout_license, null)
        val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this).setView(mDialog)
        val mAlertDialog = mBuilder.show()
        val txtPolicy = mDialog.findViewById<TextView>(R.id.txtPolicy)
        val btnCancel = mDialog.findViewById<Button>(R.id.btnCancel)
        val btnAccept = mDialog.findViewById<Button>(R.id.btnAccept)
        val checkTerms = mDialog.findViewById<CheckBox>(R.id.checkTerms)
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlertDialog.setCancelable(false)
        txtPolicy.text = HtmlCompat.fromHtml(getString(R.string.et_licence_terms_text_link), HtmlCompat.FROM_HTML_MODE_LEGACY)
        btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
            val intent = Intent(this@SplashActivity, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnAccept.setOnClickListener {
            if (checkTerms.isChecked) {
                val aviso = AvisoModel(statusAviso = "true")
                Preferences.editAviso(aviso, this@SplashActivity)
                mAlertDialog.dismiss()
                showProgress()
            }
        }
        txtPolicy.setOnClickListener {
            try {
                val urlAviso = "https://eland-ws-02.humaneland.net/ehuman/eh_files/aviso_de_privacidad/T%C3%89RMINOS%20Y%20CONDICIONES%20VSM.pdf"
                val webpage = Uri.parse(urlAviso)
                if (urlAviso.trim().isNotBlank()) {
                    val myIntent = Intent(Intent.ACTION_VIEW, webpage)
                    startActivity(myIntent)
                }
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)
    }
    private fun getSSID(){
        val mWifiManager: WifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var ambiente = ""
        try {
            var wifiName = mWifiManager.connectionInfo.ssid
            val wifiNameChar = mWifiManager.connectionInfo.ssid.toCharArray()
            if (wifiNameChar[0] == '"' && wifiNameChar[wifiNameChar.size - 1] == '"') {
                wifiName = Utilities.removeLastChar(wifiName)
                wifiName = wifiName.substring(1)
            }
            ambiente = when (wifiName) {
                "Wifi-VSM-DEV-MOBILE-2023" -> {
                    URL.URL_BASE = "http://192.168.0.10:7004/soa-infra/resources/HUMANDESVINCULADO/"
                    URL.URL_BASE_UNIFICADO = "http://192.168.0.10:7003/soa-infra/resources/HUMANDESVINCULADO/"
                    URL.URL_BASE_REPORTE_VIN = "http://192.168.0.10:7004/soa-infra/resources/HUMANVINCULADO/"
                    URL.URL_BASE_PAYPAL = "http://192.168.0.107/Paypal/paypal.html?price="
                    URL.URL_BASE_IMAGEN = "http://192.168.0.90:8080/Human/"
                    URL.URL_MICROSERVICIOS = "http://192.168.0.16/HumaneTime/api/"
                    "Desarrollo"
                }
                "Wifi-VSM-QA-MOBILE-2023" -> {
                    URL.URL_BASE = "http://192.168.0.10:8003/soa-infra/resources/HUMANDESVINCULADO/"
                    URL.URL_BASE_UNIFICADO = "http://192.168.0.10:8004/soa-infra/resources/HUMANDESVINCULADO/"
                    URL.URL_BASE_REPORTE_VIN = "http://192.168.0.10:8003/soa-infra/resources/HUMANVINCULADO/"
                    URL.URL_BASE_PAYPAL = "http://192.168.0.107/Paypal/paypal.html?price="
                    URL.URL_BASE_IMAGEN = "http://192.168.0.7:8080/Human/"
                    URL.URL_MICROSERVICIOS = "http://192.168.0.63/HumaneTime/api/"
                    "ControlCalidad"
                }
                else -> {
                    URL.URL_BASE = "https://etd.humaneland.net/soa-infra/resources/HUMANDESVINCULADO/"
                    URL.URL_BASE_UNIFICADO = "https://etd.humaneland.net:9004/soa-infra/resources/HUMANDESVINCULADO/"
                    URL.URL_BASE_REPORTE_VIN = "https://etd.humaneland.net/soa-infra/resources/HUMANVINCULADO/"
                    URL.URL_BASE_PAYPAL = "https://eland-ws-02.humaneland.net/ehuman/paypal.html?price="
                    URL.URL_BASE_IMAGEN = "https://eland-services.humaneland.net:8443/HumaneTime/Admin/Human/"
                    URL.URL_MICROSERVICIOS = "https://eland-dk.humaneland.net/HumaneTime/api/"
                    "Preproductivo"
                }
            }
        } catch (_: Exception) { }
        configVersionEnvironmentApp(ambiente)
    }
}

