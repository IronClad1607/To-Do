package com.ironclad1607.todolist.ui

import android.graphics.*
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ironclad1607.todolist.R
import com.ironclad1607.todolist.adapter.ToDoAdapter
import com.ironclad1607.todolist.db.AppDatabase
import com.ironclad1607.todolist.model.ToDoModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private val list = arrayListOf<ToDoModel>()
    var adapter = ToDoAdapter(list)

    private val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)

        rvTodo.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = this@HistoryActivity.adapter
        }

        initSwipe()
        db.todoDao().getDoneTask().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()
            } else {
                list.clear()
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun initSwipe() {
        val simpleItemTouchCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder,
                    target: ViewHolder
                ): Boolean = false

                override fun onSwiped(
                    viewHolder: ViewHolder,
                    direction: Int
                ) {
                    val positon = viewHolder.adapterPosition
                    if (direction == ItemTouchHelper.LEFT) {
                        GlobalScope.launch(Dispatchers.IO) {
                            db.todoDao().deleteTask(adapter.getItemId(positon))
                        }
                    } else if (direction == ItemTouchHelper.RIGHT) {
                        GlobalScope.launch(Dispatchers.IO) {
                            db.todoDao().unFinishTask(adapter.getItemId(positon))
                        }
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        val itemView = viewHolder.itemView

                        val paint = Paint()
                        val icon: Bitmap

                        if (dX < 0) {
                            icon = BitmapFactory.decodeResource(
                                resources,
                                R.drawable.baseline_delete_white_24dp
                            )

                            paint.color = Color.parseColor("#D32F2F")

                            c.drawRect(
                                itemView.right.toFloat() + dX,
                                itemView.top.toFloat(),
                                itemView.right.toFloat(),
                                itemView.bottom.toFloat(),
                                paint
                            )

                            c.drawBitmap(
                                icon, itemView.right.toFloat() - icon.width,
                                itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height.toFloat()) / 2,
                                paint
                            )
                        } else {
                            icon = BitmapFactory.decodeResource(
                                resources, R.drawable.baseline_clear_white_24dp
                            )

                            paint.color = Color.parseColor("#3C5CD7")

                            c.drawRect(
                                itemView.left.toFloat(),
                                itemView.top.toFloat(),
                                itemView.left.toFloat() + dX,
                                itemView.bottom.toFloat(),
                                paint
                            )

                            c.drawBitmap(
                                icon,
                                itemView.left.toFloat(),
                                itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height.toFloat()) / 2,
                                paint
                            )
                        }
                        viewHolder.itemView.translationX = dX
                    } else {
                        super.onChildDraw(
                            c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                        )
                    }
                }
            }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rvTodo)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return true
    }

}
