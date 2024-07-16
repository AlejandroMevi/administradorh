package com.venturessoft.human.views.ui.fragments.welcome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.estimote.coresdk.cloud.internal.ApiUtils.getSharedPreferences
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentAvisoPrivacidadDialogBinding
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.utils.User
import com.venturessoft.human.views.ui.activities.LoginActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection


class AvisoPrivacidadDialog : DialogFragment(), OnPageChangeListener {

    private lateinit var binding: FragmentAvisoPrivacidadDialogBinding
    private lateinit var pdfUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAvisoPrivacidadDialogBinding.inflate(inflater, container, false)
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setWindowAnimations(R.style.AppTheme_Slide)
        }
        pdfUrl = User.urlAvisoPriv
        downloadAndLoadPDF()
        listenCheck()
        initButtons()
        return binding.root
    }

    private fun listenCheck() {
        binding.checkTerms.setOnCheckedChangeListener { _, isChecked ->
            binding.btnConfirmar.isEnabled = isChecked
        }
    }

    private fun initButtons() {
        binding.btnConfirmar.setOnClickListener {
            val preferencias = getSharedPreferences(requireContext())
            val editor = preferencias.edit()
            editor.putBoolean(Constants.AVISO, true)
            editor.apply()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            dismiss()
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun downloadAndLoadPDF() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = java.net.URL(pdfUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()
                val inputStream = connection.inputStream

                val file = File(requireContext().cacheDir, "temp.pdf")
                val outputStream = FileOutputStream(file)

                inputStream.copyTo(outputStream)
                outputStream.flush()
                outputStream.close()
                inputStream.close()

                val uri = Uri.fromFile(file)
                loadPDF(uri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadPDF(uri: Uri) {
        binding.pdfView.fromUri(uri)
            .onPageChange(this@AvisoPrivacidadDialog)
            .pageFitPolicy(FitPolicy.WIDTH)
            .load()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        if (page + 1 == pageCount) {
            showCheckboxAndButton(true)
        } else {
            showCheckboxAndButton(false)
        }
    }
    private fun showCheckboxAndButton(show: Boolean) {
        if (show){
            binding.checkTerms.visibility = View.VISIBLE
            binding.linearButtons.visibility = View.VISIBLE
        }else{
            binding.checkTerms.visibility = View.GONE
            binding.linearButtons.visibility = View.GONE
        }

    }
}