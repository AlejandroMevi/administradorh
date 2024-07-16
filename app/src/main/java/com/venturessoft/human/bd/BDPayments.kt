package com.venturessoft.human.bd

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BDPayments(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 3) {
    companion object {
        const val DATABASE_NAME = "bd_etime"
        const val PAYMENTS = "tbl_payments"
        const val COL_1 = "idAdmin"
        const val COL_2 = "total"
        const val COL_3 = "subtotal"
        const val COL_4 = "fechaCompra"
        const val COL_5 = "folio"
        const val COL_6 = "checadasCompradas"
        const val COL_7 = "cvePais"
        const val TBL_LANGUAGE = "tbl_languege"
        const val COL1 = "lenguaje"
        const val COL2 = "Pais"
        const val COL3 = "idUser"
        const val COL4 = "report"
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS $PAYMENTS(ID INTEGER PRIMARY KEY AUTOINCREMENT , idAdmin  Int , total TEXT, subtotal TEXT, fechaCompra TEXT, folio TEXT, checadasCompradas TEXT, cvePais TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TBL_LANGUAGE(ID INTEGER PRIMARY KEY AUTOINCREMENT , idAdmin  Int , lenguaje TEXT, Pais TEXT, idUser TEXT, report TEXT)")
    }
    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $PAYMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TBL_LANGUAGE")
        onCreate(db)
    }
    fun insertarPayment(
        idAdmin: String,
        subtota: String,
        total: String,
        fechaCompra: String,
        folio: String,
        checadasCompradas: String,
        cvePais: String
    ): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_1, idAdmin)
        cv.put(COL_2, subtota)
        cv.put(COL_3, total)
        cv.put(COL_4, fechaCompra)
        cv.put(COL_5, folio)
        cv.put(COL_6, checadasCompradas)
        cv.put(COL_7, cvePais)
        val res = db.insert(PAYMENTS, null, cv)
        db.close()
        return !res.equals(-1)
    }
    fun insertarSettings(
        lenguaje: String,
        Pais: String,
        idUser: String,
        lenguajeReport: String
    ): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL1, lenguaje)
        cv.put(COL2, Pais)
        cv.put(COL3, idUser)
        cv.put(COL4, lenguajeReport)
        val res = db.insert(TBL_LANGUAGE, null, cv)
        db.close()
        return !res.equals(-1)
    }
    fun getNumTotalRegistros(idAdmin: Long): Int {
        val db = this.writableDatabase
        val registros = db.rawQuery("SELECT * FROM $PAYMENTS  WHERE idAdmin = $idAdmin", null)
        val num: Int = registros.count
        registros.close()
        return num
    }
    fun getPorFechaHora(idAdmin: Long): Cursor {
        val db = this.writableDatabase
        return db.rawQuery(
            "SELECT * FROM $PAYMENTS  WHERE idAdmin = $idAdmin ORDER BY fechaCompra ASC LIMIT 1",
            null
        )
    }
    fun daleteChecadaPor(id: String): Int {
        val db = this.writableDatabase
        return db.delete(PAYMENTS, "ID =? ", arrayOf(id))
    }
    fun getNumTotalRegistrosSettings(idAdmin: String): Int {
        val db = this.writableDatabase
        val registros = db.rawQuery("SELECT * FROM $TBL_LANGUAGE  WHERE idUser = $idAdmin", null)
        val num: Int = registros.count
        registros.close()
        return num
    }
    fun getPorIdUser(id: String): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM $TBL_LANGUAGE WHERE idUser = \'$id\'", null)
    }
    fun editCountryAndLanguage(
        country: String,
        language: String,
        idUsuario: String,
        lenguajeReport: String
    ) {
        val db = this.writableDatabase
        val cursroUdpate: Cursor
        val sqConsulta =
            "UPDATE $TBL_LANGUAGE SET lenguaje ='$language', Pais = '$country', report = '$lenguajeReport' WHERE idUser = '$idUsuario'"
        cursroUdpate = db.rawQuery(sqConsulta, null)
        cursroUdpate.moveToFirst()
        cursroUdpate.close()
    }
}