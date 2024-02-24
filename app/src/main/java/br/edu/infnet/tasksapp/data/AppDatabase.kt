package br.edu.infnet.tasksapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import br.edu.infnet.tasksapp.data.dao.TaskDao
import br.edu.infnet.tasksapp.data.model.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao
}