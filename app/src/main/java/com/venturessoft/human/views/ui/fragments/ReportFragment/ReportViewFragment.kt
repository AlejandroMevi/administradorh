package com.venturessoft.human.views.ui.fragments.ReportFragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentReportViewBinding
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.coroutines.CoroutineContext

class ReportViewFragment : Fragment(), CoroutineScope {
    private lateinit var binding : FragmentReportViewBinding
    private lateinit var mJob: Job
    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main
    private var mListener: OnFragmentInteractionListener? = null
    private var fileName: String? = "report"
    private var pageIndex = 0
    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null
    private var currentZoomLevel = 5f
    private var parcelFileDescriptor: ParcelFileDescriptor? = null

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mJob = Job()
        pageIndex = 0
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReportViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onBack("backEnrollToReport")
        listenButtons()

    }

    private fun listenButtons() {
        binding.btnLeft.setOnClickListener {
            currentZoomLevel = 5f
            showPage(currentPage!!.index - 1)
        }
        binding.btnRight.setOnClickListener {
            currentZoomLevel = 5f
            showPage(currentPage!!.index + 1)
        }
        binding.btnZoomMin.setOnClickListener {
            --currentZoomLevel
            showPage(currentPage!!.index)
        }
        binding.btnZoomMax.setOnClickListener {
            ++currentZoomLevel
            showPage(currentPage!!.index)
        }
        binding.btnBackFilterReportEn.setOnClickListener {
            loadFragment(Enroll_Unenroll_ReportFragment())
        }
    }

    private fun openRenderer(context: Context) {
        val urlBase = "${context.filesDir}"
        val file = fileName?.let { File(urlBase, it) }
        val dwldsPath = File(urlBase + fileName.toString() + ".pdf")
        if (!dwldsPath.exists()) {
            val asset: InputStream = context.assets.open(fileName!!)
            val output = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var size: Int
            while (asset.read(buffer).also { size = it } != -1) {
                output.write(buffer, 0, size)
            }
            asset.close()
            output.close()
        }
        parcelFileDescriptor = ParcelFileDescriptor.open(dwldsPath, ParcelFileDescriptor.MODE_READ_ONLY)
        if (parcelFileDescriptor != null) {
            pdfRenderer = PdfRenderer(parcelFileDescriptor!!)
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            openRenderer(requireActivity().applicationContext)
            showPage(pageIndex)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showPage(index: Int) {
        launch(Dispatchers.Default) {
            if (pdfRenderer!!.pageCount <= index) {
                return@launch
            }
            if (null != currentPage) {
                currentPage!!.close()
            }
            currentPage = pdfRenderer!!.openPage(index)
            val newWidth = (resources.displayMetrics.widthPixels * currentPage!!.width / 72 * currentZoomLevel / 40)
            val newHeight = (resources.displayMetrics.heightPixels * currentPage!!.height / 72 * currentZoomLevel / 64)
            if (newWidth.toInt() > 0 && newHeight.toInt() > 0) {
                val bitmap = Bitmap.createBitmap(newWidth.toInt(), newHeight.toInt(), Bitmap.Config.ARGB_8888)
                val matrix = Matrix()
                val dpiAdjustedZoomLevel: Float = currentZoomLevel * DisplayMetrics.DENSITY_MEDIUM / resources.displayMetrics.densityDpi
                matrix.setScale(dpiAdjustedZoomLevel, dpiAdjustedZoomLevel)
                currentPage!!.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                withContext(Dispatchers.Main) {
                    binding.pdfImage.setImageBitmap(bitmap)
                    updateUi()
                }
            }
        }
    }

    private fun updateUi() {
        val index = currentPage!!.index
        val pageCount = pdfRenderer!!.pageCount
        binding.btnLeft.isEnabled = 0 != index
        binding.btnRight.isEnabled = index + 1 < pageCount
        if (currentZoomLevel.toInt() == 2) {
            binding.btnZoomMax.isActivated = false
        } else {
            binding.btnZoomMin.isActivated = true
        }
    }

    fun getPageCount(): Int {
        return pdfRenderer!!.pageCount
    }

    private fun closeRenderer() {
        if (null != currentPage) {
            currentPage!!.close()
            currentPage = null
        }
        pdfRenderer!!.close()
        parcelFileDescriptor!!.close()
    }

    override fun onStop() {
        super.onStop()
        try {
            closeRenderer()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mJob.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob.cancel()
    }

    private fun loadFragment(fragmet: Fragment) {
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, fragmet, "WelcomeFragment")
                .commit()
    }
}