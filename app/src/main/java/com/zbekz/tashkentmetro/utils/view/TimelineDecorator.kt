package com.zbekz.tashkentmetro.utils.view

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

class TimelineDecorator(
    private val indicatorSize: Float,
    private val lineWidth: Float,
    private val padding: Float,
    private val position: Position,
    private val indicatorColor: Int,
    private val lineColor: Int
) : RecyclerView.ItemDecoration() {

    enum class Position {
        LEFT, RIGHT
    }

    private val paint = Paint()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val left = if (position == Position.LEFT) {
            parent.paddingLeft + padding
        } else {
            parent.width - parent.paddingRight - padding
        }

        paint.color = lineColor
        paint.strokeWidth = lineWidth

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + lineWidth

            // Draw line
            c.drawRect(left, top.toFloat(), left + lineWidth, bottom, paint)

            // Draw indicator
            paint.color = indicatorColor
            c.drawCircle(left + lineWidth / 2, top - indicatorSize / 2, indicatorSize / 2, paint)
        }
    }
}
