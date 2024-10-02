package com.azamovhudstc.tashkentmetro.custom

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

    // Paint obyekti chiziq va aylanalarni chizish uchun
    private val linePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 4f.dpToPx(context) // 4 dp ni px ga aylantirish
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
        textSize = 16f.dpToPx(context) // Matn o'lchamini dp da belgilash
        isAntiAlias = true
    }

    // Metro bekatlar ro'yxati
    var metroStations: List<String> = listOf()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val startY = 50f
        val endY = height - 50f
        val centerX = width / 2f

        // Vertikal chiziq chizish
        canvas.drawLine(centerX, startY, centerX, endY, linePaint)

        // Ikkala boshida aylanalar chizish
        val circleRadius = 10f.dpToPx(context)
        canvas.drawCircle(centerX, startY, circleRadius, circlePaint)
        canvas.drawCircle(centerX, endY, circleRadius, circlePaint)

        // Bekatlar soniga mos holda aylana va bekat nomlarini chizish
        val step = (endY - startY) / (metroStations.size - 1) // Har bir bekat o'rtasidagi masofa

        metroStations.forEachIndexed { index, station ->
            val y = startY + index * step
            canvas.drawCircle(centerX, y, circleRadius, circlePaint) // Aylana chizish

            // Bekat nomini nuqtalardan 30 dp o'ng tomonga joylashtirish
            canvas.drawText(station, centerX + 30f.dpToPx(context), y + 5f.dpToPx(context), textPaint)
        }
    }

    // dp ni px ga aylantirish
    private fun Float.dpToPx(context: Context): Float {
        return this * context.resources.displayMetrics.density
    }
}
