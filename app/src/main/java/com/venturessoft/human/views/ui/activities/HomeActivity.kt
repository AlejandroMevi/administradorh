package com.venturessoft.human.views.ui.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.venturessoft.human.R
import com.venturessoft.human.databinding.ActivityHomeBinding
import com.venturessoft.human.utils.LoadingAnimationCallback
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.WelcomeFragmentCallback


open class HomeActivity : BaseActivity(), WelcomeFragmentCallback, LoadingAnimationCallback{

    private lateinit var binding: ActivityHomeBinding
    private lateinit var progressDialog: ProgressDialog

    companion object {
        var firstTime = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolBar as Toolbar?)
        Utilities.setupUI(binding.activityMain, this)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayShowHomeEnabled(false)
        progressDialog = ProgressDialog(this)

        when (!canWrite) {
            true -> allowWritePermission()
            false -> {
                val curBrightnessValue =
                    Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)

                if (curBrightnessValue < 74) {
                    setBrightness(75)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.toolBar.navigationDrawerButton.setBackgroundColor(getColor(R.color.colorPrimary))
    }
    override fun goToWelcomeFragmentAndClearStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun showLoadingAnimation() {
        binding.loadingAnimation.root.isVisible = true
    }

    override fun hideLoadingAnimation() {
        binding.loadingAnimation.root.isVisible = false
    }

    override fun showLoadingMessage(message: String) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setMessage(message)
        progressDialog.isIndeterminate = true
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    override fun hideLoadingMessage() {
        progressDialog.dismiss()
    }

    override fun onPause() {
        super.onPause()
        when (canWrite) {
            true -> {
                val curBrightnessValue =
                    Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
                if (curBrightnessValue < 74) {
                    setBrightness(75)
                }
            }

            else -> {}
        }
    }
}

val Context.canWrite: Boolean
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(this)
        } else {
            true
        }
    }

fun Context.allowWritePermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val intent = Intent(
            Settings.ACTION_MANAGE_WRITE_SETTINGS,
            Uri.parse("package:$packageName")
        )
        startActivity(intent)
    }
}

val Context.brightness: Int
    get() {
        return Settings.System.getInt(
            this.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS,
            0
        )
    }

fun Context.setBrightness(value: Int) {
    Settings.System.putInt(
        this.contentResolver,
        Settings.System.SCREEN_BRIGHTNESS,
        value
    )
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
