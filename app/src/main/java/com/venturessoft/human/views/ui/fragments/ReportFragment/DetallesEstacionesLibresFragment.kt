package com.venturessoft.human.views.ui.fragments.ReportFragment

import  android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentDetallesEstacionesLibresBinding
import com.venturessoft.human.models.Response.DetailsFreeStationResponse
import com.venturessoft.human.models.Response.EstacionesEmpItem
import com.venturessoft.human.views.ui.fragments.AuditHistory.ViewImageFragment

class DetallesEstacionesLibresFragment(
    private val estacionesEmpItem: EstacionesEmpItem,
    private val responseDetails: DetailsFreeStationResponse? = null,
    private val a: Double? = null,
    private val b: Double? = null
) :
    DialogFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentDetallesEstacionesLibresBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private var photoCheck: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetallesEstacionesLibresBinding.inflate(inflater, container, false)
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setWindowAnimations(R.style.AppTheme_Slide)
        }
        loadData(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar()
        listenButtons()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun toolbar() {
        with(binding) {
            toolbar.setNavigationOnClickListener { dismiss() }
            toolbar.title = getString(R.string.detalle_del_reporte)
        }
    }

    private fun listenButtons() {
        binding.headerUser.image.setOnClickListener {
            if (photoCheck!!.isNotEmpty()) {
                viewImageD(photoCheck!!)

            } else {
                viewImageD(null)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadData(savedInstanceState: Bundle?) {
        val numero = estacionesEmpItem.numEmp?.toLong()
        try {
            if (responseDetails?.detalle?.get(0)?.fotografia?.contains(
                    "File not foundjava",
                    ignoreCase = true
                ) == true || responseDetails?.detalle?.get(0)?.fotografia?.isEmpty() == true
            ) {
                binding.headerUser.image.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.user_icon_big
                    )
                )
            } else {
                val imagenBase64 =
                    Base64.decode(responseDetails?.detalle?.get(0)?.fotografia, Base64.DEFAULT)
                val imagenconverBitmap =
                    BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
                binding.headerUser.image.setImageBitmap(imagenconverBitmap)
                photoCheck = responseDetails?.detalle?.get(0)?.fotografia
            }
        } catch (e: Exception) {
            println(e)
        }

        binding.headerUser.position.text =
            if (responseDetails?.detalle?.get(0)?.puesto.isNullOrEmpty()) "" else responseDetails?.detalle?.get(
                0
            )?.puesto
        binding.headerUser.name.text = estacionesEmpItem.nombreEmp
        binding.headerUser.idEmploye.text = numero.toString()

        binding.headerUser.date.text =
            "${estacionesEmpItem.fechaChecada} ${requireContext().getString(R.string.a_las)} ${estacionesEmpItem.horaChecada}"
        binding.headerUser.bssid.text = estacionesEmpItem.estacion
        binding.headerUser.status.text =
            if (estacionesEmpItem.tipo == "E") getString(R.string.entrada) else getString(R.string.salida)
        if (a != null) {
            latitud = a
        }
        if (b != null) {
            longitud = b
        }
        initMap(savedInstanceState)
    }

    private fun initMap(savedInstanceState: Bundle?) {
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        println("Latitud y longitud $latitud $longitud")
        val lugarLatLng = LatLng(latitud, longitud)
        googleMap.addMarker(MarkerOptions().position(lugarLatLng).title("Estacion libre"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lugarLatLng, 19f))
        googleMap.uiSettings.isZoomControlsEnabled = true
    }
    private fun viewImageD(photo: String?) {
        val fullScreenDialogFragment = ViewImageFragment(photo)
        fullScreenDialogFragment.show(
            requireActivity().supportFragmentManager,
            "FullScreenDialogFragment"
        )
    }
}