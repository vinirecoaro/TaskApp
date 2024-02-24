package br.edu.infnet.tasksapp.domain.repository

import br.edu.infnet.tasksapp.domain.model.TaskDomain

interface TaskRepository {

    suspend fun insert(task: TaskDomain)

    suspend fun getAll() : List<TaskDomain>

    suspend fun update(task : TaskDomain)

}