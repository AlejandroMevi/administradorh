package com.venturessoft.human.views.ui.fragments.UpdatePhotoAdmin

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentUpdatePhotoAdminBinding
import com.venturessoft.human.models.User
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.activities.LoginActivity
import com.venturessoft.human.views.ui.fragments.CameraAdmin.CameraAdminFragment
import com.venturessoft.human.views.ui.fragments.Employe.EditEmployeeFragment
import com.venturessoft.human.views.ui.fragments.Employe.RegisterEmployeeFragment
import java.io.ByteArrayOutputStream
import java.io.File

class UpdatePhotoAdminFragment : androidx.fragment.app.Fragment() {
    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var user: User
    private lateinit var foto: String
    lateinit var progressDialog: ProgressDialog
    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    companion object {
        var redirect: String? = ""
        var fotoBase64: String? = null
        var fotoEmpLocal: String = ""
        private const val ARG_USER = "userInfo"
        private const val ARG_FOTO = "foto"
        fun newInstance(user: User, foto: String): UpdatePhotoAdminFragment {
            val fragment = UpdatePhotoAdminFragment()
            val args = Bundle()
            args.putSerializable(ARG_USER, user)
            args.putSerializable(ARG_FOTO, foto)
            fragment.arguments = args

            return fragment
        }
    }
    internal inner class progressPress : AsyncTask<Void, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog.show()
            progressDialog.setContentView(R.layout.custom_progressdialog)
            progressDialog.setCancelable(false)
        }
        override fun doInBackground(vararg params: Void?): String {
            val retornaCarga: String = cargarFoto()
            return retornaCarga
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != "") {
                progressDialog.dismiss()
            }
        }
    }
    private lateinit var binding : FragmentUpdatePhotoAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(activity, R.style.MyAlertDialogStyle)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        if (arguments != null) {
            user = requireArguments().getSerializable(ARG_USER) as User
            foto = requireArguments().getSerializable(ARG_FOTO) as String
            fotoBase64 = requireArguments().getSerializable(ARG_FOTO) as String
        } else {
            val dialogLogout = DialogGeneral(getString(R.string.logout),getString(R.string.message_confirm_logout),getString(R.string.logout),getString(R.string.cancel), {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                requireActivity().finishAffinity()
            })
            dialogLogout.show(childFragmentManager,"")
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUpdatePhotoAdminBinding.inflate(inflater, container, false)
        return binding.root
    }
    fun cargarFoto(): String {
        val carga = ""
        return carga
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.employeeName.text = user.name
        progressPress().execute()
        var urlFoto = ""
        user.photoUrl?.let {
            val miphotho = "empleado" + ".jpeg"
            val arreglodeBytes = ByteArrayOutputStream()
            val imagenBase64 = Base64.decode(foto, Base64.DEFAULT)
            val imagenconverBitmap = BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
            imagenconverBitmap!!.compress(Bitmap.CompressFormat.JPEG, 50, arreglodeBytes)
            val arregloBitsImag = arreglodeBytes.toByteArray()
            "$imagenconverBitmap.jpeg"
            val nombreFoto = "empleado.jpeg"
            val urlBase = "${requireContext().filesDir}"
            File(urlBase, miphotho).writeBytes(arregloBitsImag)
            urlFoto = "$urlBase/$nombreFoto"
        }
        fotoEmpLocal = "file:/data/data/" + requireActivity().applicationContext.packageName.toString() + "/files/empleado.jpeg"
        if (urlFoto.isNotEmpty()) {
            Picasso
                    .with(context)
                    .load(fotoEmpLocal)
                    .placeholder(R.drawable.user_icon)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                    .centerCrop()
                    .resize(300, 300)
                    .into(binding.employeePhotoView, object : Callback {
                        override fun onSuccess() {
                            binding.updatePhotoButton.text = getString(R.string.message_change_photo)
                            binding.updatePhotoButton.isClickable = true
                            binding.okButton.isClickable = true
                            progressDialog.dismiss()
                        }
                        override fun onError() {
                            progressDialog.dismiss()
                        }
                    })
        } else {
            progressDialog.dismiss()
            binding.updatePhotoButton.text = getString(R.string.message_change_update)
            binding.updatePhotoButton.isClickable = true
            binding.okButton.isClickable = true
            Handler().postDelayed({
                error()
            }, 1000)
        }
        binding.updatePhotoButton.setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.Fragpricipal, CameraAdminFragment.newInstance(user = user), "CameraFragment")
                    .addToBackStack(null)
                    .commit()
        }
        binding.okButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        mListener?.onBack("backCamaraFragmentOfUpdate")
        binding.btnBackRegisterEmploye.setOnClickListener {
            if (redirect == "edit") {
                EditEmployeeFragment.foto = foto
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.Fragpricipal, EditEmployeeFragment(), "WelcomeFragment")
                        .commit()
            } else {
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.Fragpricipal, RegisterEmployeeFragment.newInstance( null, null, null, null, null), "WelcomeFragment")
                        .commit()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        progressDialog.dismiss()
        val md = ArrayList<Int>()
        activity?.displayMetrics()?.run {
            val heigh = heightPixels
            val width = widthPixels
            md.add(heigh)
            md.add(width)
        }
        binding.updatePhotoButton.isClickable = false
        binding.okButton.isClickable = false
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
    }
    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
    private fun Activity.displayMetrics(): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }
    fun error() {
        progressDialog.dismiss()
        binding.updatePhotoButton.isClickable = true
        binding.okButton.isClickable = true
    }
}