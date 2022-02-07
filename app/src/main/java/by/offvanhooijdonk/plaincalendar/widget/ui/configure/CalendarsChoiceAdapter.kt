package by.offvanhooijdonk.plaincalendar.widget.ui.configure

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import by.offvanhooijdonk.plaincalendar.widget.R
import by.offvanhooijdonk.plaincalendar.widget.model.CalendarModel

class CalendarsChoiceAdapter(
    private val ctx: Context,
    private val calendars: List<CalendarModel>,
    private val onItemToggle: (index: Int, isSelected: Boolean) -> Unit,
) : BaseAdapter() {
    override fun getCount(): Int {
        return calendars.size
    }

    override fun getItem(i: Int): Any {
        return calendars[i]
    }

    override fun getItemId(i: Int): Long {
        return calendars[i].id
    }

    override fun getView(i: Int, v: View?, parent: ViewGroup): View {
        val view = v ?: LayoutInflater.from(ctx).inflate(R.layout.item_calendar_choice, parent, false)
        val bean = calendars[i]
        val chbSelect = view.findViewById<CheckBox>(R.id.chbSelect)
        chbSelect.isChecked = bean.isSelected
        val txtCalendarName = view.findViewById<TextView>(R.id.txtCalendarName)
        txtCalendarName.text = bean.displayName
        val txtAccountName = view.findViewById<TextView>(R.id.txtAccountName)
        txtAccountName.text = bean.accountName
        bean.color?.let {
            view.findViewById<ImageView>(R.id.imgColor).setColorFilter(it)
        }

        view.setOnClickListener {
            chbSelect.toggle()
            onItemToggle(i, chbSelect.isChecked)
        }
        return view
    }
}
