package com.venturessoft.human.views.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.content.ContextCompat
import com.venturessoft.human.R
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.fragments.Employe.EmployeeListFragment

abstract class LocationFragment : BaseFragment() {

    protected var locationRunnable: Runnable? = null
    protected lateinit var mLocationListener: LocationListener
    protected var mLocationManager: LocationManager? = null
    protected lateinit var locationCriteria: Criteria
    protected var mHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLocation()
        mHandler = Handler()
    }

    protected open fun initializeLocation() {
        mLocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationCriteria = Criteria()
        locationCriteria.accuracy = Criteria.ACCURACY_COARSE
        locationCriteria.powerRequirement = Criteria.NO_REQUIREMENT
        locationCriteria.isAltitudeRequired = false
        locationCriteria.verticalAccuracy = Criteria.NO_REQUIREMENT
        locationCriteria.isBearingRequired = false
        locationCriteria.isSpeedRequired = false
        locationCriteria.isCostAllowed = true
        locationCriteria.horizontalAccuracy = Criteria.ACCURACY_MEDIUM

        mLocationListener = object : LocationListener {
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }
            override fun onProviderEnabled(provider: String) {
            }
            override fun onProviderDisabled(provider: String) {
            }
            override fun onLocationChanged(location: Location) {
                onLocationFound(location)
            }
        }
    }

    override fun onDestroy() {
        mHandler?.removeCallbacksAndMessages(null)
        locationRunnable = null
        mHandler = null
        super.onDestroy()
    }

    @SuppressLint("MissingPermission")
    protected open fun requestLocation() {
        mLocationManager?.requestSingleUpdate(locationCriteria, mLocationListener, null)
        locationRunnable = Runnable {
            mLocationManager?.removeUpdates(mLocationListener)
            onLocationNotFound()

        }
        mHandler?.postDelayed(locationRunnable!!, Constants.LOCATION_SEARCH_TIMEOUT)
    }
    protected open fun checkPermissionAndRequestLocation() {
        val permissionCheck = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), Constants.PERMISSION_FINE_LOCATION_REQUEST_CODE)
        } else {
            requestLocation()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.PERMISSION_FINE_LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation()
                } else {
                    val dialogLogout = DialogGeneral(
                        getString(R.string.location),
                        getString(R.string.message_location_permission_denied),
                        getString(R.string.ok)
                    )
                    dialogLogout.show(childFragmentManager,"")
                }
                return
            }
        }
    }
    protected abstract fun onLocationFound(location: Location)
    protected abstract fun onLocationNotFound()
}
