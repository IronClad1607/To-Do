package com.ironclad1607.todolist.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.ironclad1607.todolist.R
import com.ironclad1607.todolist.db.AppDatabase
import com.ironclad1607.todolist.model.ToDoModel
import kotlinx.android.synthetic.main.activity_new_task.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class NewTaskActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var myCalendar: Calendar

    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    private var finalDate = 0L
    private var finalTime = 0L

    private val labels = arrayListOf("Personal", "Business", "Insurance", "Shopping", "Banking")

    private val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        dateEdt.setOnClickListener(this)
        timeEdt.setOnClickListener(this)
        btnSave.setOnClickListener(this)

        setUpSpinner()
    }

    private fun setUpSpinner() {
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, labels)

        labels.sort()
        spinnerCaterogy.adapter = adapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dateEdt -> {
                setDateListener()
            }
            R.id.timeEdt -> {
                setTimeListener()
            }
            R.id.btnSave -> {
                saveTodo()
            }
        }
    }


    private fun saveTodo() {
        val category = spinnerCaterogy.selectedItem.toString()
        val title = titleInpLay.editText?.text.toString()
        val description = taskInpLay.editText?.text.toString()

        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) {
                return@withContext db.todoDao()
                    .insertTask(
                        ToDoModel(
                            title,
                            description,
                            category,
                            finalDate,
                            finalTime
                        )
                    )
            }
//            startAlarm(myCalendar)
            finish()
        }
    }

//    private fun startAlarm(myCalendar: Calendar) {
//        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(this, AlertReceiver::class.java)
//        val pi = PendingIntent.getBroadcast(this, 1, intent, 0)
//
//        if (myCalendar.before(Calendar.getInstance())) {
//            myCalendar.add(Calendar.DATE, 1)
//        }
//
//        am.setExact(AlarmManager.RTC_WAKEUP, myCalendar.timeInMillis, pi)
//
//    }

    private fun setTimeListener() {
        myCalendar = Calendar.getInstance()

        timeSetListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay, min ->
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            myCalendar.set(Calendar.MINUTE, min)
            updateTime()
        }

        val timePickerDialog = TimePickerDialog(
            this,
            timeSetListener,
            myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateTime() {
        val myFormat = "h:mm a"
        val sdf = SimpleDateFormat(myFormat)
        finalTime = myCalendar.time.time
        timeEdt.setText(sdf.format(myCalendar.time))
    }

    private fun setDateListener() {
        myCalendar = Calendar.getInstance()

        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            updateDate()
        }

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateDate() {
        val myFormat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myFormat)
        finalDate = myCalendar.time.time
        dateEdt.setText(sdf.format(myCalendar.time))

        timeInptLay.visibility = View.VISIBLE
    }
}
