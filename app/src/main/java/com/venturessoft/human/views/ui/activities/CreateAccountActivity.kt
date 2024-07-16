package com.venturessoft.human.views.ui.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.ActivityAdminDataBinding
import com.venturessoft.human.models.request.ConsultaTokeNoLoginRequest
import com.venturessoft.human.views.ui.fragments.createAccount.CreateAccountInterface
import com.venturessoft.human.views.ui.fragments.createAccount.DataUserFragment
import com.venturessoft.human.views.ui.viewModels.CreateAccountViewModel

class CreateAccountActivity : AppCompatActivity(), CreateAccountInterface {

    private lateinit var binding: ActivityAdminDataBinding
    private var adminDataActivity = CreateAccountViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDataBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initToolbar()
        loadToken()
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerCreateAccount, DataUserFragment(), "")
            .commit()
    }
    private fun initToolbar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolBar.setNavigationOnClickListener {
            validateFragments()
        }
    }

    override fun loadFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerCreateAccount, fragment, tag)
            .addToBackStack(tag)
            .commit()
        Log.i("Fragments",this.supportFragmentManager.fragments.size.toString())
    }
    override fun showLoading(isShowing: Boolean) {
        binding.progress.root.isVisible = isShowing
        /* enabledBack = !isShowing*/
        if (isShowing) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }
    private fun loadToken() {
        val requestConsultaTokenNoLogin = ConsultaTokeNoLoginRequest()
        adminDataActivity.getConsultaTokenNoLogin(requestConsultaTokenNoLogin)
    }
    private fun validateFragments(){
        if (supportFragmentManager.backStackEntryCount == 0){
            finish()
        }else{
            supportFragmentManager.popBackStack()
        }
    }

    override fun onBackPressed() {
        validateFragments()
    }
}
