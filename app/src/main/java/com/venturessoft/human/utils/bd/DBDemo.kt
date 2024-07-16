package com.venturessoft.human.utils.bd


import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBDemo(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    companion object {
        const val DATABASE_NAME = "BD_HUETIMEADMIN"
        const val TABLE_NAME = "HU_ZONA"
        const val TABLE_NAME_ADMIN = "HU_ADMIN"
        const val TABLE_NAME_EMPLOYEE = "HU_EMPLOYE"
        const val TABLE_NAME_EMPLOYE_CHECK = "HU_EMPLOYE_CHECK"
        const val TABLE_NAME_ESTACIONES = "HU_ESTACIONES"
        const val TABLE_NAME_ALTA_SUPER = "HU_ALTA_SUPER"
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME_EMPLOYE_CHECK(ID INTEGER PRIMARY KEY AUTOINCREMENT ,Nempleado TEXT, Fecha TEXT, Hora TEXT, Tipo TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME_EMPLOYEE(ID INTEGER PRIMARY KEY AUTOINCREMENT ,Name TEXT, ApPaterno TEXT, ApMaterno TEXT, numEmp TEXT, station TEXT, admin_id TEXT, estatus TEXT, foto TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME_ADMIN(ID INTEGER PRIMARY KEY AUTOINCREMENT , Email TEXT, Password TEXT, Type TEXT, Companyname TEXT, Companycode TEXT, Bussines TEXT, Rfc TEXT, Address TEXT, Pais TEXT, Phone TEXT, Nombre TEXT, ApPaterno TEXT, ApMaterno TEXT, RfcEmp TEXT, AddresEmp TEXT, Country TEXT, PhoneAdmin TEXT, estado TEXT, lenguaje  TEXT, interiorNumber TEXT, outdoorNumber TEXT,stree TEXT, suburb TEXT,city TEXT, state TEXT, phoneCode TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME(ID INTEGER PRIMARY KEY AUTOINCREMENT , Descripcion TEXT, Estado TEXT )")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME_ESTACIONES(ID INTEGER PRIMARY KEY AUTOINCREMENT , Descripcion  TEXT , UUID TEXT, lat REAL, lon REAL, rango REAL, zona Int, Estado TEXT )")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME_ALTA_SUPER(ID INTEGER PRIMARY KEY AUTOINCREMENT , nombre  TEXT , apPaterno TEXT,  apMaterno TEXT, zona TEXT, status TEXT, password TEXT)")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE $TABLE_NAME_EMPLOYEE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ADMIN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ESTACIONES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ALTA_SUPER")
        onCreate(db)
    }
    fun getPorEstaciones(): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME_ESTACIONES ORDER BY Descripcion DESC ", null)
    }
    fun getNumTotalRegistrosEmployeeSupervisor(): Int {
        val supervisorr = "Super"
        val db = this.writableDatabase
        val sqConsulta = "SELECT * FROM $TABLE_NAME_ADMIN WHERE Type = '$supervisorr'"
        val registros = db.rawQuery(sqConsulta, null)
        return registros.count
    }
}
