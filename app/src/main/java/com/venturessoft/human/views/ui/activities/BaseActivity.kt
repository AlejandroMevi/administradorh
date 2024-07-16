package com.venturessoft.human.views.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.util.*

open class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        com.venturessoft.human.utils.Preferences.setLanguage(Locale.getDefault().language)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }
}
