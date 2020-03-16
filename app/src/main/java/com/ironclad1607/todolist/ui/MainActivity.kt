package com.ironclad1607.todolist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ironclad1607.todolist.R
import com.ironclad1607.todolist.model.ToDoModel

class MainActivity : AppCompatActivity() {

    val list = arrayListOf<ToDoModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
