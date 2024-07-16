package com.venturessoft.human.views.ui.fragments.AuditHistory

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.venturessoft.human.R
import com.venturessoft.human.utils.Utilities

class ViewImageActivity : AppCompatActivity() {
    private lateinit var idImg: ImageView
    private lateinit var btnBack: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)
        initView()
    }

    private fun initView() {
        castData()
        loadData()
        listenButtons()
    }

    private fun listenButtons() {
        btnBack.setOnClickListener {
            this.finish()
        }
    }

    private fun castData() {
        idImg = this.findViewById(R.id.idImg) as ImageView
        btnBack = this.findViewById(R.id.btnBack) as Button
    }

    private fun loadData() {
        if (intent.getStringExtra("photo") != null) {
            val photo = intent.getStringExtra("photo")
            val photoBitmap = Utilities.Base64StringToBitmap(photo!!)
            idImg.setImageBitmap(photoBitmap)

        }

    }

}