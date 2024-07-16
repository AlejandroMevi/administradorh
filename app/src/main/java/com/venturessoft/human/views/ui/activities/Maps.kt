package com.venturessoft.human.views.ui.activities

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.venturessoft.human.R
import com.venturessoft.human.databinding.ActivityMaps2Binding
private lateinit var binding: ActivityMaps2Binding
class Maps : AppCompatActivity(), OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private lateinit var fusedLocationClient: FusedLocationProviderClient
        var latitud: Double = 0.0
        var longitud: Double = 0.0
    }
    private val TAG: String = Maps::class.java.simpleName

    private lateinit var mMap: GoogleMap

    private lateinit var lastLocation: Location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"ON CREATE")
        binding = ActivityMaps2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment


        mapFragment.getMapAsync {
            mMap = it
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.setOnCameraIdleListener(this)
            mMap.setOnMarkerClickListener(this)
            setUpMap()
        }

        goEstacion()

    }

    private fun goEstacion() {
        binding.aceptarCoordenas.setOnClickListener {
            AnimotionLottie.redirect = ""
            finish()

        }
        binding.btnBackCompanyDataA.setOnClickListener {
            AnimotionLottie.redirect = ""
            finish()

        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnCameraIdleListener(this)
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
            }
        }
    }

    override fun onMarkerClick(p0: Marker) = false
    override fun onCameraIdle() {
        mMap.cameraPosition.target.longitude
     //   println("Nueva posicion de la camara: " + mMap.cameraPosition.target.latitude)
       // println("Nueva posicion de la camara: " + mMap.cameraPosition.target.longitude)
        latitud = mMap.cameraPosition.target.latitude
        longitud = mMap.cameraPosition.target.longitude
        binding.charginlatlong.text = "%.4f".format(mMap.cameraPosition.target.latitude) + " , " + "%.4f".format(mMap.cameraPosition.target.longitude)
    }
}
