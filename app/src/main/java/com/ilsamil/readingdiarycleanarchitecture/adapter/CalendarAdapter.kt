package com.ilsamil.readingdiarycleanarchitecture.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ilsamil.readingdiarycleanarchitecture.R
import com.ilsamil.readingdiarycleanarchitecture.databinding.ItemCalendarBinding
import com.ilsamil.readingdiarycleanarchitecture.data.db.entity.CalendarDay
import java.time.LocalDate
import java.util.*

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    private var dayList : List<CalendarDay> = ArrayList<CalendarDay>()
    var calOnClickItem : (calDay : CalendarDay) -> Unit = {}

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemCalendarBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.binding.calDay = dayList[position]

        // 클릭 이벤트
        holder.binding.itemReadIbtn.setOnClickListener {
            calOnClickItem(dayList[position])
        }
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    object DataBindingAdapter {
        private val today = LocalDate.now().dayOfMonth
        @BindingAdapter("readCheck")
        @JvmStatic
        fun setReadCheck(imgView: ImageView, calDay : CalendarDay) {
            if (!calDay.isEmpty) {
                imgView.visibility = View.VISIBLE
                if(today == calDay.day.toInt()) imgView.setBackgroundResource(R.drawable.calendar_today_background)
                if (calDay.isRead) {
                    when(calDay.day.toInt()) {
                        in 1..10 -> imgView.setImageResource(R.drawable.img_all_reading_icon_22)
                        in 11..21 -> imgView.setImageResource(R.drawable.img_all_reading_icon_11)
                        else -> imgView.setImageResource(R.drawable.img_all_reading_icon_33)
                    }
                } else {
                    imgView.setImageResource(R.drawable.img_claendar_not_reading_icon2)
                }
            } else {
                imgView.visibility = View.INVISIBLE
            }
        }
    }

    fun updateItem(item : List<CalendarDay>) {
        dayList = item
        notifyDataSetChanged()
    }

}