package com.venturessoft.human.utils

 class DataZones
 {
     var idZona:Int? = null
     var descripcion:String? = null
     fun getIdZonas(): String {
         return idZona.toString()
     }
     fun setIdZonas(idZona: Int?) {
         this.idZona = idZona
     }
     fun getDescripcionn(): String {
         return descripcion.toString()
     }
     fun setDescripcionn(descripcion: String) {
         this.descripcion = descripcion
     }
 }

