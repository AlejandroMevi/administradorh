package com.venturessoft.human.views.ui.fragments.UpdatePhoto

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentUpdatePhotoBinding
import com.venturessoft.human.models.User
import com.venturessoft.human.views.ui.fragments.Camera.CameraFragment
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import com.venturessoft.human.views.ui.fragments.WelcomeVinculadoFragment

class UpdatePhotoFragment : androidx.fragment.app.Fragment() {
    private var mListener: OnFragmentInteractionListener? = null
    lateinit var progressDialog: ProgressDialog
    private var idEmpleado: Long? = null
    private var nameEmployee: String? = ""
    private var apellidoPat: String? = ""
    private var apellidoMat: String? = ""
    private var foto: String? = null

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    companion object {
        private const val ARG_USER = "userInfo"
        fun newInstance(user: User): UpdatePhotoFragment {
            val fragment = UpdatePhotoFragment()
            val args = Bundle()
            args.putSerializable(ARG_USER, user)
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
    private val TAG: String = UpdatePhotoFragment::class.java.simpleName
    private var _binding : FragmentUpdatePhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        progressDialog = ProgressDialog(activity, R.style.MyAlertDialogStyle)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        //Utilities.setupUI(mainLayout, requireActivity())
      //  FakeInterceptor.IS_ENABLE = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentUpdatePhotoBinding.inflate(inflater, container, false)
        Log.d(TAG, "ON CREATE VIEW")
        return binding.root
    }

    fun cargarFoto(): String {
        val carga = ""
        return carga
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressPress().execute()
        loadDefaultData()

        binding.updatePhotoButton.setOnClickListener {
            val cameraFragment = CameraFragment()
            val data = Bundle()
          //  println("El idEmpleado que se envia es: " + idEmpleado)
            data.putLong("idEmpleado", idEmpleado!!)
            cameraFragment.arguments = data

            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.Fragpricipal, cameraFragment, "CameraFragment")
                    .addToBackStack(null)
                    .commit()
        }


        binding.btnBackMenuUpdate.setOnClickListener {
            when (com.venturessoft.human.utils.User.ambiente) {
                //Vinculado "usuarioHumanItem"
                "HU" -> { requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.Fragpricipal, WelcomeVinculadoFragment(), "WelcomeFragment")
                        .commit()
                }
                //Desvinculado "usuarioHumanItem"
                "ET" -> {
                    requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.Fragpricipal, WelcomeFragment(), "WelcomeFragment")
                            .commit()
                }
            }
        }

        mListener!!.onBack("backMenuUpdatePhoto")
    }

    override fun onStart() {
        super.onStart()
        val md = ArrayList<Int>()
        activity?.displayMetrics()?.run {
            val heigh = heightPixels
            val width = widthPixels
            md.add(heigh)
            md.add(width)
        }
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
        binding.updatePhotoButton.isClickable = true
    }

    private fun loadDefaultData() {
        if (arguments != null) {
            idEmpleado = requireArguments().getLong("idEmpleado")
            nameEmployee = requireArguments().getString("nameEmployee")
            apellidoPat = requireArguments().getString("apellidoPat")
            if (requireArguments().getString("apellidoMat") != null) {
                apellidoMat = requireArguments().getString("apellidoMat")
            }
            foto = requireArguments().getString("foto")
        }
        loadTextNameEmployee()

        if (!foto.isNullOrEmpty()) {
            val imagenBase64 = Base64.decode(foto, Base64.DEFAULT)
            val imagenconverBitmap = BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
            Log.i("UpdatePhoto", "La foto base64 es: $foto")
            binding.employeePhotoView.setImageBitmap(imagenconverBitmap)
            binding.updatePhotoButton.text = getString(R.string.message_change_photo)
            progressDialog.dismiss()
            binding.photoLoadingAnimation.root.visibility = View.GONE
            binding.updatePhotoButton.isClickable = true

        } else {
            progressDialog.dismiss()
            binding.updatePhotoButton.text = getString(R.string.message_change_update)
            binding.updatePhotoButton.isClickable = true
        }
    }

    private fun loadTextNameEmployee() {
        when (com.venturessoft.human.utils.User.ambiente) {
            //Vinculado "usuarioHumanItem"
            "HU" -> {
                binding.employeeName.text =  nameEmployee
            }
            //Desvinculado "usuarioHumanItem"
            "ET" -> {
                binding.employeeName.text = nameEmployee
            }
        }
    }
}
