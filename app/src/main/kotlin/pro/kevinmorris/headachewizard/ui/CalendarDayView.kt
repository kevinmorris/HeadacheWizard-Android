package pro.kevinmorris.headachewizard.ui

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import pro.kevinmorris.headachewizard.R
import org.joda.time.LocalDate

class CalendarDayView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    var date: LocalDate? = null
        set(date) {
            field = date
            val dayField = findViewById(R.id.day_number) as TextView
            dayField.text = if(this.date != null) this.date!!.dayOfMonth.toString() else ""
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.calendar_day_view, this, true)
    }

    fun setHeadacheData(headacheData: Double?) {

        val headacheImage = findViewById(R.id.day_headache_image) as ImageView
        headacheImage.setImageDrawable(when(headacheData) {
            -1.0 -> resources.getDrawable(R.drawable.no_headache, null)
            0.0 -> resources.getDrawable(R.drawable.maybe_headache, null)
            1.0 -> resources.getDrawable(R.drawable.headache, null)
            null -> null
            else -> throw IllegalArgumentException(headacheData.toString())
        })
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        val dayNumberTextView = findViewById(R.id.day_number) as TextView
        dayNumberTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                Math.max(14.0f, (b - t) / 15f)
        )

        val headacheImage = findViewById(R.id.day_headache_image) as ImageView
        val layoutParams = headacheImage.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = (0.6*(r - l)).toInt()
        layoutParams.height = (0.5*(b - t)).toInt()
        layoutParams.bottomMargin = (0.1*(b - t)).toInt()
    }
}
