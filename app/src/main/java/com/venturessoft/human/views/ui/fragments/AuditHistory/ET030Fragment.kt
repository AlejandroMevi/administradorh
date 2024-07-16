package com.venturessoft.human.views.ui.fragments.AuditHistory

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentET030Binding
import com.venturessoft.human.models.response.itemAuditoriasDetalle
import com.venturessoft.human.views.ui.fragments.Stations.WorkaroundMapFragment

class ET030Fragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener {
    private var movimiento: String? = ""
    var latitud: Double? = null
    var longitud: Double? = null
    private var mListener: OnFragmentInteractionListener? = null
    private var auditoriaDet: ArrayList<itemAuditoriasDetalle>? = null
    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private lateinit var fusedLocationClient: FusedLocationProviderClient
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }
    private lateinit var binding : FragmentET030Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentET030Binding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapDetails) as SupportMapFragment
        mapFragment.getMapAsync(this)
        (mapFragment as WorkaroundMapFragment).setListener(object : WorkaroundMapFragment.OnTouchListener {
            override fun onTouch() {
                binding.scrollET030.requestDisallowInterceptTouchEvent(true)
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mListener?.onBack("ET030Fragment")
        loadData()
        listenButtons()
    }

    private fun listenButtons() {
        binding.btnBackET030.setOnClickListener {
            loadFragment(ListAudtHistoryFragment())
        }
    }

    private fun loadData() {
        auditoriaDet = ListAudtHistoryFragment.detallesResponse.auditoriaDet
        loadMovimiento()
        binding.usernameText.text = if (auditoriaDet!![0].nombreEmp.contains("null")) auditoriaDet!![0].nombreEmp.replace("null","") else auditoriaDet!![0].nombreEmp
        binding.movText.text = movimiento + " " + auditoriaDet!![0].fechaMov
        binding.txtDateCheck.text = auditoriaDet!![0].fechaChecada
        binding.idNumCompany.text = auditoriaDet!![0].numCia.toString()
        binding.idNumEmp.text = auditoriaDet!![0].numEmp.toString()
        latitud = auditoriaDet!![0].latitud.toDouble()
        longitud = auditoriaDet!![0].longitud.toDouble()
        binding.txtBeaconsCheck.text = auditoriaDet!![0].beacon01
        binding.device.text = auditoriaDet!![0].dispositivo
        if (auditoriaDet!![0].beacon01.isEmpty()){
            binding.imgBeacon.visibility = View.GONE
        }

        binding.txtET.text = "${auditoriaDet!![0].codigoEtime} : "
        binding.txtCodigo.text = txtCodigo(auditoriaDet!![0].codigoEtime)

    }

    private fun loadGPSCheck() {
        val currentLatLng = LatLng(latitud!!, longitud!!)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
        val markerCheck = MarkerOptions()
        markerCheck.position(currentLatLng)
        markerCheck.title(getString(R.string.details_audit_marking_location))
        mMap.addMarker(markerCheck)
        loadGPSStatation()
    }

    private fun loadGPSStatation() {
        for (i in 0 until auditoriaDet!![0].estaciones!!.size) {
            val currentLatLng = LatLng(auditoriaDet!![0].estaciones!![i].latitud.toDouble(), auditoriaDet!![0].estaciones!![i].longitud.toDouble())
            val markerItems = MarkerOptions()
            markerItems.position(currentLatLng)
            val bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            markerItems.title(auditoriaDet!![0].estaciones!![i].nombre)
            markerItems.icon(bitmapDescriptor)
            markerItems.snippet(getString(R.string.details_audit_label_radio)+" "+ auditoriaDet!![0].estaciones!![i].radio+ " "+getString(R.string.details_audit_label_distancia)+" "+ auditoriaDet!![0].estaciones!![i].distancia)
            mMap.addMarker(markerItems)
        }
    }

    private fun loadMovimiento() {
        movimiento = if (auditoriaDet!![0].tipo == "E") {
            getString(R.string.checkin)
        } else {
            getString(R.string.checkout)
        }
    }

    private fun loadFragment(fragmet: Fragment) {
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, fragmet, "WelcomeFragment")
                .commit()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.setOnCameraIdleListener(this)
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener {
            //   println("Se dio un click en el mapa")
        }
        setUpMap()

    }

    override fun onMarkerClick(p0: Marker): Boolean {
        return false
    }

    override fun onCameraIdle() {

    }

    private fun setUpMap() {
        validatePermission()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
    }

    private fun txtCodigo(code: String): String {
        val contextoPaquete: String = requireActivity().packageName
        val identificadorMensaje = this.resources.getIdentifier(code, "string", contextoPaquete)
        return if (identificadorMensaje > 0) {
            getString(identificadorMensaje)
        } else {
            code
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                validatePermission()
            }
        }
    }


    private fun validatePermission() {
        if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            loadGPSCheck()


            fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    lastLocation = location
                    /*  val currentLatLng = LatLng(location.latitude, location.longitude)
                      mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
                      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))*/
                }
            }
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

    }
}