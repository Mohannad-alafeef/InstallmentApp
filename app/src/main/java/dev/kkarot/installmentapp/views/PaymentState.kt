package dev.kkarot.installmentapp.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import dev.kkarot.installmentapp.R
import dev.kkarot.installmentapp.database.models.PaymentInfo


class PaymentState : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )



    companion object {
        private val colors = arrayOf(
            Color.GREEN,
            Color.RED,
            Color.YELLOW
        )
    }

    fun setPayment(info: PaymentInfo) {
        if (info.isPaid) {
            setBackgroundColor(colors[0])
        }
        if (!info.isPaid && info.isDue) {
            setBackgroundColor(colors[2])

        }
        if (!info.isPaid && !info.isDue) {
            setBackgroundColor(colors[1])

        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height1 = MeasureSpec.makeMeasureSpec(200, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, height1)
    }
}