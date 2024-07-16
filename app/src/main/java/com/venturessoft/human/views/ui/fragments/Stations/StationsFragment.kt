package com.venturessoft.human.views.ui.fragments.Stations

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
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
import com.google.gson.Gson
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentRegisterStationsBinding
import com.venturessoft.human.models.request.AltaEstacionRequest
import com.venturessoft.human.models.request.BusquedaZonaRequest
import com.venturessoft.human.models.response.AltaEstacionResponse
import com.venturessoft.human.models.response.BusquedaZonaResponse
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.activities.AnimotionLottie
import com.venturessoft.human.views.ui.activities.Maps
import com.venturessoft.human.views.ui.viewModels.StationsFragmentViewModel

class StationsFragment : androidx.fragment.app.Fragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener,
    SearchView.OnQueryTextListener {
    private var mListener: OnFragmentInteractionListener? = null
    private var stationsFragmentViewModel = StationsFragmentViewModel()
    private var altaEstacionResponse = AltaEstacionResponse()
    private var busquedaZonaResponse = BusquedaZonaResponse()
    private var mainInterface: MainInterface? = null
    var user: User? = null
    var uMedidaSpinner: String = "MX"
    var idZonas: Int? = null
    private var list_of_items = arrayOf("VenturesSoft")
    private var disableMaps = false
    private lateinit var mapFragment: SupportMapFragment

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    private var PLACE_PICKER_REQUEST: Int = 1

    companion object {
        //Maps
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private lateinit var fusedLocationClient: FusedLocationProviderClient
    }

    //Variables de Mapas
    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location

    private lateinit var binding: FragmentRegisterStationsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val arrayAdapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, list_of_items)
        // Set layout to use when the list of choices appear
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Utilities.setupUI(mainLayout, requireActivity())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterStationsBinding.inflate(inflater, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.mapStation) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.searchMaps.setOnQueryTextListener(this)
        (mapFragment as WorkaroundMapFragment).setListener(object :
            WorkaroundMapFragment.OnTouchListener {
            override fun onTouch() {
                binding.headScroll.requestDisallowInterceptTouchEvent(true);binding.frameLMapsNew
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.statusFreeStationNew.isChecked = false
        loadButtons()
        loadRequestListZones()
        addObserver()
        mListener?.onBack("backButtonEstationMethod")
        binding.lat.setText(Maps.latitud.toString())
        binding.lon.setText(Maps.longitud.toString())
        //  println("OnViewCreate")
        binding.lat.isLongClickable = false
        binding.lon.isLongClickable = false

    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.season_add))

        if (AnimotionLottie.redirect == "ListStation") {
            AnimotionLottie.redirect = ""
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, ListEstacionesFragment(), "ListEstacionesFragment")
                .commit()

        }
        binding.lat.setText("%.4f".format(Maps.latitud))
        binding.lon.setText("%.4f".format(Maps.longitud))
    }

    override fun onDetach() {
        super.onDetach()
        mainInterface = null
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

    @SuppressLint("ClickableViewAccessibility")
    private fun loadButtons() {
        binding.btnGuardarEstaciones.setOnClickListener {
            //val description = binding.descestacion.text.toString()
            val checkFields = checkFields(
                binding.descestacion.text.toString(),
                binding.uuid.text.toString(),
                binding.rango.text.toString()
            )
            if (checkFields == "OK") {
                val request = AltaEstacionRequestData()
                Log.i("AltaEstacion", "AltaEstacionRequest: " + Gson().toJson(request))
                Log.e("AltaEstacion", "Hola aqui se dio de alta una estacion")
                stationsFragmentViewModel.getAltaEstacion(request)

            } else {
                Toast.makeText(requireActivity(), checkFields, Toast.LENGTH_SHORT).show()


            }
        }
        binding.statusFreeStationNew.setOnClickListener {
            this.isEnableFreeStationComponents(!binding.statusFreeStationNew.isChecked)
        }
        binding.btnBackEstaciones.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, ListEstacionesFragment(), "WelcomeFragment")
                .commit()
        }
        binding.openSuggestions.setOnClickListener {
        }



        binding.searchEditext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.searchEditext.text.isNotEmpty()) {
                    binding.searchEditext.setCompoundDrawables(null, null, null, null)
                } else {
                    binding.searchEditext.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.quantum_ic_search_grey600_24,
                        0,
                        0,
                        0
                    )
                }
            }
        })
        binding.searchEditext.setOnTouchListener { v, event ->
            if (disableMaps.not()) {
                disableMaps = true
                loadPlaces()
            }
            v?.onTouchEvent(event) ?: true
        }
    }

    private fun isEnableFreeStationComponents(isEnable: Boolean) {
        binding.frameLMapsNew.isEnabled = isEnable
        binding.llMapsNew.visibility = if (isEnable) View.VISIBLE else View.INVISIBLE
        binding.searchEditext.isEnabled = isEnable
        binding.lat.isEnabled = isEnable
        binding.lon.isEnabled = isEnable
        binding.rango.isEnabled = isEnable
        binding.selectType.isEnabled = isEnable
        binding.uuid.isEnabled = isEnable
        binding.bssid.isEnabled = isEnable
    }

    private fun AltaEstacionRequestData(): AltaEstacionRequest {
        val latitud = binding.lat.text.toString().replace(",", ".").toFloat()
        val longitud = binding.lon.text.toString().replace(",", ".").toFloat()

        var request = AltaEstacionRequest(
            idUsuario = User.idUsuario,
            idZona = idZonas!!.toInt(),
            clvUnidad = "",
            uuid = "*",
            bssid = "*",
            latitud = 0.0f,
            longitud = 0.0f,
            rango = 0.0f,
            nombre = binding.descestacion.text.toString(),
            estacionLibre = binding.statusFreeStationNew.isChecked
        )
        if (!binding.statusFreeStationNew.isChecked) {
            request = AltaEstacionRequest(
                idUsuario = User.idUsuario,
                idZona = idZonas!!.toInt(),
                clvUnidad = uMedidaSpinner,
                uuid = binding.uuid.text.toString().ifEmpty { null },
                bssid = binding.bssid.text.toString().ifEmpty { null },
                latitud = latitud,
                longitud = longitud,
                rango = binding.rango.text.toString().toFloat(),
                nombre = binding.descestacion.text.toString(),
                estacionLibre = binding.statusFreeStationNew.isChecked
            )
        }

        return request
    }

    private fun loadZones() {
        val listZoneName = arrayListOf<String>()
        val listZonesId = arrayListOf<Int>()
        val listZonesStatus = arrayListOf<String>()
        val list1 = arrayListOf<SearchModel>()

        for (i in 0 until busquedaZonaResponse.totalRegistros) {
            val datamodel = SearchModel()
            datamodel.setIdd(busquedaZonaResponse.sZona!![i].idZona.toString())
            datamodel.setNames(busquedaZonaResponse.sZona!![i].descripcion)
            datamodel.setStatuss(busquedaZonaResponse.sZona!![i].estatus)
            list1.add(datamodel)
            listZonesId.add(i, busquedaZonaResponse.sZona!![i].idZona!!)
            listZoneName.add(i, busquedaZonaResponse.sZona!![i].descripcion)
            listZonesStatus.add(i, busquedaZonaResponse.sZona!![i].estatus)

        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            listZoneName
        )
        val adapteruMedida = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, Utilities.uMedida)

        binding.selectType.setAdapter(adapteruMedida)
        binding.selectZone.setAdapter(adapter)
        binding.selectZone.setOnItemClickListener { parent, view, position, id ->
            parent.getItemAtPosition(position).toString()
            listZoneName.indexOf(parent.getItemAtPosition(position).toString())
            idZonas =
                listZonesId[listZoneName.indexOf(parent.getItemAtPosition(position).toString())]
        }
        binding.selectType.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> uMedidaSpinner = "MTR"
                1 -> uMedidaSpinner = "YRD"
                2 -> uMedidaSpinner = "FT"
            }

        }

    }

    private fun checkFields(descestacionn: String, uuid: String, nrango: String): String {
        if (descestacionn.isBlank() || descestacionn.isEmpty()) {

            return getString(R.string.error_station_data_name_empty)
        }
        if (!binding.statusFreeStationNew.isChecked) {
            if (nrango.isBlank() || nrango.isEmpty()) {
                return getString(R.string.error_station_data_range_empty)
            }
            if (binding.lat.text.toString() == "0.0" && binding.lon.text.toString() == "0.0") {
                return getString(R.string.error_station_data_latlon_empty)
            }
        }
        if (idZonas == null) {
            return getString(R.string.error_station_data_zone_empty)
        }

        return "OK"
    }

    //Inicializa el Mapa
    override fun onMapReady(googleMap: GoogleMap) {
        // println("OnMapReady")
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.setOnCameraIdleListener(this)
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }

    //Carga el mapa deacuerdo a la localizacion obtenida
    private fun setUpMap() {
        //   println("setUpMap")
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
        //   println("Camera onCameraIdle")
        mMap.cameraPosition.target.longitude
        // println("Nueva posicion de la camara: " + mMap.cameraPosition.target.latitude)
        //println("Nueva posicion de la camara: " + mMap.cameraPosition.target.longitude)
        Maps.latitud = mMap.cameraPosition.target.latitude
        Maps.longitud = mMap.cameraPosition.target.longitude
        // Toast.makeText(activity!!, "Latitud: " + mMap.cameraPosition.target.latitude + " Longitud: " + mMap.cameraPosition.target.longitude, Toast.LENGTH_SHORT).show()
        val latitud = requireActivity().findViewById<TextView>(R.id.lat)
        val longitud = requireActivity().findViewById<TextView>(R.id.lon)

        binding.lat.setText("%.4f".format(mMap.cameraPosition.target.latitude))
        binding.lon.setText("%.4f".format(mMap.cameraPosition.target.longitude))
        //  charginlatlong.text =  "%.4f".format(mMap.cameraPosition.target.latitude)+" , "+"%.4f".format(mMap.cameraPosition.target.longitude)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        loadPlaces()
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        //AutoComplete
        //AutoComplete
        loadPlaces()
        val locationn: String = binding.searchMaps!!.query.toString()
        val addressList: List<Address>
        if (locationn != null || locationn != "") {
            val geocoder = Geocoder(requireContext())
            addressList = geocoder.getFromLocationName(locationn, 5) as List<Address>
            if (addressList.isNotEmpty()) {
                val address = addressList[0]
                //  println("Latitud Busqueda : " + address.latitude)
                // println("Longitud Busqueda : " + address.longitude)
                val latLng = LatLng(address.latitude, address.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            } else {
                Toast.makeText(requireActivity(), "Sugerencia", Toast.LENGTH_SHORT).show()
            }
        }
        return false
    }

    private fun loadPlaces() {
        Places.initialize(requireActivity(), getString(R.string.apiKey))
        val fields = arrayListOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(requireActivity())
        startActivityForResult(intent, PLACE_PICKER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == -1) {
                //  println("Codigo de resultado : " + resultCode)
                val place: Place = Autocomplete.getPlaceFromIntent(data!!)
                if (place != null) {
                    mMap.clear()
                    //    println("Latitud Places: " + place.latLng!!.latitude)
                    //  println("Longitud: " + place.latLng!!.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(place.latLng!!))
                    val markerOption = MarkerOptions()
                    val lat = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)
                    markerOption.position(lat)
                    markerOption.title(place.name)

                    binding.searchEditext.setText(place.name.toString())
                    mMap.addMarker(markerOption)

                } else {
                    binding.searchEditext.setText("")
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.text_non_closed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // activity!!.finish();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                //println("Cancel Error Code")

            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
                //println("RESULT_CANCELED")
            }
            disableMaps = false
        }
    }

    fun addObserver() {
        stationsFragmentViewModel.altaEstacionResponseMutableData.observe(viewLifecycleOwner) {
            altaEstacionResponse = it
            if (altaEstacionResponse.codigo == "ET000") {
                successRegisterStation()
            } else {
                Utilities.loadMessageError(
                    altaEstacionResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }

        stationsFragmentViewModel.busquedaZonaResponseMutableData.observe(viewLifecycleOwner) {
            busquedaZonaResponse = it
            if (busquedaZonaResponse.codigo == "ET000") {
                loadZones()
            } else {
                //Utilities.loadMessageError(busquedaZonaResponse.codigo, requireActivity())
            }
        }

        stationsFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        stationsFragmentViewModel.codeError.observe(
            viewLifecycleOwner
        ) {
            loadServerMessageError(it)
        }

    }

    private fun hideLoadingAnimation() {
        binding.loadingAnimationStationFragment.root.visibility = View.INVISIBLE
    }

    private fun showLoadingAnimation() {
        binding.loadingAnimationStationFragment.root.visibility = View.GONE
    }

    private fun successRegisterStation() {
        val intent = Intent(requireActivity(), AnimotionLottie::class.java)
        intent.putExtra(Constants.FROM_LOG_OUT, true)
        intent.putExtra("Redirect", "ListStation")
        startActivity(intent)
    }

    private fun loadRequestListZones() {
        val requestListZones = BusquedaZonaRequest(User.idUsuario)
        stationsFragmentViewModel.getBusquedaZonas(requestListZones)
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
