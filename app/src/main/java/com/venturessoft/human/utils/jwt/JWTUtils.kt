/**
 * @author Team Mobile Android (Eduardo Solis, Alejandro Mejia, Rafael Villa)
 * @verion 1.0
 * @see android.os.Parcelable
 * Versión inicial de la clase
 */

package com.venturessoft.human.utils.jwt

import androidx.annotation.Nullable

import android.os.Parcel
import android.util.Base64

import android.os.Parcelable
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.venturessoft.human.utils.exceptions.DecodeException
import org.jetbrains.annotations.NotNull
import java.lang.Exception
import java.lang.IllegalArgumentException

import java.util.Date
import java.nio.charset.Charset
import java.lang.reflect.Type
import kotlin.math.floor

/**
 * Clase que transforma y decodifca el JWT en modelo JSON
 */
class JWTUtils(@NotNull private val tokenJWT: String): Parcelable {

    private val TAG: String = JWTUtils::class.java.simpleName

    /**
     * Variables de metodo privado
     */
    private var header: Map<String, String>? = null
    private var payload: JWTPayload? = null
    private var signature: String? = null

    /**
     * Metodo que retorna el header del JWT
     * @return header, retorna header del token desencriptado
     */
    fun getHeader(): Map<String, String>? {
        return header
    }

    /**
     * Metodo que retorna el signature del JWT
     * @return signature, retorna signature del token desencriptado en formato string
     */
    fun getSignature(): String? {
        return signature
    }

    /**
     * Metodo que retorna el payload del JWT
     * @return payload, retorna el iss del payload del token desencriptado
     */
    fun getIssuer(): String? {
        return payload!!.iss
    }

    /**
     * Metodo que retorna el payload del JWT
     * @return sub, retorna el sub del payload  del token desencriptado
     */
    fun getSubject(): String? {
        return payload!!.sub
    }

    /**
     * Metodo que retorna el payload del JWT
     * @return aud, retorna el aud del payload  del token desencriptado
     */
    fun getAudience(): List<String?>? {
        return payload!!.aud
    }

    /**
     * Metodo que retorna el payload del JWT
     * @return exp, retorna el exp del payload del token desencriptado
     */
    fun getExpiresAt(): Date? {
        return payload!!.exp
    }

    /**
     * Metodo que retorna el payload del JWT
     * @return nbf, retorna el nbf del payload del token desencriptado
     */
    fun getNotBefore(): Date? {
        return payload!!.nbf
    }

    /**
     * Metodo que retorna el payload del JWT
     * @return iat, retorna el iat del payload del token desencriptado
     */
    fun getIssuedAt(): Date? {
        return payload!!.iat
    }

    /**
     * Metodo que retorna el payload del JWT
     * @return jti, retorna el jti del payload del token desencriptado
     */
    fun getId(): String? {
        return payload!!.jti
    }

    /**
     * Se inicializa el constructor de la clase llamando a la funcion decode
     */
    init {
        decode(tokenJWT)
    }

    /**
     * Inicializa constructor de la clase Parceable
     */
    constructor(parcel: Parcel) : this(parcel.readString().toString()) {
        signature = parcel.readString()
    }

    /**
     * Metodo que evalua la expiración del token
     * Se obtiene el exp del payload y se multiplica por 1000 milisegundos para determinar
     * si el tiempo esta en el rango de fecha actual
     * @param leeway variable que contenga el tiempo actual en tipo long
     * @return true si el token expiro false si el token esta activo
     */
    fun isExpired(leeway: Long): Boolean {
        require(leeway >= 0) { "The leeway must be a positive value." }
        val todayTime =
            (floor((Date().time / 1000).toDouble()) * 1000).toLong()
        Log.e(TAG,"VER CUANTO VALE $todayTime")
        val futureToday = Date(todayTime + leeway * 1000)
        val pastToday = Date(todayTime - leeway * 1000)
        val expValid = payload!!.exp == null || !pastToday.after(payload!!.exp)
        val iatValid = payload!!.iat == null || !futureToday.before(payload!!.iat)
        return !expValid || !iatValid
    }

    /*
     * Metodos de tipo privados
     */

    /**
     * Este metodo decpdifica el token y se almacena en las variables
     *  Payload
     *  Header
     *  Signature
     *  @param token que se desea decodificar
     *
     */
    private fun decode(token: String) {
        val isBearer = token.replace("Bearer ", "")
        val partsJWT = splitToken(isBearer)
        val mapType: Type = object : TypeToken<Map<String?, String?>?>() {}.type
        header = parseJson( base64Decode(partsJWT?.get(0).toString()), mapType)
        Log.d(TAG, "JWT Token Header: $header")
        payload = parseJson(base64Decode(partsJWT?.get(1).toString()), JWTPayload::class.java)
        Log.d(TAG, "JWT Token Payload: $payload")
        signature = partsJWT?.get(2)
        Log.d(TAG, "JWT Token signature: $signature")
    }

    /**
     * Metodo que divide el token con la funcion split
     * @param token
     * @return un array con las 3 partes del token
     */
    private fun splitToken(token: String): Array<String?>? {
        var parts: Array<String?> = token.split("\\.".toRegex()).toTypedArray()
        if (parts.size == 2 && token.endsWith(".")) {
            parts = arrayOf(parts[0], parts[1], "")
        }
        if (parts.size != 3) {
            throw DecodeException(
                String.format(
                    "The token was expected to have 3 parts, but got %s.",
                    parts.size
                )
            )
        }
        return parts
    }

    /**
     * Metodo que decodica en token en formato Base64
     * @param string
     * @return el formato de token decodificado
     */
    @Nullable
    private fun base64Decode(string: String): String {
        val decoded: String = try {
            val bytes: ByteArray =
                Base64.decode(string, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
            String(bytes, Charset.defaultCharset())
        } catch (e: IllegalArgumentException) {
            throw DecodeException("Received bytes didn't correspond to a valid Base64 encoded string.", e)
        }
        return decoded
    }

    /**
     * Metodo que parse el string en formato JSON
     * @param json se solicita esta varialble del json decodificado
     * @param typeOfT tipo del fomato del json
     * @return un generico (T) con los parametros del payload del token
     */
    private fun <T> parseJson(json: String, typeOfT: Type): T {
        val payload: T = try {
            getGson().fromJson(json, typeOfT)
        } catch (e: Exception) {
            throw DecodeException("The token's payload had an invalid JSON format.", e)
        }
        return payload
    }

    /**
     * Metodo que deserealiza el token en el modelo de payload
     * @see JWTPayload
     */
    private fun getGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(JWTPayload::class.java, JWTDeserializer())
            .create()
    }

    /*
    Metodos sobreesctricos de la clase Parcelable
     */

    /**
     * Metodo sobreescrito de la clase android.os.Parcelable que serealiza el token y signature
     * @param parcel
     * @param flags
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tokenJWT)
        parcel.writeString(signature)
    }

    /**
     * Metodo sobreescrito de la clase android.os.Parcelable
     * @return int
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * Metodos de la clase Parcelable CREATOR
     */
    companion object CREATOR : Parcelable.Creator<JWTUtils> {

        /*
         Metodos sobre escritos de la clase Parcelable
         */

        /**
         * Metodo sobre escrito
         * @param parcel
         * @return JWTUtils
         */
        override fun createFromParcel(parcel: Parcel): JWTUtils {
            return JWTUtils(parcel)
        }

        /**
         * Metodo sobre escrito
         * @param size
         * @return Array<JWTUtils?>
         */
        override fun newArray(size: Int): Array<JWTUtils?> {
            return arrayOfNulls(size)
        }
    }

}