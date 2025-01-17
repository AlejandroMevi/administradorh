package com.venturessoft.human.utils.cameravision

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.android.gms.vision.face.Face

internal class FaceGraphic(overlay: GraphicOverlay) : GraphicOverlay.Graphic(overlay) {

    private val mFacePositionPaint: Paint
    private val mIdPaint: Paint
    private val mBoxPaint: Paint

    @Volatile
    private var mFace: Face? = null
    private var mFaceId: Int = 0

    companion object {
        var derecha: Boolean = false
        var izquierda: Boolean = false
        var apruebaChecada: Boolean = false
        private const val FACE_POSITION_RADIUS = 10.0f
        private const val ID_TEXT_SIZE = 40.0f
        private const val ID_Y_OFFSET = 50.0f
        private const val ID_X_OFFSET = -50.0f
        private const val BOX_STROKE_WIDTH = 5.0f
        private val COLOR_CHOICES = intArrayOf(Color.TRANSPARENT)
        private var mCurrentColorIndex = 0
        fun finRostro() {
            derecha = false
            izquierda = false
        }
    }
    init {
        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.size
        val selectedColor = COLOR_CHOICES[mCurrentColorIndex]
        mFacePositionPaint = Paint()
        mFacePositionPaint.color = selectedColor
        mIdPaint = Paint()
        mIdPaint.color = selectedColor
        mIdPaint.textSize = ID_TEXT_SIZE
        mBoxPaint = Paint()
        mBoxPaint.color = selectedColor
        mBoxPaint.style = Paint.Style.STROKE
        mBoxPaint.strokeWidth = BOX_STROKE_WIDTH
    }
    fun setId(id: Int) {
        mFaceId = id
    }
    fun updateFace(face: Face) {
        mFace = face
        postInvalidate()
        logFaceData(mFace)
    }

    override fun draw(canvas: Canvas) {
        val face = mFace ?: return
        val x = translateX(face.position.x + face.width / 2)
        val y = translateY(face.position.y + face.height / 2)
        canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint)
        canvas.drawText("id: $mFaceId", x + ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint)
        canvas.drawText("left eye: " + String.format("%.2f", face.isLeftEyeOpenProbability), x - ID_X_OFFSET * 2, y - ID_Y_OFFSET * 2, mIdPaint)
        val xOffset = scaleX(face.width / 2.0f)
        val yOffset = scaleY(face.height / 2.0f)
        val left = x - xOffset
        val top = y - yOffset
        val right = x + xOffset
        val bottom = y + yOffset
        val EulerY = mFace!!.eulerY
        val EulerZ = mFace!!.eulerZ
        canvas.drawText("Euler Y: " + String.format("%.2f", EulerY), x - ID_X_OFFSET, y - ID_Y_OFFSET, mIdPaint)
        canvas.drawText("Euler Z: " + String.format("%.2f", EulerZ), x + ID_X_OFFSET * 2, y + ID_Y_OFFSET * 2, mIdPaint)
        canvas.drawRect(left, top, right, bottom, mBoxPaint)
    }

    private fun logFaceData(mFaces: Face?) {
        if (mFaces?.eulerY?.toDouble()!! >= 10 && mFaces.eulerZ.toDouble() <= -5) {
            derecha = true
        }
        if (mFaces.eulerY.toDouble() <= -10 && mFaces.eulerZ.toDouble() >= 5) {
            izquierda = true
        }
        if (derecha && izquierda) {
            if (GraphicFaceTracker.rostroDetectado == true) {
                apruebaChecada = true
            } else {
                derecha = false
                izquierda = false
                apruebaChecada = false
            }
        }
    }
}
