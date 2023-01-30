package pro.kevinmorris.headachewizard.ui

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import pro.kevinmorris.headachewizard.R

class CalendarDayView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    var day: Int? = null
        set(value) {
            val dayField = findViewById<TextView>(R.id.day_number)
            field = if(value != 999) value else null
            dayField.text = field?.toString() ?: ""
        }

    var headache: Int? = null
        set(value) {
            field = value
            val headacheImage = findViewById<ImageView>(R.id.day_headache_image)
            headacheImage.setImageDrawable(when(field) {
                -1 -> resources.getDrawable(R.drawable.no_headache, null)
                0 -> resources.getDrawable(R.drawable.maybe_headache, null)
                1 -> resources.getDrawable(R.drawable.headache, null)
                999, null -> null
                else -> throw IllegalArgumentException(field.toString())
            })
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.calendar_day_view, this, true)
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.CalendarDayView, 0, 0).apply {

            try {
                day = getInteger(R.styleable.CalendarDayView_day, 999)
                headache = getInteger(R.styleable.CalendarDayView_headache, 999)
            } finally {
                recycle()
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        val dayNumberTextView = findViewById<TextView>(R.id.day_number)
        dayNumberTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                Math.max(14.0f, (b - t) / 15f)
        )

        val headacheImage = findViewById<ImageView>(R.id.day_headache_image)
        val layoutParams = headacheImage.layoutParams as LayoutParams
        layoutParams.width = (0.6*(r - l)).toInt()
        layoutParams.height = (0.5*(b - t)).toInt()
        layoutParams.bottomMargin = (0.1*(b - t)).toInt()
    }
}
