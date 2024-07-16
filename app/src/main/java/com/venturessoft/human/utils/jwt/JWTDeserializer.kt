package com.venturessoft.human.utils.jwt

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.venturessoft.human.utils.exceptions.DecodeException

import java.lang.reflect.Type
import java.util.ArrayList
import java.util.Collections
import java.util.Date
internal class JWTDeserializer : JsonDeserializer<JWTPayload?> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): JWTPayload {
        if (json.isJsonNull || !json.isJsonObject) {
            throw DecodeException("The token's payload had an invalid JSON format.")
        }
        val `object`: JsonObject = json.asJsonObject

        val iss = getString(`object`, "iss")
        val sub = getString(`object`, "sub")
        val exp: Date? = getDate(`object`, "exp")
        val nbf: Date? = getDate(`object`, "nbf")
        val iat: Date? = getDate(`object`, "iat")
        val jti = getString(`object`, "jti")
        val aud = getStringOrArray(`object`, "aud")

        return JWTPayload(iss, sub, exp, nbf, iat, jti, aud)
    }
    private fun getStringOrArray(obj: JsonObject, claimName: String): MutableList<String> {
        var list: MutableList<String> = Collections.emptyList()
        if (obj.has(claimName)) {
            val arrElement: JsonElement = obj.get(claimName)
            if (arrElement.isJsonArray) {
                val jsonArr: JsonArray = arrElement.asJsonArray
                list = ArrayList(jsonArr.size())
                for (i in 0 until jsonArr.size()) {
                    list.add(jsonArr.get(i).asString)
                }
            } else {
                list = Collections.singletonList(arrElement.asString)
            }
        }
        return list
    }
    private fun getDate(obj: JsonObject, claimName: String): Date? {
        if (!obj.has(claimName)) {
            return null
        }
        val ms: Long = obj.get(claimName).asLong * 1000
        return Date(ms)
    }
    private fun getString(obj: JsonObject, claimName: String): String? {
        return if (!obj.has(claimName)) {
            null
        } else obj.get(claimName).asString
    }
}