package com.venturessoft.human.views.ui.fragments.Camera

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.hardware.Camera
import android.os.AsyncTask
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.face.FaceDetector
import com.google.gson.Gson
import com.venturessoft.human.R
import com.venturessoft.human.bd.BDPayments
import com.venturessoft.human.databinding.FragmentCameraBinding
import com.venturessoft.human.models.request.EnrolarRequest
import com.venturessoft.human.models.response.EnrolarResponse
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.LoadingAnimationCallback
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.Utilities.CheckType
import com.venturessoft.human.utils.WelcomeFragmentCallback
import com.venturessoft.human.utils.cameravision.Exif
import com.venturessoft.human.utils.cameravision.FaceGraphic
import com.venturessoft.human.utils.cameravision.GraphicFaceTracker
import com.venturessoft.human.utils.cameravision.GraphicFaceTrackerFactory
import com.venturessoft.human.views.ui.fragments.BaseFragment
import com.venturessoft.human.views.ui.fragments.WelcomeVinculadoFragment
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import com.venturessoft.human.views.ui.viewModels.CameraFragmentViewModel
import java.io.IOException

class CameraFragment : BaseFragment() {
    private var cameraFragmentViewModel = CameraFragmentViewModel()
    private var actualizaEmpleadoVinculado = EnrolarResponse()

    protected enum class LayoutType {
        TAKE_PHOTO, REVIEW_PHOTO
    }

    protected var startedCamera = false
    protected var hasCameraPermission = false
    protected var photoBitmap: Bitmap? = null
    private lateinit var checkType: CheckType
    private var mListener: OnFragmentInteractionListener? = null
    private var mAnimationCallBack: LoadingAnimationCallback? = null
    private var mWelcomeFragmentCallback: WelcomeFragmentCallback? = null
    private var faceDetected: Boolean = false
    lateinit var progressDialog: ProgressDialog
    private var idEmp: Long? = null
    private var newPhoto: String? = ""
    var idioma:String="ES"


    //GoogleVision
    private var mCameraSource: CameraSource? = null
    private var faceDetector: FaceDetector? = null
    private var nCamera: Int = 0
    private var frontCamera = false

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    companion object {
        lateinit var myDataBase: BDPayments

        private const val ARG_CHECKTYPE = "checkType"
        private const val ARG_LOCATION = "location"
        fun newInstance(checkType: CheckType, location: Pair<Double, Double>?): CameraFragment {
            val fragment = CameraFragment()
            val args = Bundle()
            args.putSerializable(ARG_CHECKTYPE, checkType)
            args.putSerializable(ARG_LOCATION, location)
            fragment.arguments = args
            return fragment
        }
    }
    private lateinit var binding : FragmentCameraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(activity, R.style.MyAlertDialogStyle)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        checkType = CheckType.UpdatePhoto
        checkCameraPermission()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onStop() {
        super.onStop()
        stopCamera()
        mAnimationCallBack?.hideLoadingMessage()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
        if (context is WelcomeFragmentCallback) {
            mWelcomeFragmentCallback = context
        }
        if (context is LoadingAnimationCallback) {
            mAnimationCallBack = context
        }
    }
    override fun onDetach() {
        super.onDetach()
        mListener = null
        mWelcomeFragmentCallback = null
        mAnimationCallBack = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDataBase = BDPayments(requireActivity())
        loadDataBase()
        loadDefaultData()
        loadObserver()
        mListener?.onBack("backToUpdatePhoto")
        listensButtons()
    }
    private fun loadDataBase() {
        val rows = myDataBase.getPorIdUser(User.idUsuario.toString())
        while (rows.moveToNext()) {
            idioma =   rows.getString(2).toString().toUpperCase()
        }
    }
    private fun loadObserver() {
        cameraFragmentViewModel.actualizaEmpleadoVinculadoResponseMutableData.observe(viewLifecycleOwner) {
            actualizaEmpleadoVinculado = it
            if (!actualizaEmpleadoVinculado.error) {
                successActualizarEmpleado()
            } else {
                Utilities.loadMessageError(
                    actualizaEmpleadoVinculado.resultado,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }

        cameraFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        cameraFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
    }

    private fun successActualizarEmpleado() {
        progressDialog.dismiss()
        val dialogLogout = DialogGeneral(
            getString(R.string.good),
            getString(R.string.message_success_photo_update),
            getString(R.string.ok),
            null,
            {
                mListener?.onBack("backToSuper")

                when (User.ambiente) {
                    "HU" -> {
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.Fragpricipal, WelcomeVinculadoFragment(), "WelcomeFragment")
                            .commit()
                    }
                    "ET" -> {
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.Fragpricipal, WelcomeFragment(), "WelcomeFragment")
                            .commit()
                    }
                }
            }
        )
        dialogLogout.show(childFragmentManager,"")
    }

