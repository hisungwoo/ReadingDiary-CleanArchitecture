package com.ilsamil.readingdiarycleanarchitecture.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ilsamil.readingdiarycleanarchitecture.BR
import com.ilsamil.readingdiarycleanarchitecture.viewmodel.CalendarViewModel
import com.ilsamil.readingdiarycleanarchitecture.R
import com.ilsamil.readingdiarycleanarchitecture.adapter.CalendarAdapter
import com.ilsamil.readingdiarycleanarchitecture.databinding.CalendarListBinding
import com.ilsamil.readingdiarycleanarchitecture.data.db.entity.CalendarDay
import com.ilsamil.readingdiarycleanarchitecture.data.db.entity.ReadingDay
import com.ilsamil.readingdiarycleanarchitecture.utils.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.floor

class CalendarFragment : Fragment() {
    private val mainViewModel by activityViewModels<CalendarViewModel>()
    private lateinit var binding : CalendarListBinding
    private lateinit var selectedDate : LocalDate

    companion object {
        private const val TAG = "CalendarFragment_1sam1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)

        //?????? ??????
        selectedDate = LocalDate.now()
        val calendarAdapter = CalendarAdapter().apply { calOnClickItem = this@CalendarFragment::moveWriteReadingItem }

        binding.apply {
            calCurrentDateTv.text = monthYearFromDate(selectedDate)
            calRecyclerview.layoutManager = GridLayoutManager(context,
                7)
            calRecyclerview.adapter = calendarAdapter
        }

        // ?????? ?????? ??????
        mainViewModel.apply {
            setCalendar(selectedDate)
            calReadList.observe(this@CalendarFragment, androidx.lifecycle.Observer {
                calendarAdapter.updateItem(it)
            })
        }

        binding.apply {
            setVariable(BR.model, ViewModelProvider(this@CalendarFragment).get(CalendarViewModel::class.java))
            lifecycleOwner = this@CalendarFragment
            model = mainViewModel
            executePendingBindings()

            calPrevBtn.setOnClickListener {
                selectedDate = selectedDate.minusMonths(1)
                mainViewModel.setCalendar(selectedDate)
                binding.calCurrentDateTv.text = monthYearFromDate(selectedDate)
            }

            calNextBtn.setOnClickListener {
                selectedDate = selectedDate.plusMonths(1)
                mainViewModel.setCalendar(selectedDate)
                binding.calCurrentDateTv.text = monthYearFromDate(selectedDate)
            }
        }
        return binding.root
    }

    // ?????? ??????
    private fun moveWriteReadingItem(calDay : CalendarDay) {
        if (!calDay.isEmpty && calDay.isRead) {
            // ?????? ????????? ?????? ??????
            GlobalScope.launch(Dispatchers.Main) {
                val calInfo = mainViewModel.getCalInfo(calDay)
                val imgUrl = mainViewModel.getImgUrl(calInfo)
                setDialog(context!!, calInfo, imgUrl, calDay)
            }

        } else {
            // ?????? ????????? ?????? ?????? : ????????? ?????? ???????????? ??????
            val action = CalendarFragmentDirections.actionCalendarFragmentToWriteReadingFragment(calDay)
            findNavController().navigate(action)
        }
    }


    private fun setDialog(context : Context, readingDay: ReadingDay, imgUrl : String, item : CalendarDay) {
        AlertDialog.Builder(context)
            .setView(R.layout.dialog_sel_calendar)
            .show()
            .also { alertDialog ->
                if (alertDialog == null) return@also

                val dateTitleTv = alertDialog.findViewById<TextView>(R.id.dialog_cal_date_tv)
                val bookNameTv = alertDialog.findViewById<TextView>(R.id.dialog_cal_book_tv)
                val progressBar = alertDialog.findViewById<ProgressBar>(R.id.dialog_cal_progress_bar)
                val progressTv = alertDialog.findViewById<TextView>(R.id.dialog_cal_progress_tv)
                val pageTv = alertDialog.findViewById<TextView>(R.id.dialog_cal_page_tv)
                val readingTv = alertDialog.findViewById<TextView>(R.id.dialog_cal_reading_tv)

                val editBtn = alertDialog.findViewById<Button>(R.id.dialog_cal_edit_btn)
                val removeBtn = alertDialog.findViewById<Button>(R.id.dialog_cal_remove_btn)
                val bookImg = alertDialog.findViewById<ImageView>(R.id.dialog_cal_img_iv)
                val cancelBtn = alertDialog.findViewById<ImageButton>(R.id.dialog_cal_cancel_btn)

                Glide.with(context)
                    .load(imgUrl)
                    .placeholder(R.drawable.img_loading)
                    .error(R.drawable.img_not)
                    .override(470,530)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(bookImg!!)


                dateTitleTv?.text = "${readingDay.year}??? ${readingDay.month}??? ${readingDay.day}???"
                bookNameTv?.text = "${readingDay.book}"
                progressBar?.max = readingDay.maxPage?.toInt()!!
                progressBar?.progress = readingDay.readEd!!
                progressTv?.text = floor((readingDay.readEd!!.toDouble()/ readingDay.maxPage!!.toDouble())*100).toInt().toString()+ "%"
                pageTv?.text = "${readingDay.readEd} / ${readingDay.maxPage} ?????????"

                val readingPage = readingDay.readEd!!.toInt() - readingDay.readSt!!.toInt()
                readingTv?.text = "$readingPage ?????? ???????????????."


                editBtn?.setOnClickListener {
                    alertDialog.dismiss()
                    val action = CalendarFragmentDirections.actionCalendarFragmentToWriteReadingFragment(item)
                    findNavController().navigate(action)
                }

                removeBtn?.setOnClickListener { view ->
                    val util = Util()
                    val removeBook : () -> Unit = {
                        mainViewModel.removeReadingDay(readingDay.year, readingDay.month, readingDay.day, selectedDate)
                        alertDialog.dismiss()
                    }
                    util.showDialog(context, removeBook,"????????? ?????? ???????????????????", "??????")
                }

                cancelBtn?.setOnClickListener {
                    alertDialog.dismiss()
                }


            }
    }

    // ?????? ?????? ??????
    private fun monthYearFromDate(date : LocalDate) : String {
        val formatter = DateTimeFormatter.ofPattern("yyyy??? MM???")
        return date.format(formatter)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.setCalendar(selectedDate)
    }

}


















