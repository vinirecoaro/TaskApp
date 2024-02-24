package br.edu.infnet.tasksapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.infnet.tasksapp.data.model.TaskEntity

@Dao
interface TaskDao {

    @Insert
    fun insert(task : TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getAll() : List<TaskEntity>

    @Update
    fun update(task : TaskEntity)

    @Delete
    fun delete(task : TaskEntity)
}