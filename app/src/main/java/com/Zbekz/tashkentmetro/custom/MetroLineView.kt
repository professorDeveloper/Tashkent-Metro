package com.Zbekz.tashkentmetro.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
class MetroLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val linePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 4f.dpToPx(context)
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val circlePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 16f.dpToPx(context)
        isAntiAlias = true
    }


    var metroStations: List<String> = listOf()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val startY = 50f
        val endY = height - 50f
        val centerX = width / 2f


        canvas.drawLine(centerX, startY, centerX, endY, linePaint)


        val circleRadius = 10f.dpToPx(context)
        canvas.drawCircle(centerX, startY, circleRadius, circlePaint)
        canvas.drawCircle(centerX, endY, circleRadius, circlePaint)


        val step = (endY - startY) / (metroStations.size - 1)

        metroStations.forEachIndexed { index, station ->
            val y = startY + index * step
            canvas.drawCircle(centerX, y, circleRadius, circlePaint)


            canvas.drawText(station, centerX + 30f.dpToPx(context), y + 5f.dpToPx(context), textPaint)
        }
    }


    private fun Float.dpToPx(context: Context): Float {
        return this * context.resources.displayMetrics.density
    }
}
