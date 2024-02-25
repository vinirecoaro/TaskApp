package br.edu.infnet.tasksapp.domain.model

typealias TaskDomain = Task
data class Task(
    val id : Int = 0,
    val title : String,
    val description : String
)