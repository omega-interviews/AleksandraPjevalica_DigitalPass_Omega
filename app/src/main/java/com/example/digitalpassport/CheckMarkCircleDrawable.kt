import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class CheckMarkCircleDrawable(context: Context) : Drawable() {

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val tickPath = Path()

    var circleColor: Int = ContextCompat.getColor(context, android.R.color.holo_green_light)
        set(value) {
            field = value
            invalidateSelf()
        }

    var tickColor: Int = ContextCompat.getColor(context, android.R.color.white)
        set(value) {
            field = value
            invalidateSelf()
        }

    init {
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeWidth = 5f
        circlePaint.color = circleColor

        tickPaint.style = Paint.Style.STROKE
        tickPaint.strokeWidth = 5f
        tickPaint.color = tickColor
    }

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        val radius = bounds.width() / 2f

        // Draw the empty circle
        canvas.drawCircle(
            bounds.centerX().toFloat(),
            bounds.centerY().toFloat(),
            radius - circlePaint.strokeWidth / 2,
            circlePaint
        )

        // Draw the tick mark with the correct color
        tickPaint.color = tickColor
        drawTick(bounds.centerX().toFloat(), bounds.centerY().toFloat(), radius, canvas)
    }


    private fun drawTick(centerX: Float, centerY: Float, radius: Float, canvas: Canvas) {
        val startX = centerX - radius / 2
        val startY = centerY + radius / 4
        val midX = centerX
        val midY = centerY + radius / 2
        val endX = centerX + radius / 2
        val endY = centerY - radius / 4

        tickPath.reset()
        tickPath.moveTo(startX, startY)
        tickPath.lineTo(midX, midY)
        tickPath.lineTo(endX, endY)

        canvas.drawPath(tickPath, tickPaint)
    }

    override fun setAlpha(alpha: Int) {
        // Not implemented
    }

    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
        // Not implemented
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN // Not needed for this example
    }
}
