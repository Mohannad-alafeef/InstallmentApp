package dev.kkarot.installmentapp.views

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

private const val TAG = "---NameHolder"

class NameHolder:View {
    private companion object{
        val sColorList:List<String> = listOf(
            "#FAF8F1",
            "#FAEAB1",
            "#E5BA73",
            "#C58940",
            "#ADA2FF",
            "#C0DEFF",
            "#FFE5F1",
            "#FFF8E1",
            "#FFEA20",
            "#8DCBE6",
            "#9DF1DF",
            "#E3F6FF"
        )
    }

    private val circlePaint:Paint = Paint()
    private val textPaint:Paint = Paint()

    private val cx:Float = 100f
    private val cy:Float = 100f
    private val radius:Float = 100f
    private var char:String = "B"

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        circlePaint.color = Color.parseColor(sColorList[char[0].code.mod(sColorList.size-1)])
        canvas!!.drawCircle(width/2f,height/2f,width/2f,circlePaint)

        drawCenter(canvas)


    }
    fun setName(name:String){
        char = name.first().toString()
        invalidate()
    }
    private fun drawCenter(canvas: Canvas){
        val bounds:Rect = Rect()
        canvas.getClipBounds(bounds)
        val cHeight: Int = bounds.height()
        val cWidth: Int = bounds.width()
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.color = Color.BLACK
        textPaint.textSize = width/2f
        textPaint.flags = Paint.ANTI_ALIAS_FLAG
        textPaint.getTextBounds(char, 0, 1, bounds)
        val x: Float = cWidth / 2f - bounds.width() / 2f - bounds.left
        val y: Float = cHeight / 2f + bounds.height() / 2f - bounds.bottom
        canvas.drawText(char, x, y, textPaint)
    }



}