    private fun loadDefaultData() {
        if (arguments != null) {
            idEmp = requireArguments().getLong("idEmpleado")
        }

    }


    fun Activity.displayMetrics(): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }


    protected fun takePhoto() {
        binding.adminLayoutSuper.visibility = View.VISIBLE

        if (mCameraSource != null) {
            Log.i("CameraAdmin", "TomarFoto")
            mCameraSource?.takePicture(null) {
                val orientacion = Exif.getOrientation(it)
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                val options = BitmapFactory.Options()
                options.inScaled = false

                try {
                    val matrix = Matrix()
                    //matrix.postScale(0.7f, 0.7f);
                    val oreintacionCam = Utilities.orientacionCamara(orientacion, matrix)
                    photoBitmap = Bitmap.createBitmap(
                        bitmap,
                        0,
                        0,
                        bitmap.width,
                        bitmap.height,
                        oreintacionCam,
                        true
                    )
                } catch (e: OutOfMemoryError) {
                    e.printStackTrace()
                }
                newPhoto = Utilities.bitmapToBase64(
                    Bitmap.createScaledBitmap(
                        photoBitmap!!,
                        360,
                        480,
                        true
                    )
                )
                binding.photoReviewImageViewSuper.setImageBitmap(photoBitmap)
                changeLayout(LayoutType.REVIEW_PHOTO)

            }
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            val params: WindowManager.LayoutParams = requireActivity().window.attributes
            params.screenBrightness = 2.0f
            requireActivity().window.attributes = params
            binding.adminLayoutSuper.visibility = View.GONE


        }
    }

    protected fun changeLayout(layoutType: LayoutType) {
        when (layoutType) {
            LayoutType.TAKE_PHOTO -> {
                binding.takePhotoButtonSuper.visibility = View.VISIBLE
                binding.switchCameraButtonSuper.visibility = View.VISIBLE
                if (checkType == CheckType.UpdatePhoto) {
                    binding.messageCameraFace.setText(R.string.message_camera_face_position)
                }

                binding.photoReviewImageViewSuper.visibility = View.INVISIBLE
                binding.retryConfirmLayoutSuper.visibility = View.GONE
            }
            LayoutType.REVIEW_PHOTO -> {
                binding.takePhotoButtonSuper.visibility = View.GONE
                binding.switchCameraButtonSuper.visibility = View.INVISIBLE


                binding.photoReviewImageViewSuper.visibility = View.VISIBLE
                if (checkType == CheckType.UpdatePhoto) {
                    binding.retryConfirmLayoutSuper.visibility = View.VISIBLE
                    binding.messageCameraFace.setText(R.string.message_camera_photo_taken)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (hasCameraPermission) {
            mCameraSource!!.release()
            mCameraSource!!.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        faceDetector?.release()

    }
    internal inner class progressPress : AsyncTask<Void, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog.show()
            progressDialog.setContentView(R.layout.custom_progressdialog)

            progressDialog.setCancelable(false)
        }

        override fun doInBackground(vararg params: Void?): String {
            return ""

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != "") {
                progressDialog.dismiss()
            }
        }
    }

    private fun showLoadingAnimation() {
        binding.adminLayoutSuper.visibility = View.VISIBLE
        requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

    }

    private fun hideLoadingAnimation() {
        binding.adminLayoutSuper.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = requireActivity().packageName
        val indentificadorMensaje = this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        Utilities.showDialog(requireActivity().getString(indentificadorMensaje), requireActivity(),childFragmentManager)
    }

    private fun listensButtons() {
        binding.switchCameraButtonSuper.setOnClickListener {
            if (mCameraSource!!.cameraFacing == CameraSource.CAMERA_FACING_FRONT) {
                if (mCameraSource != null) {
                    mCameraSource!!.release()
                    GraphicFaceTracker.resetBlink()
                }
                createCameraSource(CameraSource.CAMERA_FACING_BACK)
            } else {
                if (mCameraSource != null) {
                    mCameraSource!!.release()
                }
                createCameraSource(CameraSource.CAMERA_FACING_FRONT)
                GraphicFaceTracker.resetBlink()
            }
            startCameraSource()
        }
        binding.btnBackUpdatePhotoSuper.setOnClickListener {
            changeLayout(LayoutType.TAKE_PHOTO)
            photoBitmap = null
            binding.photoReviewImageViewSuper.setImageBitmap(null)
            binding.takePhotoButtonSuper.isClickable = true
            requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        binding.confirmButtonSuper.setOnClickListener {
            println("El ambiente es: "+User.ambiente)
            when (User.ambiente) {
                //Vinculado "usuarioHumanItem"
                "HU" -> {
                    // employeeName.text =  nameEmployee
                    val request = EnrolarRequest(cia = User.ciaVinculado.toString(), empleado = idEmp!!.toString(), imagen = newPhoto!!, idioma = idioma)
                    Log.i("RquestEnrolar","Enrolar JSON Entrada: "+ Gson().toJson(request))
                    cameraFragmentViewModel.getActualizaEmpleadoVinculadoService(request)
                }
                //Desvinculado "usuarioHumanItem"
                "ET" -> {
                    val request = EnrolarRequest(cia = User.scia!![0].cia.toString(), empleado = idEmp!!.toString(), imagen = newPhoto!!, idioma = idioma)
                    Log.i("RquestEnrolar","Enrolar JSON Entrada: "+ Gson().toJson(request))
                    cameraFragmentViewModel.getActualizaEmpleadoVinculadoService(request)
                    //val request = ActualizaEmpleadoRequest(idCia = User.scia!![0].cia, idEmpleado = idEmp!!, foto = newPhoto)
                    //Log.i("RquestEnrolar","ActualizaEmpleado JSON Entrada: "+ Gson().toJson(request))
                    //cameraFragmentViewModel.getActualizaEmpleadoService(request)
                }
            }
        }

        binding.takePhotoButtonSuper.setOnClickListener {
            if (checkType == CheckType.UpdatePhoto) {
                if (GraphicFaceTracker.rostroDetectado == true) {
                    binding.takePhotoButtonSuper.visibility = View.GONE
                    binding.takePhotoButtonSuper.visibility = View.INVISIBLE
                    facedetected()
                    FaceGraphic.finRostro()
                } else {
                    GraphicFaceTracker.resetBlink()
                }
            } else {
                faceDetected = false

            }
        }

    }
    //Google Vision
    private fun facedetected() {
        takePhoto()
    }

    open protected fun stopCamera() {
        if (startedCamera) {
            startedCamera = false
        }
        faceDetector?.release()
        faceDetector = null
    }

    private fun hasNoPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
    }

    //Inicializa la camara
    private fun startCameraSource() {
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                requireActivity().applicationContext)
        if (code != ConnectionResult.SUCCESS) {
            val dlg = GoogleApiAvailability.getInstance().getErrorDialog(requireActivity(), code, 9001)
            dlg?.show()
        }
        if (mCameraSource != null) {
            try {
                binding.faceOverlay.let { binding.preview.start(mCameraSource!!, it) }
            } catch (e: IOException) {
                mCameraSource!!.release()
                mCameraSource = null
            }
        }
    }

    //Crea el detector de camaras
    private fun createCameraSource(cameraFacing: Int) {
        val context = requireActivity().applicationContext
        val detector = FaceDetector.Builder(context)
                .setProminentFaceOnly(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE)
                .build()
        detector.setProcessor(
                MultiProcessor.Builder(GraphicFaceTrackerFactory(binding.faceOverlay))
                        .build())
        mCameraSource = CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(cameraFacing)
                .setRequestedFps(4.0f)
                .build()
    }

    //Permisos de Camara GoogleVision
    open protected fun checkCameraPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            hasCameraPermission = false
            requestPermissions(arrayOf(Manifest.permission.CAMERA), Constants.PERMISSION_CAMERA_REQUEST_CODE)
        } else {
            hasCameraPermission = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.PERMISSION_CAMERA_REQUEST_CODE -> {
                hasCameraPermission = grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (hasCameraPermission) {
                    val cameraInfo: Camera.CameraInfo = Camera.CameraInfo()
                    val numberCameras = Camera.getNumberOfCameras()
                    println("Numero de camaras: $numberCameras")
                    nCamera = numberCameras
                    for (num in 0 until numberCameras) {
                        println("Camara :::: : : : : $num")
                        Camera.getCameraInfo(num, cameraInfo)
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                            println("Camara frontal$num")

                            frontCamera = true
                        }
                    }

                    if (nCamera == 1 && frontCamera) {
                        createCameraSource(CameraSource.CAMERA_FACING_FRONT)
                        startCameraSource()
                        binding.switchCameraButtonSuper.isEnabled = false
                    }
                    if (nCamera == 1 && !frontCamera) {
                        createCameraSource(CameraSource.CAMERA_FACING_BACK)
                        startCameraSource()
                        binding.switchCameraButtonSuper.isEnabled = false

                    }
                    if (nCamera > 1 && frontCamera) {
                        createCameraSource(CameraSource.CAMERA_FACING_FRONT)
                        startCameraSource()
                    }
                    if (nCamera > 1 && !frontCamera) {
                        createCameraSource(CameraSource.CAMERA_FACING_BACK)
                        startCameraSource()
                    }

                }
                return
            }
        }
    }

    private fun startCamera() {
        if (nCamera == 1 && frontCamera) {
            createCameraSource(CameraSource.CAMERA_FACING_FRONT)
            startCameraSource()
            binding.switchCameraButtonSuper.isEnabled = false
        }
        if (nCamera == 1 && !frontCamera) {
            createCameraSource(CameraSource.CAMERA_FACING_BACK)
            startCameraSource()
            binding.switchCameraButtonSuper.isEnabled = false

        }
        if (nCamera > 1 && frontCamera) {
            createCameraSource(CameraSource.CAMERA_FACING_FRONT)
            startCameraSource()
        }
        if (nCamera > 1 && !frontCamera) {
            createCameraSource(CameraSource.CAMERA_FACING_BACK)
            startCameraSource()
        }
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
        if (hasCameraPermission) {
            val cameraInfo: Camera.CameraInfo = Camera.CameraInfo()
            val numberCameras = Camera.getNumberOfCameras()
            println("Numero de camaras: $numberCameras")
            nCamera = numberCameras
            for (num in 0 until numberCameras) {
                println("Camara :::: : : : : $num")
                Camera.getCameraInfo(num, cameraInfo)
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    println("Camara frontal$num")

                    frontCamera = true
                }
            }
            startCamera()
        }
    }
}
