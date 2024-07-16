package com.venturessoft.human.views.ui.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.venturessoft.human.R
import com.venturessoft.human.databinding.ActivityNavDrawerBinding
import com.venturessoft.human.models.User
import com.venturessoft.human.services.Lenguaje
import com.venturessoft.human.utils.AlarmNotification
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.Preferences
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.Utilities.Companion.compareDates
import com.venturessoft.human.utils.Utilities.Companion.isTimeAutomaticEnabled
import com.venturessoft.human.views.ui.fragments.AboutUs.AboutUsFragment
import com.venturessoft.human.views.ui.fragments.Administrator.AdminDataFragment
import com.venturessoft.human.views.ui.fragments.AuditHistory.AuditHistoryFilterFragment
import com.venturessoft.human.views.ui.fragments.AuditHistory.ET029Fragment
import com.venturessoft.human.views.ui.fragments.AuditHistory.ET030Fragment
import com.venturessoft.human.views.ui.fragments.AuditHistory.ET032Fragment
import com.venturessoft.human.views.ui.fragments.AuditHistory.ItemCheckInOutSuccessFragment
import com.venturessoft.human.views.ui.fragments.AuditHistory.ItemFailsGeneralErrorFragment
import com.venturessoft.human.views.ui.fragments.AuditHistory.ListAudtHistoryFragment
import com.venturessoft.human.views.ui.fragments.Camera.CameraFragment
import com.venturessoft.human.views.ui.fragments.CameraAdmin.CameraAdminFragment
import com.venturessoft.human.views.ui.fragments.ChangePassword.ChangePasswordFragment
import com.venturessoft.human.views.ui.fragments.CompanyData.CompanyDataFragment
import com.venturessoft.human.views.ui.fragments.Employe.EditEmployeeFragment
import com.venturessoft.human.views.ui.fragments.Employe.EmployeeListFragment
import com.venturessoft.human.views.ui.fragments.Employe.RegisterEmployeeFragment
import com.venturessoft.human.views.ui.fragments.Filter.FilterReportFragment
import com.venturessoft.human.views.ui.fragments.ReportFragment.Enroll_Unenroll_ReportFragment
import com.venturessoft.human.views.ui.fragments.ReportFragment.ReporByEmployee
import com.venturessoft.human.views.ui.fragments.ReportFragment.ReportFragment
import com.venturessoft.human.views.ui.fragments.ReportFragment.ReportViewFragment
import com.venturessoft.human.views.ui.fragments.ReportFragment.ReportsMenuFragment
import com.venturessoft.human.views.ui.fragments.Settings.SettingsFragment
import com.venturessoft.human.views.ui.fragments.Stations.EditStationsFragment
import com.venturessoft.human.views.ui.fragments.Stations.ListEstacionesFragment
import com.venturessoft.human.views.ui.fragments.Stations.SelectedStationFragment
import com.venturessoft.human.views.ui.fragments.Stations.StationsFragment
import com.venturessoft.human.views.ui.fragments.Supervisor.EditSupervisorFragment
import com.venturessoft.human.views.ui.fragments.Supervisor.ListaSupervisorFragment
import com.venturessoft.human.views.ui.fragments.Supervisor.RegisterSupervisorFragment
import com.venturessoft.human.views.ui.fragments.UpdatePhoto.UpdatePhotoFragment
import com.venturessoft.human.views.ui.fragments.UpdatePhotoAdmin.UpdatePhotoAdminFragment
import com.venturessoft.human.views.ui.fragments.WelcomeVinculadoFragment
import com.venturessoft.human.views.ui.fragments.Zones.ListZonesFragment
import com.venturessoft.human.views.ui.fragments.Zones.SelectZonesFragment
import com.venturessoft.human.views.ui.fragments.photographyauthorization.DetailsPhotographyAuthorizationFragment
import com.venturessoft.human.views.ui.fragments.photographyauthorization.PhotographyAuthorizationFragment
import com.venturessoft.human.views.ui.fragments.welcome.EnterpriceDetailsFragment
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import de.hdodenhof.circleimageview.CircleImageView
import java.util.Calendar

