package com.venturessoft.human.views.ui.fragments.Stations

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentEditStationsBinding
import com.venturessoft.human.models.request.ActualizaEstacionRequest
import com.venturessoft.human.models.response.ActualizaEstacionResponse
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.bd.DBDemo
import com.venturessoft.human.views.ui.activities.AnimotionLottie
import com.venturessoft.human.views.ui.activities.Maps
import com.venturessoft.human.views.ui.viewModels.EditStationsFragmentViewModel

class EditStationsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnCameraIdleListener {
    private var editStationsFragmentViewModel = EditStationsFragmentViewModel()
    private var actualizaEstacionResponse = ActualizaEstacionResponse()
    private var defaultZoneSelected = true
    var defaultUMedida = true
    private var mapaDisabled = true
    private var mListener: OnFragmentInteractionListener? = null
    var user: User? = null
    lateinit var myDataBase: DBDemo
    var positionSpnner: Int = 0
    private val PLACE_PICKER_REQUEST: Int = 1
    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var initialName: String
    private var mainInterface: MainInterface? = null
    var idZonaSelected: Int? = null
    var uMedida: String = "FT"
    private var nameEstationDefault: String = ""
    var idZona: String = ""
    var latitud: Double? = null
    var longitud: Double? = null
    var rango: Float? = null
    var uuid: String = ""
    var bssid: String = ""
    var clvUnidad: String = ""
    var status: String = ""
    private var freeStation: Boolean = false
    private var disableMaps = false
    var locationDefaultActivate: Boolean = false

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private lateinit var fusedLocationClient: FusedLocationProviderClient
        var idStation: Int? = null
    }

    private lateinit var binding: FragmentEditStationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditStationsBinding.inflate(inflater, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapStationEdit) as SupportMapFragment
        mapFragment.getMapAsync(this)
        (mapFragment as WorkaroundMapFragment).setListener(object :
            WorkaroundMapFragment.OnTouchListener {
            override fun onTouch() {
                locationDefaultActivate = false
                binding.scrollEditStation.requestDisallowInterceptTouchEvent(true)
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onBack("backListStation")
        loadDefaultData()
        loadListZones()
        addObserver()
        activeBack()
        listenButtons()
        binding.editsearchEditext.setOnClickListener {
            binding.editsearchEditext.isClickable = false
            if (disableMaps.not()) {
                disableMaps = true
                loadPlaces()
            }
        }

        binding.editsearchEditext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.editsearchEditext.text.isNotEmpty()) {
                } else {
                    binding.editsearchEditext.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.quantum_ic_search_grey600_24,
                        0,
                        0,
                        0
                    );
                }
            }
        })
        this.configFreeStation()
    }

    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.station_edit))

        if (AnimotionLottie.redirect == "Mapa") {
            val intent = Intent(activity, Maps::class.java)
            startActivity(intent)
        }
        if (AnimotionLottie.redirect == "ListEstacion") {
            AnimotionLottie.redirect = ""
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, ListEstacionesFragment(), "WelcomeFragment")
                .commit()
        }
        if (!this.locationDefaultActivate) {
            binding.latEdit.setText("%.4f".format(Maps.latitud))
            binding.lonEdit.setText("%.4f".format(Maps.longitud))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
        if (context is MainInterface) {
            mainInterface = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }

    private fun loadDefaultData() {
        if (arguments != null) {
            nameEstationDefault = requireArguments().getString("nameEstationEdit")!!
            initialName = requireArguments().getString("nameEstationEdit")!!
            idZona = requireArguments().getString("idZona")!!
            latitud = requireArguments().getDouble("latitud")
            longitud = requireArguments().getDouble("longitud")
            rango = requireArguments().getFloat("rango")
            if (requireArguments().getString("uuid") != null) {
                uuid = requireArguments().getString("uuid")!!
            }
            if (requireArguments().getString("bssid") != null) {
                bssid = requireArguments().getString("bssid")!!
            }
            clvUnidad = requireArguments().getString("clvUnidad")!!
            status = requireArguments().getString("status")!!
            freeStation = requireArguments().getBoolean("estacionLibre")
            locationDefaultActivate = true
            setDataServer()
        }
        binding.latEdit.isLongClickable = false
        binding.lonEdit.isLongClickable = false
        if (User.tipoUsuario == 2 && User.estaciones == true) {
            lockData()
        }
    }

    private fun lockData() {
        binding.nameEstationEdit.isEnabled = false
        binding.editsearchEditext.isEnabled = false
        binding.selectEstacionesEdit.isEnabled = false
        binding.latEdit.isEnabled = false
        binding.lonEdit.isEnabled = false
        binding.rangoEdit.isEnabled = false
        binding.selectTypeEdit.isEnabled = false
        binding.uuidEdit.isEnabled = false
        binding.bssiddEdit.isEnabled = false
        binding.btnGuardarEstacionesEdit.isEnabled = false
        binding.btnGuardarEstacionesEdit.visibility = View.GONE
    }

    private fun listenButtons() {
        binding.btnGuardarEstacionesEdit.setOnClickListener {
            val checkFields = checkFields()
            if (checkFields == "OK") {
                val requestActualizaEstaciones = loadRequestActualizarEst()
                editStationsFragmentViewModel.getActualizaEstacionService(requestActualizaEstaciones)
            } else {
                Toast.makeText(requireActivity(), checkFields, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkFields(): String {
        if (binding.nameEstationEdit.text.toString()
                .isBlank() || binding.nameEstationEdit.text.toString().isEmpty()
        ) {
            return getString(R.string.error_station_data_name_empty)
        }
        if (binding.rangoEdit.text.isNullOrEmpty()) {
            return getString(R.string.error_station_data_range_empty)
        }
        if (binding.latEdit.text.toString() == "0.0" && binding.lonEdit.text.toString() == "0.0") {
            return getString(R.string.error_station_data_latlon_empty)
        }
        if (idZona == null) {
            return getString(R.string.error_station_data_zone_empty)
        }
        return "OK"
    }

    private fun addObserver() {
        editStationsFragmentViewModel.actualizaEstacionResponseMutableData.observe(
            viewLifecycleOwner
        ) {
            actualizaEstacionResponse = it
            if (actualizaEstacionResponse.codigo == "ET000") {
                updateSuccess()
            } else {
                Utilities.loadMessageError(
                    actualizaEstacionResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        editStationsFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        editStationsFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
    }

    private fun updateSuccess() {
        val dialogLogout = DialogGeneral(
            getString(R.string.good),
            getString(R.string.successful_change),
            null,
            null,
            {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.Fragpricipal, ListEstacionesFragment(), "WelcomeFragment")
                    .commit()
            }
        )
        dialogLogout.show(childFragmentManager, "")
    }

    private fun loadListZones() {
        val listZonesName = arrayListOf<String>()
        val listZonesId = arrayListOf<String>()
        for (i in 0 until ListEstacionesFragment.listZones!!.size) {
            listZonesName.add(i, ListEstacionesFragment.listZones!![i].descripcion)
            listZonesId.add(i, ListEstacionesFragment.listZones!![i].idZona.toString())
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            listZonesName
        )
        binding.selectEstacionesEdit.setAdapter(adapter)
        binding.selectEstacionesEdit.setOnItemClickListener { parent, view, position, id ->
            positionSpnner = listZonesId[listZonesName.indexOf(
                parent.getItemAtPosition(position).toString()
            )].toInt()
            idZonaSelected = positionSpnner
        }

        if (defaultZoneSelected) {
            val positionDefaultZone = listZonesId.indexOf(idZonaSelected.toString())
            binding.selectEstacionesEdit.setText(listZonesName[positionDefaultZone], false)
            defaultZoneSelected = false
        } else {
            idZonaSelected = positionSpnner
        }
        binding.selectTypeEdit.setOnItemClickListener { _, _, position, _ ->
            if (!defaultUMedida) {
                when (position) {
                    0 -> uMedida = "MTR"
                    1 -> uMedida = "YRD"
                    2 -> uMedida = "FT"
                }
            }
        }

        val adapterMedidas = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            Utilities.uMedida
        )
        binding.selectTypeEdit.setAdapter(adapterMedidas)
        if (defaultUMedida) {
            val item = Utilities.uMedidaCode.indexOf(uMedida)
            binding.selectTypeEdit.setText(Utilities.uMedida[item], false)
            defaultUMedida = false
        }
    }

    private fun setDataServer() {
        binding.nameEstationEdit.setText(nameEstationDefault)
        binding.latEdit.setText(latitud.toString())
        binding.lonEdit.setText(longitud.toString())
        binding.rangoEdit.setText(rango.toString())
        binding.uuidEdit.setText(uuid)
        binding.bssiddEdit.setText(bssid)
        idZonaSelected = idZona.toInt()
        uMedida = clvUnidad
    }

    private fun configFreeStation() {
        binding.statusFreeStation.setOnClickListener {
            this.isEnableFreeStationComponents(!binding.statusFreeStation.isChecked)
        }
        binding.statusFreeStation.isChecked = freeStation
        this.isEnableFreeStationComponents(!freeStation)
    }

    private fun isEnableFreeStationComponents(isEnable: Boolean) {
        binding.frameMapsEdit.visibility = if (isEnable) View.VISIBLE else View.INVISIBLE
        binding.latEdit.isEnabled = isEnable
        binding.lonEdit.isEnabled = isEnable
        binding.rangoEdit.isEnabled = isEnable
        binding.selectTypeEdit.isEnabled = isEnable
        binding.uuidEdit.isEnabled = isEnable
        binding.bssiddEdit.isEnabled = isEnable
    }

    private fun activeBack() {
        binding.btnBackListEstaciones.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, ListEstacionesFragment(), "ListaEstaciones")
                .commit()
        }
    }

    //Mapas
    //Inicializa el Mapa
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.setOnCameraIdleListener(this)
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener {
            mapaDisabled = false
        }
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
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
        if (locationDefaultActivate) {
            loadDefaultLocation()
        } else {
            mMap.cameraPosition.target.longitude
            Maps.latitud = mMap.cameraPosition.target.latitude
            Maps.longitud = mMap.cameraPosition.target.longitude
            binding.latEdit.setText("%.4f".format(mMap.cameraPosition.target.latitude))
            binding.lonEdit.setText("%.4f".format(mMap.cameraPosition.target.longitude))
        }
    }

    private fun loadDefaultLocation() {
        val currentLatLng = LatLng(latitud!!, longitud!!)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
        binding.latEdit.setText("%.4f".format(latitud))
        binding.lonEdit.setText("%.4f".format(longitud))
    }

    private fun loadPlaces() {
        locationDefaultActivate = false
        Places.initialize(requireActivity(), getString(R.string.apiKey))
        val fields = arrayListOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = Autocomplete
            .IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(requireActivity())
        startActivityForResult(intent, PLACE_PICKER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == -1) {
                val place: Place = Autocomplete.getPlaceFromIntent(data!!)
                if (place != null) {
                    mMap.clear()
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(place.latLng!!))
                    val markerOption = MarkerOptions()
                    val lat = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)
                    markerOption.position(lat)
                    markerOption.title(place.name)
                    binding.editsearchEditext.setText(place.name.toString())
                    mMap.addMarker(markerOption)
                    binding.editsearchEditext.isClickable = true
                    disableMaps = false
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.text_non_closed),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.editsearchEditext.isClickable = true
                    disableMaps = false
                    binding.editsearchEditext.setText("")
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                binding.editsearchEditext.setText("")
                binding.editsearchEditext.isClickable = true
                disableMaps = false
            }
            disableMaps = false
        }
    }

    fun showLoadingAnimation() {
        binding.loadingAnimationEditStation.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideLoadingAnimation() {
        binding.loadingAnimationEditStation.root.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

    }

    private fun loadRequestActualizarEst(): ActualizaEstacionRequest {
        val latitud = binding.latEdit.text.toString().replace(",", ".").toDouble()
        val longitud = binding.lonEdit.text.toString().replace(",", ".").toDouble()
        val request: ActualizaEstacionRequest?
        if (binding.statusFreeStation.isChecked) {
            request = ActualizaEstacionRequest(
                idUsuario = User.idUsuario,
                idEstacion = idStation!!.toInt(),
                idZona = idZonaSelected,
                clvUnidad = "",
                uuid = "*",
                bssid = "*",
                latitud = 0.0,
                longitud = 0.0,
                rango = 0.0,
                nombre = if (binding.nameEstationEdit.text.toString().isBlank().not()) {
                    binding.nameEstationEdit.text.toString()
                } else {
                    "*"
                },
                status = status,
                estacionLibre = binding.statusFreeStation.isChecked
            )
        } else {
            request = ActualizaEstacionRequest(
                idUsuario = User.idUsuario,
                idEstacion = idStation!!.toInt(),
                idZona = idZonaSelected,
                clvUnidad = uMedida,
                uuid =
                if (binding.uuidEdit.text.toString().isBlank().not()) {
                    binding.uuidEdit.text.toString()
                } else {
                    "*"
                },
                bssid = if (binding.bssiddEdit.text.toString().isBlank().not()) {
                    binding.bssiddEdit.text.toString()
                } else {
                    "*"
                },
                latitud = latitud,
                longitud = longitud,
                rango = binding.rangoEdit.text.toString().toDouble(),
                nombre =
                if (binding.nameEstationEdit.text.toString().isBlank().not()) {
                    binding.nameEstationEdit.text.toString()
                } else {
                    "*"
                },
                status = status,
                estacionLibre = binding.statusFreeStation.isChecked
            )
        }
        return request
    }

    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = requireActivity().packageName
        val identificadorMensaje =
            this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(
                requireActivity().getString(identificadorMensaje),
                requireActivity(),
                childFragmentManager
            )
        } else {
            Utilities.showDialog(code.toString(), requireActivity(), childFragmentManager)
        }
    }
}
