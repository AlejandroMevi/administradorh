package com.venturessoft.human.firebase

import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
class EventsCustomFirebase {
    @JvmField val debugProdFirebase: Boolean = false
    companion object {
        fun eventCustomGeneral(eventName: String, parameters: ParametersBuilder.() -> Unit) {
            if (EventsCustomFirebase().debugProdFirebase) {
                Firebase.analytics.logEvent(eventName, parameters)
            }
        }
    }
}