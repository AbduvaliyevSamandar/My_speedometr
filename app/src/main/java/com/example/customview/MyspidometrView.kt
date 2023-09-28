package com.example.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MyspidometrView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet?=null,
    defStyle:Int=0
):View(context,attributeSet,defStyle){


    companion object {
        private const val min_angle = 230
        private const val max_angle = -50


        private const val small_korsatkich = 24f
        private const val katta_korsatkich = 64f

        private const val kichik_margin = 8f
        private const val katta_marjin = 0f
        private const val TEXT_MARGIN = 120f
        private const val LINE_MARGIN = 165f
    }


    private val centerX get() = width / 2
    private val centerY get() = height / 2
    private var maxSpeed = 100
    private var currentSpeed = 0


    private val smallSignPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val bigSignPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 10f
        color = Color.BLACK
    }

    private val paintText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        textSize = 48f
    }

    private val paintCircle = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.parseColor("#C0FF78")
    }


    private val paintInnerCircle = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color =  Color.parseColor("#91E137")
    }

    private val paintLineCircle = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.BLACK
    }

    private val paintLine = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 8f
        color = Color.BLACK
    }


    init {
        val typeArray = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MyspidometrView, defStyle, 0)

        try {
            maxSpeed = typeArray.getInt(R.styleable.MyspidometrView_maxSpeed, maxSpeed)
            currentSpeed = typeArray.getInt(R.styleable.MyspidometrView_currentSpeed, currentSpeed)
        } catch (e: Exception) {
        } finally {
            typeArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        renderSmallSigns(canvas)
        renderBigSigns(canvas)
        renderCircles(canvas)
        renderLine(canvas)
    }

    private fun renderSmallSigns(canvas: Canvas){
        for (i in 0..maxSpeed step 2) {
            val alfa = getAngleBySpeed(i)
            val radian = alfa.toRadian()

            if (i<=60){
                smallSignPaint.color=Color.GREEN
            }
            if (i>60 && i<=120){
                smallSignPaint.color=Color.BLACK
            }
            if (i>130){
                smallSignPaint.color=Color.RED
            }

            canvas.drawLine(
                centerX + (centerX - kichik_margin) * cos(radian),
                centerY - (centerY - kichik_margin) * sin(radian),
                centerX + (centerX - kichik_margin - small_korsatkich) * cos(radian),
                centerY - (centerY - kichik_margin - small_korsatkich) * sin(radian),
                smallSignPaint
            )
        }

    }

    private fun renderBigSigns(canvas: Canvas) {
        for (i in 0..maxSpeed step 10) {
            val radian = getAngleBySpeed(i).toRadian()

            canvas.drawLine(
                centerX + (centerX - katta_marjin) * cos(radian),
                centerY - (centerY - katta_marjin) * sin(radian),
                centerX + (centerX - katta_marjin - katta_korsatkich) * cos(radian),
                centerY - (centerY - katta_marjin - katta_korsatkich) * sin(radian),
                bigSignPaint
            )
            if (i<=60){
                paintText.color=Color.GREEN
            }
            if (i>60 && i<=120){
                paintText.color=Color.BLACK
            }
            if (i>120){
                paintText.color=Color.RED
            }

            paintText.textSize=40f
            canvas.drawCenterText(
                "$i",
                centerX + (centerX -
                        TEXT_MARGIN) * cos(radian),
                centerY - (centerY -
                        TEXT_MARGIN) * sin(radian),
                paintText
            )
        }
    }

    private fun renderCircles(canvas: Canvas) {
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), width / 4f, paintCircle)
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), width / 10f, paintInnerCircle)
    }

    private fun renderLine(canvas: Canvas) {
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), width / 20f, paintLineCircle)
        val radian = getAngleBySpeed(currentSpeed).toRadian()

        val path=Path()

        canvas.drawLine(
            centerX.toFloat()-15f,
            centerY.toFloat()-15f,
            centerX + (centerX - LINE_MARGIN) * cos(radian),
            centerY - (centerY - LINE_MARGIN) * sin(radian),
            bigSignPaint
        )
        canvas.drawLine(
            centerX.toFloat()+15f,
            centerY.toFloat()+15f,
            centerX + (centerX - LINE_MARGIN) * cos(radian),
            centerY - (centerY - LINE_MARGIN) * sin(radian),
            bigSignPaint
        )
        canvas.drawLine(
            centerX.toFloat()-15f,
            centerY.toFloat()-15f,
            centerX.toFloat()+15f,
            centerY.toFloat()+15f,
            bigSignPaint
        )

    }





    private fun Canvas.drawCenterText(text: String, x: Float, y: Float, paint: Paint) {
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        this.drawText(
            text,
            x - rect.exactCenterX(),
            y - rect.exactCenterY(),
            paint
        )
    }



    private fun getAngleBySpeed(speed: Int): Float {
        return min_angle - ((min_angle - max_angle) * speed / maxSpeed).toFloat()
    }
    private fun Float.toRadian(): Float {
        return ((this * PI) / 180f).toFloat()
    }

    fun setCurrentSpeed(speed : Int) {
        var _speed = speed
        if (_speed < 0) _speed = 0
        if (_speed > maxSpeed) _speed = maxSpeed
        this.currentSpeed = _speed
        invalidate()
    }
    fun setMaxSpeed(maxspeededit : Int) {
        maxSpeed=maxspeededit
        invalidate()
    }
    fun getMaxSpeed() =maxSpeed

}