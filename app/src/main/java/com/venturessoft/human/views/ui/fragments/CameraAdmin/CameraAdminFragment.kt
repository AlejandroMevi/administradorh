package com.venturessoft.human.views.ui.fragments.CameraAdmin

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
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.face.FaceDetector
import com.venturessoft.human.R
import com.venturessoft.human.utils.cameravision.FaceGraphic
import com.venturessoft.human.utils.cameravision.GraphicFaceTracker
import com.venturessoft.human.utils.cameravision.GraphicFaceTrackerFactory
import com.venturessoft.human.databinding.FragmentCameraAdminBinding
import com.venturessoft.human.models.User
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.utils.LoadingAnimationCallback
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.Utilities.CheckType
import com.venturessoft.human.utils.WelcomeFragmentCallback
import com.venturessoft.human.utils.cameravision.Exif
import com.venturessoft.human.views.ui.fragments.BaseFragment
import com.venturessoft.human.views.ui.fragments.Employe.EditEmployeeFragment
import com.venturessoft.human.views.ui.fragments.Employe.EmployeeListFragment
import com.venturessoft.human.views.ui.fragments.Employe.RegisterEmployeeFragment
import java.io.IOException

class CameraAdminFragment : BaseFragment() {
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

    //GoogleVision
    private var mCameraSource: CameraSource? = null
    private var faceDetector: FaceDetector? = null
    private var nCamera: Int = 0
    private var frontCamera = false
    var idioma:String="ES"

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    companion object {
        var redirect = ""
        private const val ARG_USER = "userInfo"
        fun newInstance(user: User): CameraAdminFragment {
            val fragment = CameraAdminFragment()
            val args = Bundle()
            args.putSerializable(ARG_USER, user)
            fragment.arguments = args
            return fragment
        }
    }
    private val TAG: String = CameraAdminFragment::class.java.simpleName
    private var _binding : FragmentCameraAdminBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCameraAdminBinding.inflate(inflater, container, false)
        Log.d(TAG, "ON CREATE VIEW")
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(activity, R.style.MyAlertDialogStyle)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        checkCameraPermission()
        loadData()
    }
    private fun loadData() {
        if (arguments != null) {
            redirect = requireArguments().getString("redirect")!!
        }
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

        mListener?.onBack("backCamaraFragment")
        checkType = CheckType.UpdatePhoto
        listensButtons()


        binding.takePhotoButton.setOnClickListener {
            if (checkType == CheckType.UpdatePhoto) {
                if (GraphicFaceTracker.rostroDetectado == true ) {
                    binding.takePhotoButton.visibility = View.GONE
                    binding.takePhotoButton.visibility = View.INVISIBLE
                    facedetected()
                    FaceGraphic.finRostro()
                } else {
                    GraphicFaceTracker.resetBlink()
                }
            } else {
                faceDetected = false

            }
        }



        binding.confirmButton.setOnClickListener {
            val foto360x480: String = Utilities.bitmapToBase64(Bitmap.createScaledBitmap(photoBitmap!!, 360, 480, true))
            mListener?.onBack("backToSuper")
            progressDialog.dismiss()
            EmployeeListFragment.zonas?.clear()
            EmployeeListFragment.estaciones?.clear()
            when (redirect) {
                "RegisterEmployeeFragment" -> {
                    Log.d("CameraAdminFragment", "Foto de retorno: $foto360x480")
                    val registerEmployeeFragment = RegisterEmployeeFragment()
                    registerEmployeeFragment
                    val bundle = Bundle()
                    bundle.putString("foto", foto360x480)
                    registerEmployeeFragment.arguments = bundle
                    requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.Fragpricipal, registerEmployeeFragment, "WelcomeFragment")
                            .commit()
                }
                "EditEmployeeFragment" -> {
                    Log.d("EditEmployeeFragment", "Foto de retorno: $foto360x480")
                    EditEmployeeFragment.foto = foto360x480
                    requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.Fragpricipal, EditEmployeeFragment(), "WelcomeFragment")
                            .commit()
                }
            }
        }
        binding.btnBackRegisterEmployee.setOnClickListener {
            //   mListener?.onBack("backToUpdatePhoto")
            changeLayout(LayoutType.REVIEW_PHOTO)
            photoBitmap = null
            binding.photoReviewImageView.setImageBitmap(null)
            binding.confirmButton.visibility = View.GONE
            binding.takePhotoButton.visibility = View.VISIBLE
            when (redirect) {
                "RegisterEmployeeFragment" -> {
                    Log.d("BackRegistAdminFragment", "retorno AltaEmpleado: ")
                    val registerEmployeeFragment = RegisterEmployeeFragment()
                    requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.Fragpricipal, registerEmployeeFragment, "WelcomeFragment")
                            .commit()
                }
                "EditEmployeeFragment" -> {
                    Log.d("BackEditEmployFragment", "retorno AltaEmpleado ")
                    requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.Fragpricipal, EditEmployeeFragment(), "WelcomeFragment")
                            .commit()
                }
            }
        }
    }

    private fun listensButtons() {
        binding.switchCameraButton.setOnClickListener {
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
    }

    fun Activity.displayMetrics(): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    protected fun takePhoto() {
        binding.cameraAdminAnimotion.visibility = View.VISIBLE
        Log.i("CameraAdmin", "TomarFoto")

        if (mCameraSource != null) {
            mCameraSource?.takePicture(null, CameraSource.PictureCallback {
                val orientacion = Exif.getOrientation(it)
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                val options = BitmapFactory.Options();
                options.inScaled = false

                try {
                    val matrix = Matrix()
                 //   matrix.postScale(-5.0f, -5.0f);
                    val oreintacionCam = Utilities.orientacionCamara(orientacion, matrix)
                    photoBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, oreintacionCam, true)
                } catch (e: OutOfMemoryError) {
                    e.printStackTrace()
                }
                binding.photoReviewImageView.setImageBitmap(photoBitmap)
                changeLayout(LayoutType.REVIEW_PHOTO)
            })
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            val params: WindowManager.LayoutParams = requireActivity().window.attributes
            params.screenBrightness = 2.0f
            requireActivity().window.attributes = params
            binding.cameraAdminAnimotion.visibility = View.GONE

        }


    }

    protected fun changeLayout(layoutType: LayoutType) {
        when (layoutType) {
            LayoutType.TAKE_PHOTO -> {
                binding.takePhotoButton.visibility = View.VISIBLE
                binding.switchCameraButton.visibility = View.VISIBLE
                if (checkType == CheckType.UpdatePhoto) {
                    binding.messageCameraFace.setText(R.string.message_camera_face_position)
                }

                binding.photoReviewImageView.visibility = View.INVISIBLE
                binding.retryConfirmLayout.visibility = View.GONE
            }
            LayoutType.REVIEW_PHOTO -> {
                binding.takePhotoButton.visibility = View.GONE
                binding.switchCameraButton.visibility = View.INVISIBLE


                binding.photoReviewImageView.visibility = View.VISIBLE
                if (checkType == CheckType.UpdatePhoto) {
                    binding.retryConfirmLayout.visibility = View.VISIBLE
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
                binding.faceOverlay1.let { binding.preview1.start(mCameraSource!!, it) }
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
                MultiProcessor.Builder(GraphicFaceTrackerFactory(binding.faceOverlay1))
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
                        binding.switchCameraButton.isEnabled = false
                    }
                    if (nCamera == 1 && !frontCamera) {
                        createCameraSource(CameraSource.CAMERA_FACING_BACK)
                        startCameraSource()
                        binding.switchCameraButton.isEnabled = false

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
            binding.switchCameraButton.isEnabled = false
        }
        if (nCamera == 1 && !frontCamera) {
            createCameraSource(CameraSource.CAMERA_FACING_BACK)
            startCameraSource()
            binding.switchCameraButton.isEnabled = false

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
        println("Permisos de camara $hasCameraPermission")

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
