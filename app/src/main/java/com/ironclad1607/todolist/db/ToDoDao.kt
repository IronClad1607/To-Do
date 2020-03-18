package com.ironclad1607.todolist.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ironclad1607.todolist.model.ToDoModel

@Dao
interface ToDoDao {
    @Insert
    suspend fun insertTask(toDoModel: ToDoModel): Long

    @Query("SELECT * FROM ToDoModel WHERE isFinished == 0")
    fun getTask(): LiveData<List<ToDoModel>>

    @Query("UPDATE ToDoModel SET isFinished = 1 where id=:uid")
    fun finishTask(uid: Long)

    @Query("DELETE from ToDoModel where id= :uid")
    fun deleteTask(uid: Long)

    @Query("SELECT * FROM ToDoModel WHERE isFinished == 1")
    fun getDoneTask(): LiveData<List<ToDoModel>>

    @Query("UPDATE ToDoModel SET isFinished = 0 where id=:uid")
    fun unFinishTask(uid: Long)
}