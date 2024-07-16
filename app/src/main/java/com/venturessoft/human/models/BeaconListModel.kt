package com.venturessoft.human.models

class BeaconListModel {
    var id: String? = ""
    var name: String? = ""
    var uuid: String? = ""
    fun getIds(): String {
        return id!!.toString()
    }
    fun setIds(id: String) {
        this.id = id
    }
    fun getNames(): String {
        return name!!.toString()
    }
    fun setNamess(name: String) {
        this.name = name
    }
    fun getUuids(): String {
        return uuid!!.toString()
    }
    fun setUuids(uuids: String) {
        this.uuid = uuids
    }
}