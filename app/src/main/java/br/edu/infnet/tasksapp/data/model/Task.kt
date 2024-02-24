package br.edu.infnet.tasksapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

typealias TaskEntity = Task

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "title") val title : String,
    @ColumnInfo(name = "description") val description : String
)