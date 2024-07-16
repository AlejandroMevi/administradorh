package com.venturessoft.human.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.StateListDrawable
import android.location.LocationManager
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.provider.Settings
import android.util.Base64
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.R
import com.venturessoft.human.views.ui.activities.CreateAccountActivity
import com.venturessoft.human.views.ui.fragments.ReportFragment.ReportsMenuFragment
import org.joda.time.Days
import org.joda.time.LocalDate
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Utilities {
    enum class CheckType {
        UpdatePhoto
    }
    companion object {
        fun isTimeAutomaticEnabled(context: Context): Boolean {
            return Settings.Global.getInt(context.contentResolver, Settings.Global.AUTO_TIME, 0) == 1
        }
        fun compareDates(dateStart: String, dateToday: String): Int {
            return try {
                val start = LocalDate.parse(dateStart)
                val today = LocalDate.parse(dateToday)
                val remainingDays = Days.daysBetween(start, today).days
                remainingDays
            } catch (_: Exception) {
                val start = LocalDate.parse(dateStart)
                val today = LocalDate.parse(getDateLocal() ?: "")
                val remainingDays = Days.daysBetween(start, today).days
                remainingDays
            }
        }
        fun base64StringToBitmap(base64: String): Bitmap {
            val imageBytes = Base64.decode(base64, 0)
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }
        @SuppressLint("SimpleDateFormat")
        fun getDateLocal(): String? {
            return try {
                val calendar = Calendar.getInstance(Locale.getDefault())
                val format1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val requiredSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                return sourceSdf.parse(format1.format(calendar.time))?.let { requiredSdf.format(it) }
            } catch (_: Exception) {
                ""
            }
        }
        fun isConnected(context: Activity): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netinfo = cm.activeNetworkInfo
            return if (netinfo != null && netinfo.isConnectedOrConnecting) {
                val wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                val mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                (mobile != null && mobile.isConnectedOrConnecting || wifi != null && wifi.isConnectedOrConnecting)
            } else {
                true
            }
        }
        private fun isLocationProviderActivated(context: Context): Pair<Boolean, Boolean> {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gpsEnabled: Boolean =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val networkEnabled: Boolean =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            return Pair(gpsEnabled, networkEnabled)
        }
        fun isLocationServiceActivated(context: Context): Boolean {
            val (gps, network) = isLocationProviderActivated(context)
            return gps || network
        }
        fun showDialog(message: String, context: Context,supportFragmentManager: FragmentManager) {
            val dialogLogout = DialogGeneral(context.getString(R.string.errormissing), message)
            dialogLogout.show(supportFragmentManager,"")
        }
        private fun hideKeyboard(activity: Activity) {
            val view = activity.currentFocus
            if (view != null) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
        fun addSelector(
            activity: Context,
            imageSelectedId: Int,
            imageUnselectedId: Int
        ): StateListDrawable {
            val states = StateListDrawable()
            states.addState(
                intArrayOf(android.R.attr.state_pressed),
                activity.resources.getDrawable(imageSelectedId)
            )
            states.addState(
                intArrayOf(android.R.attr.state_focused),
                activity.resources.getDrawable(imageSelectedId)
            )
            states.addState(intArrayOf(), activity.resources.getDrawable(imageUnselectedId))
            return states
        }
        fun setupUI(view: View?, activity: Activity) {
            if (view !is EditText) {
                view?.setOnTouchListener { v, event ->
                    hideKeyboard(activity)
                    false
                }
            }
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val innerView = view.getChildAt(i)
                    setupUI(innerView, activity)
                }
            }
        }
        fun bitmapToBase64(bitmap: Bitmap, quality: Int = 80): String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return byteArrayToBase64(byteArray)
        }
        private fun byteArrayToBase64(array: ByteArray): String {
            return Base64.encodeToString(array, Base64.NO_WRAP)
        }
        fun scaleImageKeepAspectRatio(bitmap: Bitmap): Bitmap {
            val maxSize = 375
            val width = bitmap.width
            val height = bitmap.height
            val newHeight: Int
            val newWidth: Int
            if (height > width && height > maxSize) {
                newHeight = maxSize
                newWidth = (width * maxSize) / height
            } else if (width > height && width > maxSize) {
                newHeight = (height * maxSize) / width
                newWidth = maxSize
            } else {
                newHeight = height
                newWidth = width
            }
            return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)
        }
        fun loadMessageError(
            codigo: String,
            activity: Activity,
            supportFragmentManager: FragmentManager
        ) {
            val contextoPaquete: String = activity.packageName
            val identificadorMensaje = activity.applicationContext.resources.getIdentifier(codigo, "string", contextoPaquete)
            if (identificadorMensaje > 0) {
                showDialog(activity.getString(identificadorMensaje), activity,supportFragmentManager)
            } else {
                showDialog(codigo, activity,supportFragmentManager)
            }
        }
        fun dateNextMonth(): String {
            val dateString = LocalDate.now()
            val newDate = dateString.plusMonths(1)
            return newDate.toString("yyyy-MM-dd")
        }
        fun dateExpiredFreeTrail(): String {
            val dateString = LocalDate.now()
            val newDate = dateString.plusDays(15)
            return newDate.toString("yyyy-MM-dd")
        }
        fun dateToday(): String {
            return LocalDate.now().toString("dd/MM/yyyy")
        }
        fun validateConexion(activity: Activity): Boolean {
            val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netinfo = cm.activeNetworkInfo
            return netinfo != null && netinfo.isConnected
        }
        var uMedidaCode = arrayOf("MTR", "YRD", "FT")
        var uMedida = arrayOf("Metros", "Yardas", "Pies")
        var codeCountry = arrayOf(
            "MX",
            "AF",
            "AL",
            "DE",
            "AD",
            "AO",
            "AI",
            "AQ",
            "SA",
            "DZ",
            "AR",
            "AM",
            "AW",
            "AU",
            "AT",
            "AZ",
            "BS",
            "BH",
            "BD",
            "BB",
            "BE",
            "BZ",
            "BJ",
            "BM",
            "BY",
            "MM",
            "BO",
            "BA",
            "BW",
            "BR",
            "BN",
            "BG",
            "BF",
            "BI",
            "BT",
            "CV",
            "KH",
            "CM",
            "CA",
            "CL",
            "CN",
            "CY",
            "CO",
            "KM",
            "KP",
            "KR",
            "CI",
            "CR",
            "HR",
            "CU",
            "CW",
            "DK",
            "DJ",
            "DM",
            "EC",
            "EG",
            "EH",
            "SV",
            "VA",
            "AE",
            "ER",
            "SK",
            "SI",
            "ES",
            "US",
            "EE",
            "ET",
            "PH",
            "FI",
            "FJ",
            "FR",
            "GA",
            "GM",
            "GE",
            "GH",
            "GI",
            "GR",
            "GL",
            "GP",
            "GU",
            "GT",
            "GN",
            "GQ",
            "GW",
            "GY",
            "HT",
            "NL",
            "HN",
            "HK",
            "HU",
            "IN",
            "ID",
            "IQ",
            "IR",
            "IE",
            "IM",
            "NF",
            "IS",
            "KY",
            "CK",
            "FO",
            "FK",
            "MP",
            "MH",
            "PN",
            "SB",
            "VG",
            "IL",
            "IT",
            "JM",
            "JP",
            "JO",
            "KZ",
            "KE",
            "KG",
            "KI",
            "XK",
            "KW",
            "LA",
            "LS",
            "LV",
            "LB",
            "LR",
            "LY",
            "LI",
            "LT",
            "LU",
            "MO",
            "MK",
            "MG",
            "MY",
            "MW",
            "MV",
            "ML",
            "MT",
            "MA",
            "MU",
            "MR",
            "FM",
            "MD",
            "MC",
            "MN",
            "ME",
            "MS",
            "MZ",
            "NA",
            "NR",
            "NP",
            "NI",
            "NE",
            "NG",
            "NU",
            "NO",
            "NC",
            "NZ",
            "OM",
            "PK",
            "PW",
            "PA",
            "PG",
            "PY",
            "PE",
            "PF",
            "PL",
            "PT",
            "PR",
            "QA",
            "GB",
            "CF",
            "CZ",
            "SS",
            "CG",
            "CD",
            "DO",
            "RE",
            "RW",
            "RO",
            "RU",
            "WS",
            "AS",
            "BL",
            "KN",
            "SM",
            "MF",
            "PM",
            "VC",
            "SH",
            "LC",
            "ST",
            "SN",
            "RS",
            "SC",
            "SL",
            "SG",
            "SY",
            "SO",
            "LK",
            "ZA",
            "SD",
            "SE",
            "CH",
            "SR",
            "SZ",
            "TH",
            "TW",
            "TZ",
            "TJ",
            "TL",
            "TG",
            "TK",
            "TT",
            "TN",
            "TM",
            "TR",
            "TV",
            "UA",
            "UG",
            "UY",
            "UZ",
            "VU",
            "VE",
            "VN",
            "YE",
            "ZM",
            "ZW"
        )
        var countryList = arrayOf(
            "México",
            "Afganistán",
            "Albania",
            "Alemania",
            "Andorra",
            "Angola",
            "Anguila",
            "Antártida",
            "Arabia Saudita",
            "Argelia",
            "Argentina",
            "Armenia",
            "Aruba",
            "Australia",
            "Austria",
            "Azerbaiyán",
            "Bahamas",
            "Bahréin",
            "Bangladesh",
            "Barbados",
            "Bélgica",
            "Belice",
            "Benín",
            "Bermudas",
            "Bielorrusia",
            "Birmania (Myanmar)",
            "Bolivia",
            "Bosnia-Herzegovina",
            "Botsuana",
            "Brasil",
            "Brunéi",
            "Bulgaria",
            "Burkina Faso",
            "Burundi",
            "Bután",
            "Cabo Verde",
            "Camboya",
            "Camerún",
            "Canadá",
            "Chile",
            "China",
            "Chipre",
            "Colombia",
            "Comoras",
            "Corea del Norte",
            "Corea del Sur",
            "Costa de Marfil",
            "Costa Rica",
            "Croacia",
            "Cuba",
            "Curazao",
            "Dinamarca",
            "Djibuti",
            "Dominica",
            "Ecuador",
            "Egipto",
            "El Sáhara Español",
            "El Salvador",
            "El Vaticano",
            "Emiratos Árabes Unidos",
            "Eritrea",
            "Eslovaquia",
            "Eslovenia",
            "España",
            "Estados Unidos",
            "Estonia",
            "Etiopía",
            "Filipinas",
            "Finlandia",
            "Fiyi",
            "Francia",
            "Gabón",
            "Gambia",
            "Georgia",
            "Ghana",
            "Gibraltar",
            "Grecia",
            "Groenlandia",
            "Guadalupe",
            "Guam",
            "Guatemala",
            "Guinea",
            "Guinea Ecuatorial",
            "Guinea-Bissáu",
            "Guyana",
            "Haití",
            "Holanda",
            "Honduras",
            "Hong Kong",
            "Hungría",
            "India",
            "Indonesia",
            "Irak",
            "Irán",
            "Irlanda",
            "Isla de Man",
            "Isla Norfolk",
            "Islandia",
            "Islas Caimán",
            "Islas Cook",
            "Islas Feroe",
            "Islas Malvinas",
            "Islas Marianas del Norte",
            "Islas Marshall",
            "Islas Pitcairn",
            "Islas Salomón",
            "Islas Vírgenes Británicas",
            "Israel",
            "Italia",
            "Jamaica",
            "Japón",
            "Jordania",
            "Kazajistán",
            "Kenia",
            "Kirgizistán",
            "Kiribati",
            "Kosovo",
            "Kuwait",
            "Laos",
            "Lesoto",
            "Letonia",
            "Líbano",
            "Liberia",
            "Libia",
            "Liechtenstein",
            "Lituania",
            "Luxemburgo",
            "Macao",
            "Macedonia",
            "Madagascar",
            "Malasia",
            "Malaui",
            "Maldivas",
            "Mali",
            "Malta",
            "Marruecos",
            "Mauricio",
            "Mauritania",
            "Micronesia",
            "Moldavia",
            "Mónaco",
            "Mongolia",
            "Montenegro",
            "Montserrat",
            "Mozambique",
            "Namibia",
            "Nauru",
            "Nepal",
            "Nicaragua",
            "Níger",
            "Nigeria",
            "Niue",
            "Noruega",
            "Nueva Caledonia",
            "Nueva Zelanda",
            "Omán",
            "Pakistán",
            "Palau",
            "Panamá",
            "Papúa Nueva Guinea",
            "Paraguay",
            "Perú",
            "Polinesia Francesa",
            "Polonia",
            "Portugal",
            "Puerto Rico",
            "Qatar",
            "Reino Unido",
            "República Centroafricana",
            "República Checa",
            "República de Sudán del Sur",
            "República del Congo",
            "República Democrática del Congo",
            "República Dominicana",
            "Reunión",
            "Ruanda",
            "Rumanía",
            "Rusia",
            "Samoa",
            "Samoa Americana",
            "San Bartolomé",
            "San Cristóbal y Nevis",
            "San Marino",
            "San Martín",
            "San Pedro y Miquelón",
            "San Vicente y las Granadinas",
            "Santa Elena",
            "Santa Lucía",
            "Santo Tomé y Príncipe",
            "Senegal",
            "Serbia",
            "Seychelles",
            "Sierra Leona",
            "Singapur",
            "Siria",
            "Somalia",
            "Sri Lanka",
            "Sudáfrica",
            "Sudán",
            "Suecia",
            "Suiza",
            "Surinam",
            "Swazilandia",
            "Tailandia",
            "Taiwán",
            "Tanzania",
            "Tayikistán",
            "Timor Oriental",
            "Togo",
            "Tokelau",
            "Trinidad y Tobago",
            "Túnez",
            "Turkmenistán",
            "Turquía",
            "Tuvalu",
            "Ucrania",
            "Uganda",
            "Uruguay",
            "Uzbekistán",
            "Vanuatu",
            "Venezuela",
            "Vietnam",
            "Yemen",
            "Zambia",
            "Zimbabue"
        )
        fun orientacionCamara(
            orientacion: Int,
            matrix: android.graphics.Matrix
        ): android.graphics.Matrix {
            when (orientacion) {
                ExifInterface.ORIENTATION_NORMAL -> matrix.postRotate((0).toFloat())
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale((-1).toFloat(), (1).toFloat())
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate((180).toFloat())
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                    matrix.setRotate((180).toFloat())
                    matrix.postScale((-1).toFloat(), (1).toFloat())
                }
                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    matrix.setRotate((90).toFloat())
                    matrix.postScale((-1).toFloat(), (1).toFloat())
                }
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    matrix.setRotate((90).toFloat())
                }
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    matrix.setRotate((-90).toFloat())
                    matrix.postScale((-1).toFloat(), (1).toFloat())
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    matrix.setRotate((-90).toFloat())
                }
            }
            return matrix
        }
        internal class RecyclerTouchListener(
            context: Context,
            recyclerView: RecyclerView,
            private val clickListener: ReportsMenuFragment.ClickListener?
        ) : RecyclerView.OnItemTouchListener {
            private val gestureDetector: GestureDetector
            init {
                gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                        override fun onSingleTapUp(e: MotionEvent): Boolean {
                            return true
                        }
                        override fun onLongPress(e: MotionEvent) {
                            val child = recyclerView.findChildViewUnder(e.x, e.y)
                            if (child != null && clickListener != null) {
                                clickListener.onLongClick(
                                    child,
                                    recyclerView.getChildAdapterPosition(child)
                                )
                            }
                        }
                    }
                )
            }
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y)
                if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                    clickListener.onClick(child, rv.getChildAdapterPosition(child))
                }
                return false
            }
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        }
        fun Base64StringToBitmap(base64: String): Bitmap {
            val imageBytes = Base64.decode(base64, 0)
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }
        fun cambiarFormatoFecha(inputDate: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = inputFormat.parse(inputDate)
                outputFormat.format(date as Date)
            } catch (_: Exception) {
                ""
            }
        }
        fun loadFragment(activity: Activity, fragment: Fragment, tag: String) {
            (activity as CreateAccountActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerCreateAccount, fragment, tag)
                .commit()
        }
        fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
            observe(lifecycleOwner, object : Observer<T> {
                override fun onChanged(value: T) {
                    observer.onChanged(value)
                    if (value != null) {
                        removeObserver(this)
                    }
                }
            })
        }
        fun removeLastChar(str: String?): String? {
            return str?.replaceFirst(".$".toRegex(), "")
        }
        fun loadFragment(fragmentNow: Fragment, fragmetGo: Fragment) {
            fragmentNow.requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, fragmetGo, "$fragmetGo")
                .commit()
        }
    }
}