class NavigationDrawerActivity : ItemCheckInOutSuccessFragment.OnFragmentInteractionListener,
    ItemFailsGeneralErrorFragment.OnFragmentInteractionListener,
    ET030Fragment.OnFragmentInteractionListener, ET029Fragment.OnFragmentInteractionListener,
    ListAudtHistoryFragment.OnFragmentInteractionListener,
    ET032Fragment.OnFragmentInteractionListener, AppCompatActivity(),
    AuditHistoryFilterFragment.OnFragmentInteractionListener,
    ReportViewFragment.OnFragmentInteractionListener,
    Enroll_Unenroll_ReportFragment.OnFragmentInteractionListener,
    ReportsMenuFragment.OnFragmentInteractionListener,
    AboutUsFragment.OnFragmentInteractionListener, CameraFragment.OnFragmentInteractionListener,
    UpdatePhotoFragment.OnFragmentInteractionListener,
    EditEmployeeFragment.OnFragmentInteractionListener,
    EditSupervisorFragment.OnFragmentInteractionListener,
    EditStationsFragment.OnFragmentInteractionListener,
    ListaSupervisorFragment.OnFragmentInteractionListener,
    SelectedStationFragment.OnFragmentInteractionListener,
    UpdatePhotoAdminFragment.OnFragmentInteractionListener,
    CameraAdminFragment.OnFragmentInteractionListener,
    FilterReportFragment.OnFragmentInteractionListener,
    ReportFragment.OnFragmentInteractionListener,
    PhotographyAuthorizationFragment.OnFragmentInteractionListener,
    DetailsPhotographyAuthorizationFragment.OnFragmentInteractionListener,
    ListEstacionesFragment.OnFragmentInteractionListener,
    ListZonesFragment.OnFragmentInteractionListener,
    RegisterEmployeeFragment.OnFragmentInteractionListener,
    CompanyDataFragment.OnFragmentInteractionListener,
    ChangePasswordFragment.OnFragmentInteractionListener,
    StationsFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener,
    RegisterSupervisorFragment.OnFragmentInteractionListener,
    EmployeeListFragment.OnFragmentInteractionListener,
    AdminDataFragment.OnFragmentInteractionListener,
    SelectZonesFragment.OnFragmentInteractionListener,
    SettingsFragment.OnFragmentInteractionListener, ReporByEmployee.OnFragmentInteractionListener,
    MainInterface {

    private lateinit var binding: ActivityNavDrawerBinding
    var user: User? = null
    private var lenguaje: Lenguaje? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lenguaje = Preferences.getLanguage(this.applicationContext)
        loadNavigationAndUserType()
        val listRedirect: String? = intent.getStringExtra("Lista")
        if (listRedirect != null) {
            redirectView(listRedirect)
        } else {
            when (com.venturessoft.human.utils.User.ambiente) {
                "HU" -> loadFragment(WelcomeVinculadoFragment())
                "ET" -> loadFragment(WelcomeFragment())
            }
        }
        binding.drawerLayout.setScrimColor(getColor(R.color.mTransparentBlue))
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        //validateTrialVersion()
    }
    private fun validateTrialVersion() {
        if (com.venturessoft.human.utils.User.fechaVigencia.isNotEmpty()) {
            if (isTimeAutomaticEnabled(this)) {
                val days = compareDates(
                    com.venturessoft.human.utils.User.fechaActual,
                    com.venturessoft.human.utils.User.fechaVigencia
                )
                scheduleNotification(7)
                scheduleNotification(14)
            } else {
                val days = compareDates(
                    com.venturessoft.human.utils.User.fechaActual,
                    com.venturessoft.human.utils.User.fechaVigencia
                )
                scheduleNotification(days)
            }
        }
    }

    private fun scheduleNotification(days: Int) {
        val intent = Intent(applicationContext, AlarmNotification()::class.java)
        when (days) {
            in 9..15 -> {
                AlarmNotification.NOTIFICATION_ID = 1
                AlarmNotification.TITLE = getString(R.string.notification_one)
            }

            in 1..8 -> {
                AlarmNotification.NOTIFICATION_ID = 2
                AlarmNotification.TITLE = getString(R.string.notification_two)
            }
        }
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            AlarmNotification.NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            Calendar.getInstance().timeInMillis + (days * 1000)/*86400000L*/,
            pendingIntent
        )
    }

    private fun redirectView(redirect: String?) {
        when (redirect) {
            "Zonas" -> loadFragment(ListZonesFragment())
            "Estaciones" -> loadFragment(ListEstacionesFragment())
            "Employee" -> loadFragment(EmployeeListFragment())
            "Supervisor" -> loadFragment(ListaSupervisorFragment())
            "ReportWithData" -> loadFragment(ReportFragment())
            "Filter" -> loadFragment(FilterReportFragment())
        }
    }

    private fun loadNavigationAndUserType() {
        Utilities.setupUI(binding.drawerLayout, this)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        loadDataUser()
    }

    private fun loadDataUser() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {}
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        val hr = navView.getHeaderView(0)
        val uname = hr.findViewById<TextView>(R.id.userName)
        val photo = hr.findViewById<CircleImageView>(R.id.imgAvatar)
        val drawer = binding.drawerLayout
        drawer.setScrimColor(Color.TRANSPARENT)
        uname.text = com.venturessoft.human.utils.User.nombreCompleto
        if (com.venturessoft.human.utils.User.fotografia.isEmpty().not()) {
            val imagenBase64 =
                Base64.decode(com.venturessoft.human.utils.User.fotografia, Base64.DEFAULT)
            val imagenconverBitmap =
                BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
            photo.setImageBitmap(imagenconverBitmap)
        } else {
            photo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.profile_photo))
        }
        when (com.venturessoft.human.utils.User.ambiente) {
            "HU" -> {
                navView.menu.removeItem(R.id.payments)
                navView.menu.removeItem(R.id.nav_companyData)
                navView.menu.removeItem(R.id.nav_Zone)
                navView.menu.removeItem(R.id.nav_Supervisores)
                navView.menu.removeItem(R.id.nav_Estaciones)
                navView.menu.removeItem(R.id.nav_mantEmp)
                navView.menu.removeItem(R.id.nav_PhotographyAuthorization)
            }

            "ET" -> {
                navView.menu.removeItem(R.id.payments)
                if (com.venturessoft.human.utils.User.scia!![0].fotoLocal == "S") navView.menu.removeItem(R.id.nav_AuditHistory)
                navView.menu.removeItem(R.id.nav_About)
                if (com.venturessoft.human.utils.User.tipoUsuario > 1) {
                    navView.menu.removeItem(R.id.payments)
                    navView.menu.removeItem(R.id.nav_companyData)
                    navView.menu.removeItem(R.id.nav_Zone)
                    navView.menu.removeItem(R.id.nav_Supervisores)
                }
            }
        }
    }

    override fun onBackPressed() {
    }

    override fun onBackMenuLsuper() {
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = binding.drawerLayout
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val botonModoColaboradorr = findViewById<Button>(R.id.botonModoColaboradorr)
        toolbar.navigationIcon = null
        drawerLayout.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        botonModoColaboradorr.visibility = View.VISIBLE
        botonModoColaboradorr.setOnClickListener {
            val backBtn = findViewById<Button>(R.id.btnBackMenuListSuper)
            backBtn.callOnClick()
            botonModoColaboradorr.visibility = View.GONE
            drawerLayout.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED)
            toolbar.setNavigationIcon(R.drawable.ic_menu)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.payments -> {
                val intent = Intent(this, WayToPayActivity::class.java)
                intent.putExtra("isPaypal", true)
                startActivity(intent)
            }

            R.id.nav_companyData -> {
                loadFragment(AdminDataFragment())
            }

            R.id.nav_Zone -> {
                loadFragment(ListZonesFragment())
            }

            R.id.nav_Supervisores -> {
                loadFragment(ListaSupervisorFragment())
            }

            R.id.nav_mantEmp -> {
                loadFragment(EmployeeListFragment())
            }

            R.id.nav_Estaciones -> {
                loadFragment(ListEstacionesFragment())
            }

            R.id.change_nip -> {
                when (com.venturessoft.human.utils.User.ambiente) {
                    "HU" -> {
                        val intent = Intent(
                            this@NavigationDrawerActivity,
                            ChangeNipVinculadoActivity::class.java
                        )
                        startActivity(intent)
                    }

                    "ET" -> loadFragment(ChangePasswordFragment())
                }
            }

            R.id.nav_Reportes -> {
                loadFragment(ReportsMenuFragment())
            }

            R.id.nav_AuditHistory -> {
                loadFragment(AuditHistoryFilterFragment())
            }

            R.id.nav_PhotographyAuthorization -> {
                loadFragment(PhotographyAuthorizationFragment())
            }
            R.id.nav_Settings -> {
                loadFragment(SettingsFragment())
            }

            R.id.nav_About -> {
                loadFragment(AboutUsFragment())
            }

            R.id.nav_closed -> {
                val dialogLogout = DialogGeneral(getString(R.string.logout),getString(R.string.message_confirm_logout),getString(R.string.logout),getString(R.string.cancel), {
                    val intent = Intent(this@NavigationDrawerActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finishAffinity()
                })

                dialogLogout.show(supportFragmentManager,"")
            }
        }
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout =
            findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFragment(fragmet: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.Fragpricipal, fragmet, "WelcomeFragment")
            .commit()
    }

    override fun onBack(name: String) {
        onBackView(name)
    }

    private fun onBackView(view: String) {
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = binding.drawerLayout
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val botonModoColaboradorr = findViewById<Button>(R.id.botonModoColaboradorr)
        toolbar.navigationIcon = null
        drawerLayout.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        botonModoColaboradorr.visibility = View.VISIBLE
        botonModoColaboradorr.setOnClickListener {
            when (view) {
                "backListEstacion" -> {
                    val btnBackZon = findViewById<Button>(R.id.btnBackMenuListEst)
                    btnBackZon.callOnClick()
                }

                "backMenuReportByEmp" -> {
                    val btnBackZon = findViewById<Button>(R.id.btnBackFilter)
                    btnBackZon.callOnClick()
                }

                "backMenu" -> {
                    val btnBackZon = findViewById<Button>(R.id.btnBackMenu)
                    btnBackZon.callOnClick()
                }

                "backMenuSettings" -> {
                    val btnBackZon = findViewById<Button>(R.id.btnBackMenuSettings)
                    btnBackZon.callOnClick()
                }

                "backMenuUpdatePhoto" -> {
                    val btnBack = findViewById<Button>(R.id.btnBackMenuUpdate)
                    btnBack.callOnClick()
                }

                "backToUpdatePhoto" -> {
                    val btnBack = findViewById<Button>(R.id.btnBackUpdatePhotoSuper)
                    btnBack.callOnClick()
                }

                "backMenuReport" -> {
                    val btnBackZonn = findViewById<Button>(R.id.btnAtrasReport)
                    btnBackZonn.callOnClick()
                }

                "backReports" -> {
                    val btnAtrReport = findViewById<Button>(R.id.btnAtrReport)
                    btnAtrReport.callOnClick()
                }

                "backCamaraFragmentOfUpdate" -> {
                    val btnBackRegisterEmployee = findViewById<Button>(R.id.btnBackRegisterEmploye)
                    btnBackRegisterEmployee.callOnClick()
                }

                "backButtonRegisterEmployee" -> {
                    val btnCompanyDFragment = findViewById<Button>(R.id.btnBackRegEmployee)
                    btnCompanyDFragment.callOnClick()
                }

                "backCamaraFragment" -> {
                    val btnBackRegisterEmployee = findViewById<Button>(R.id.btnBackRegisterEmployee)
                    btnBackRegisterEmployee.callOnClick()
                }

                "backListMantenanceEmployee" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackRegEmployeeEdit)
                    backBtn.callOnClick()
                }

                "backSelectionEmployee" -> {
                    val btnBackRegisterEmployee = findViewById<Button>(R.id.btnBackSelectedRegister)
                    btnBackRegisterEmployee.callOnClick()
                }

                "backButtonCompanyData" -> {
                    val btnCompanyDFragment = findViewById<Button>(R.id.btnBackCompanyData)
                    btnCompanyDFragment.callOnClick()
                }

                "backButtonChangePassMethod" -> {
                    val btnChangePass = findViewById<Button>(R.id.btnBackChange)
                    btnChangePass.callOnClick()
                }

                "backButtonEstationMethod" -> {
                    val btnEstacioness = findViewById<Button>(R.id.btnBackEstaciones)
                    btnEstacioness.callOnClick()
                }

                "backSuper" -> {
                    val backBtn = findViewById<Button>(R.id.btnSelectZones)
                    backBtn.callOnClick()
                }

                "backButonMaintenanceMethod" -> {
                    val btnMaintenance = findViewById<Button>(R.id.btnBackMaintenance)
                    btnMaintenance.callOnClick()
                }

                "backButonRegisteMethod" -> {
                    val btnRegisterr = findViewById<Button>(R.id.btnRegisterSuper)
                    btnRegisterr.callOnClick()
                }

                "backAdmin" -> {
                    val btnAtrReport = findViewById<Button>(R.id.btnRegisterAdmin)
                    btnAtrReport.callOnClick()
                }

                "backEditSuper" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackSuperEdit)
                    backBtn.callOnClick()
                }

                "backListStation" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackListEstaciones)
                    backBtn.callOnClick()
                }

                "backMenuAbout" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackAboutUsF)
                    backBtn.callOnClick()
                }

                "backMenuToHome" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackHome)
                    backBtn.callOnClick()
                }
                "backPhotoToHome" -> {
                    val btnMaintenance = findViewById<Button>(R.id.btnBackListPhoto)
                    btnMaintenance.callOnClick()
                }
                "backListDetailsPhoto" -> {
                    val btnBack = findViewById<Button>(R.id.btnBackListDetailsPhoto)
                    btnBack.callOnClick()
                }
                "backMenuToReport" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackMenuReport)
                    backBtn.callOnClick()
                }

                "backEnrollToReport" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackFilterReportEn)
                    backBtn.callOnClick()
                }

                "BackHistoryFilterToHomeFragment" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackHistoryFilter)
                    backBtn.callOnClick()
                }

                "BackFilter" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackFilterAuditHistory)
                    backBtn.callOnClick()
                }

                "ET032Fragment" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackET032)
                    backBtn.callOnClick()
                }

                "ET029Fragment" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackET029)
                    backBtn.callOnClick()
                }

                "ET030Fragment" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackET030)
                    backBtn.callOnClick()
                }

                "FailsGeneralErrorFragment" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackFailGeneralError)
                    backBtn.callOnClick()
                }

                "btnBackItemCheckInOut" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackItemCheckInOut)
                    backBtn.callOnClick()
                }

                "backMenuReportStationFree" -> {
                    val backBtn = findViewById<Button>(R.id.btnBackListReportStationFree)
                    backBtn.callOnClick()
                }
            }
            unlockedMenu()
        }
        if (view == "backToSuper") {
            unlockedMenu()
        }
    }

    private fun unlockedMenu() {
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = binding.drawerLayout
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val botonModoColaboradorr = findViewById<Button>(R.id.botonModoColaboradorr)
        botonModoColaboradorr.visibility = View.GONE
        drawerLayout.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.setNavigationIcon(R.drawable.ic_menu)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)
    }

    override fun showLanding(isShowing: Boolean) {
        val newFragment = EnterpriceDetailsFragment(false)
        newFragment.isCancelable = false
        newFragment.show(supportFragmentManager, "game")
    }

    override fun setTextToolbar(text: String) {
        binding.toolbarNav.venturesLogo.isVisible = text.isEmpty()
        binding.toolbarNav.title.isVisible = text.isNotEmpty()
        binding.toolbarNav.title.text = text
    }
}
