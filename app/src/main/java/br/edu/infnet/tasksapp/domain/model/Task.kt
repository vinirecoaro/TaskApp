package br.edu.infnet.tasksapp.domain.model

typealias TaskDomain = Task
data class Task(
    val id : Int,
    val title : String,
    val description : String
)