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
    fun insertAll(vararg tasks : TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getAll()

    @Update
    fun updateTasks(vararg tasks : TaskEntity)

    @Delete
    fun delete(task : TaskEntity)
}