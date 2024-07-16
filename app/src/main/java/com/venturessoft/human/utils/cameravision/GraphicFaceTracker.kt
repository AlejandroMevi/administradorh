package com.venturessoft.human.utils.cameravision

import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.face.Face

class GraphicFaceTracker internal constructor(private val mOverlay: GraphicOverlay) :
    Tracker<Face>() {
    private val mFaceGraphic: FaceGraphic
    private var leftEyeOpenProbability = -1.0
    private var rightEyeOpenProbability = -1.0
    private var currentEyess: Boolean = false

    companion object {
        var rostroDetectado: Boolean? = false
        var detectarParpadeo: Boolean? = false
        var nParpadeo: Int = 0
        fun resetBlink() {
            nParpadeo = -1
        }
    }

    init {
        mFaceGraphic = FaceGraphic(mOverlay)
    }

    override fun onNewItem(faceId: Int, item: Face) {
        mFaceGraphic.setId(faceId)
    }

    override fun onUpdate(p0: Detector.Detections<Face>, face: Face) {
        mOverlay.add(mFaceGraphic)
        face.let { mFaceGraphic.updateFace(it) }
        rostroDetectado = true
        val currentLeftEyeOpenProbability: Float = face.isLeftEyeOpenProbability
        val currentRightEyeOpenProbability: Float = face.isRightEyeOpenProbability

        if (currentLeftEyeOpenProbability.toDouble() == -1.0 || currentRightEyeOpenProbability.toDouble() == -1.0) {
            return
        }
        if (currentLeftEyeOpenProbability < 0.1 && rightEyeOpenProbability < 0.1) {
            currentEyess = true
        }
        if (leftEyeOpenProbability > 0.8 && rightEyeOpenProbability > 0.8 && currentEyess) {
            detectarParpadeo = true
            nParpadeo += 1
            leftEyeOpenProbability = currentLeftEyeOpenProbability.toDouble()
            rightEyeOpenProbability = currentRightEyeOpenProbability.toDouble()
            currentEyess = false
        } else {
            leftEyeOpenProbability = currentLeftEyeOpenProbability.toDouble()
            rightEyeOpenProbability = currentRightEyeOpenProbability.toDouble()
        }
    }

    override fun onMissing(p0: Detector.Detections<Face>) {
        mOverlay.remove(mFaceGraphic)
        nParpadeo = 0
    }

    override fun onDone() {
        nParpadeo = 0
        rostroDetectado = false
        detectarParpadeo = false
        FaceGraphic.finRostro()
        mOverlay.remove(mFaceGraphic)
    }
}