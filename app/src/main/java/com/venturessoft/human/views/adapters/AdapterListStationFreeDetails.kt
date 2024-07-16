package com.venturessoft.human.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.venturessoft.human.models.response.EstacionLibreDetalleResponse
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.venturessoft.human.R
import com.venturessoft.human.views.ui.fragments.ReportFragment.ReportDetailsStationFree
import com.google.android.gms.maps.model.CameraPosition
import kotlin.random.Random


class AdapterListStationFreeDetails(context: Context, private val items: MutableList<EstacionLibreDetalleResponse>, fm: FragmentManager, fragment: Fragment) : RecyclerView.Adapter<AdapterListStationFreeDetails.VH>(), OnMapReadyCallback {

    private val arrayData = ArrayList<EstacionLibreDetalleResponse>()
    private val inflater: LayoutInflater
    private var fragmentManager: FragmentManager? = null
    private var fragmentSuper: Fragment? = null
    private var context: Context? = null
    private var isCreateMap = true
    private lateinit var mapFrag: SupportMapFragment
    private var holder: VH? = null

    init {
        this.inflater = LayoutInflater.from(context)
        this.arrayData.addAll(ReportDetailsStationFree.listStationFreeDetails)
        this.fragmentManager = fm
        this.fragmentSuper = fragment
        this.context = context
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {
        return VH(LayoutInflater.from(context).inflate(R.layout.row_station_free_details, parent, false))
    }
    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        this.holder = holder
        val view = holder.mapLayout
        val stationFree = LatLng(holder.latitud!!, holder.longitud!!)
        val options = GoogleMapOptions()
        options.camera(CameraPosition(stationFree, 15f, 14f, 90f))
        options.scrollGesturesEnabled(false)
        options.scrollGesturesEnabledDuringRotateOrZoom(false)
        options.zoomControlsEnabled(true)
        options.zoomGesturesEnabled(true)
        mapFrag = SupportMapFragment.newInstance(options)
        mapFrag.getMapAsync(this)
        val fm = fragmentSuper!!.childFragmentManager
        fm.beginTransaction().add(view.id, mapFrag).commitNowAllowingStateLoss()
        isCreateMap = false

    }
    override fun onBindViewHolder(holder: VH, position: Int) {

        if (items[position].fotoEmployee.toString().isEmpty().not()) {
            val imagenBase64 = Base64.decode(items[position].fotoEmployee.toString(), Base64.DEFAULT)
            val imagenconverBitmap = BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
            holder.imgAvatarEmployeeStation.setImageBitmap(imagenconverBitmap)
        } else {
            holder.imgAvatarEmployeeStation.setImageDrawable(ContextCompat.getDrawable(this.context!!, R.drawable.profile_photo))
        }
        holder.txtNameStation.text = items[position].nombreEstacion.toString()
        holder.txtIdEmployee.text = items[position].idEmployee.toString()
        holder.txtDateCheckStation.text = items[position].fechaChecada.toString()
        holder.txtNameEmployee.text = items[position].nameEmployee.toString()
        if (items[position].tipo.toString() == "E"){
            holder.txtNameTypeStation.text = this.context!!.getString(R.string.checkin)
        }else{
            holder.txtNameTypeStation.text = this.context!!.getString(R.string.checkout)
        }
        holder.latitud = items[position].latitud!!.toDouble()
        holder.longitud = items[position].longitud!!.toDouble()
    }

    override fun getItemCount(): Int = items.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var mapLayout: FrameLayout
        lateinit var imgAvatarEmployeeStation: ImageView
        lateinit var txtNameStation: TextView
        lateinit var txtIdEmployee: TextView
        lateinit var txtDateCheckStation: TextView
        lateinit var txtNameEmployee: TextView
        lateinit var txtNameTypeStation: TextView
        private var fragmentManager: FragmentManager? = null
        private var fragment: Fragment? = null
        private lateinit var fusedLocationClient: FusedLocationProviderClient
        var latitud: Double? = 0.0
        var longitud: Double? = 0.0
        lateinit var mMap: GoogleMap

        init {
            init(itemView)
        }
        @SuppressLint("SetTextI18n")
        private fun init(itemView: View) {
            imgAvatarEmployeeStation = itemView.findViewById(R.id.imgAvatarEmployeeStation)
            txtNameStation = itemView.findViewById(R.id.txtViewNameStation)
            txtIdEmployee = itemView.findViewById(R.id.txtViewIdEmployee)
            txtDateCheckStation = itemView.findViewById(R.id.txtDateCheckStation)
            txtNameEmployee = itemView.findViewById(R.id.txtViewNameEmployee)
            txtNameTypeStation = itemView.findViewById(R.id.txtViewNameTypeStation)
            mapLayout = itemView.findViewById(R.id.fragmentLayoutStationFree)
            mapLayout.id = Random.nextInt(1, 10000)

        }
        fun bind(fm: FragmentManager, fragment: Fragment, context: Context, nameStation: String, nameEmployee: String, idEmployee: String, dateCheck: String, latitud: String, longitud: String, photo: String, typeStation: String) = with(this.itemView) {
            this@VH.fragmentManager = fm
            this@VH.fragment = fragment
            this@VH.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context.applicationContext!!)
            this@VH.latitud = latitud.toDouble()
            this@VH.longitud = longitud.toDouble()

            val imgAvatarEmployeeStation = this.findViewById<ImageView>(R.id.imgAvatarEmployeeStation)
            val txtNameStation = this.findViewById<TextView>(R.id.txtViewNameStation)
            val txtIdEmployee = this.findViewById<TextView>(R.id.txtViewIdEmployee)
            val txtDateCheckStation = this.findViewById<TextView>(R.id.txtDateCheckStation)
            val txtNameEmployee = this.findViewById<TextView>(R.id.txtViewNameEmployee)
            val txtNameTypeStation = this.findViewById<TextView>(R.id.txtViewNameTypeStation)
            mapLayout = this.findViewById(R.id.fragmentLayoutStationFree)

            if (photo.isEmpty().not()) {
                val imagenBase64 = Base64.decode(photo, Base64.DEFAULT)
                val imagenconverBitmap = BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
                imgAvatarEmployeeStation.setImageBitmap(imagenconverBitmap)
            } else {
                imgAvatarEmployeeStation.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile_photo))
            }

            txtNameStation.text = nameStation
            txtIdEmployee.text = idEmployee
            txtDateCheckStation.text = dateCheck
            txtNameEmployee.text = nameEmployee
            txtNameTypeStation.text = typeStation

        }
    }

    override fun onMapReady(p0: GoogleMap) {
        this.holder!!.mMap = p0
        val stationFree = LatLng(this.holder!!.latitud!!, this.holder!!.longitud!!)
        this.holder!!.mMap.addMarker(MarkerOptions().position(stationFree))
        this.holder!!.mMap.moveCamera(CameraUpdateFactory.newLatLng(stationFree))
        this.holder!!.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(stationFree, 18f))

        if (ActivityCompat.checkSelfPermission(context!!.applicationContext!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                (this.context as Activity?)!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
        this.holder!!.mMap.isMyLocationEnabled = true
        this.holder!!.mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

}


