package com.venturessoft.human.views.ui.fragments.ReportFragment

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.venturessoft.human.R
import com.venturessoft.human.models.response.EstacionLibreDetalleResponse

class ItemReportStationFreeFragment: Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener {

    private var itemStationFree: EstacionLibreDetalleResponse? = null

    private lateinit var imgAvatarEmployeeStation: ImageView
    private lateinit var txtNameStation: TextView
    private lateinit var txtIdEmployee: TextView
    private lateinit var txtDateCheckStation: TextView
    private lateinit var txtNameEmployee: TextView
    private lateinit var txtNameTypeStation: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap

    fun newInstance(itemStationFree: EstacionLibreDetalleResponse) : ItemReportStationFreeFragment {
        val stationFreeFragment = ItemReportStationFreeFragment()
        stationFreeFragment.itemStationFree = itemStationFree
        return stationFreeFragment
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.row_station_free_details, container, false)

        imgAvatarEmployeeStation = view.findViewById(R.id.imgAvatarEmployeeStation)
        if (itemStationFree!!.fotoEmployee.isNullOrEmpty().not()) {
            val imagenBase64 = Base64.decode(itemStationFree!!.fotoEmployee, Base64.DEFAULT)
            val imagenconverBitmap = BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
            imgAvatarEmployeeStation.setImageBitmap(imagenconverBitmap)
        } else {
            imgAvatarEmployeeStation.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.profile_photo))
        }
        txtNameStation = view.findViewById(R.id.txtViewNameStation)
        txtNameStation.text = itemStationFree!!.nombreEstacion.toString()
        txtIdEmployee = view.findViewById(R.id.txtViewIdEmployee)
        txtIdEmployee.text = itemStationFree!!.idEmployee.toString()
        txtDateCheckStation = view.findViewById(R.id.txtDateCheckStation)
        txtDateCheckStation.text = itemStationFree!!.fechaChecada.toString()
        txtNameEmployee = view.findViewById(R.id.txtViewNameEmployee)
        txtNameEmployee.text = itemStationFree!!.nameEmployee.toString()
        txtNameTypeStation = view.findViewById(R.id.txtViewNameTypeStation)
        txtNameTypeStation.text = itemStationFree!!.tipo.toString()
        return view
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(requireActivity().applicationContext!!, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        this.fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
        }
    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val stationFree = LatLng(itemStationFree!!.latitud!!.toDouble(), itemStationFree!!.longitud!!.toDouble())
        mMap.addMarker(MarkerOptions().position(stationFree))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(stationFree))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(stationFree, 18f))
        setUpMap()
    }

    override fun onMarkerClick(p0: Marker): Boolean = false

    override fun onCameraIdle() {}

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}