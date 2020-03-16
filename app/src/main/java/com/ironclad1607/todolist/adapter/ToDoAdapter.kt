package com.ironclad1607.todolist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ironclad1607.todolist.R
import com.ironclad1607.todolist.model.ToDoModel
import kotlinx.android.synthetic.main.item_todo.view.*
import java.text.SimpleDateFormat
import java.util.*

class ToDoAdapter(val list: List<ToDoModel>) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemId(position: Int): Long {
        return list[position].id
    }

    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(toDoModel: ToDoModel) {
            with(itemView) {
                val colors = resources.getIntArray(R.array.random_color)
                val randomColor = colors[Random().nextInt(colors.size)]
                viewColorTag.setBackgroundColor(randomColor)
                txtShowTitle.text = toDoModel.title
                txtShowTask.text = toDoModel.description
                txtShowCaterogy.text = toDoModel.catergory
                updateTime(toDoModel.time)
                updateDate(toDoModel.date)
            }
        }

        @SuppressLint("SimpleDateFormat")
        private fun updateTime(time: Long) {
            val myFormat = "h:mm a"
            val sdf = SimpleDateFormat(myFormat)
            itemView.txtShowTime.text = sdf.format(Date(time))
        }

        @SuppressLint("SimpleDateFormat")
        private fun updateDate(time: Long) {
            val myFormat = "EEE, d MMM yyyy"
            val sdf = SimpleDateFormat(myFormat)
            itemView.txtShowDate.text = sdf.format(Date(time))

        }
    }
}