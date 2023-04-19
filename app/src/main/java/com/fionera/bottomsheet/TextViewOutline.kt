package com.fionera.bottomsheet

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * TextView
 *
 * @author fionera
 * @date 2023/3/21 in BehaviorChanges.
 */
class TextViewOutline(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    val textPaint = TextPaint()

    init {
        textPaint.color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawText("123456789", 150F, 50F, textPaint)
    }
}