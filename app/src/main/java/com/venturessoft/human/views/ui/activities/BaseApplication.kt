package com.venturessoft.human.views.ui.activities

import android.content.Context
import android.os.Build
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.security.ProviderInstaller
import com.google.firebase.analytics.FirebaseAnalytics
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class BaseApplication : MultiDexApplication() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()

        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        io.github.inflationx.calligraphy3.CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/DIN_Medium.ttf")
                                .setFontAttrId(io.github.inflationx.calligraphy3.R.attr.fontPath)
                                .build()))
                .build())
        // TODO: check this if it's the best solution
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            initializeSSLContext(applicationContext)
        }

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)


    }

    fun initializeSSLContext(mContext: Context) {
        try {
            ProviderInstaller.installIfNeeded(mContext)
        } catch (e: GooglePlayServicesRepairableException) {
            // Indicates that Google Play services is out of date, disabled, etc.
            // Prompt the user to install/update/enable Google Play services.
            GooglePlayServicesUtil.showErrorNotification(
                    e.connectionStatusCode, mContext)
            return

        } catch (e: GooglePlayServicesNotAvailableException) {
            GooglePlayServicesUtil.showErrorNotification(
                    e.errorCode, mContext)
            // Indicates a non-recoverable error; the ProviderInstaller is not able
            // to install an up-to-date Provider.
            e.printStackTrace()
            return
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
