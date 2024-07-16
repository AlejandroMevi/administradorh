package com.venturessoft.human.views.ui.customView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager

class VerticalViewPager : ViewPager {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return false
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return super.canScrollVertically(direction)
    }

    private fun init() {
        setPageTransformer(true, VerticalPageTransformer())
        overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > height) height = h
        }

        val heightMeasure = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)

        super.onMeasure(widthMeasureSpec, heightMeasure)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val toIntercept: Boolean = super.onInterceptTouchEvent(ev?.let { flipXY(it) })
        ev?.let { flipXY(it) }
        println("onInterceptTouchEvent")
        println(ev?.actionMasked)

        println(toIntercept)


        return toIntercept
    }

    private fun flipXY(ev: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()
        val x = (ev.y / height) * width
        val y = (ev.x / width) * height
        println("Control de variables")
        println(x)
        println(y)
        //println(calculateDistanceSwipe(ev))
        ev.setLocation(x, y)
        return ev
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val toHandle: Boolean = super.onTouchEvent(ev?.let { flipXY(it) })
        ev?.let { flipXY(it) }

        return toHandle
    }

    private class VerticalPageTransformer : PageTransformer {
        override fun transformPage(view: View, position: Float) {
            val pageWidth = view.width
            val pageHeight = view.height / 2
            if (position < -1) {
                view.alpha = 0f
            } else if (position <= 1) {
                view.alpha = 1f
                view.translationX = pageWidth * -position
                val yPosition = position * pageHeight
                view.translationY = yPosition
            } else {
                view.alpha = 0f
            }
        }
    }

}