/**
 * @author TEAM MOBILE VENTURESSOF
 * @version 1.0.0
 */
package com.venturessoft.human.repository

import com.venturessoft.human.firebase.EventVariableFirebase
import com.venturessoft.human.firebase.EventsCustomFirebase
import java.io.IOException
/**
 * Esta clase es para la captura de excepciones.
 */
open class EventFirebaseCatch {
    /**
     * Este metodo es para detectar los errores del servidor y validar la excepcion que nos interesa
     * y enviarla a Firebase para ser analizada.
     * Si es una exception exitosa se envia log a Firebase
     * @param t Throwable se importa la pila de error para detectar el timeout  del servidor u otros errores.
     * @param isCanceled Boolean Este parametro es para detectar si el usuario cancelo el servicio.
     */
    fun sendCustomEventCatch(t: Throwable?, nameEvent: String) {
        var sendNameParam = ""

        if (t is IOException) sendNameParam = EventVariableFirebase.NAME_TIMEOUT

        if (sendNameParam != "") EventsCustomFirebase.eventCustomGeneral(nameEvent) {
            param(EventVariableFirebase.PARAMETER_WS_ADMDES_MESSAGE_ERROR, sendNameParam)
        }
    }
    /**
     * Se detecta el error 500 para el json incorrecto.
     * Nota: Si se desea cambiar o agregar codigos cambiar el nombre del evento.
     * @param statusCode : Codigo de error de la peticion del web services.
     * @param eventWSSend : Nombre del evento que se desea monitoriar.
     */
    fun validateHTTPStatusCode500 (statusCode: Int, eventWSSend: String) {
        if (statusCode >= 500) {
            EventsCustomFirebase.eventCustomGeneral(eventWSSend) {
                param(EventVariableFirebase.PARAMETER_WS_ADMDES_MESSAGE_ERROR, EventVariableFirebase.NAME_CORRUPTDATA)
            }
        }
    }
}