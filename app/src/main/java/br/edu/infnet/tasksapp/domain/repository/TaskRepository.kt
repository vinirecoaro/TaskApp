package br.edu.infnet.tasksapp.domain.repository

import br.edu.infnet.tasksapp.domain.model.TaskDomain
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun insert(task: TaskDomain)

    suspend fun getAll(userId : String) : Flow<List<TaskDomain>>

    suspend fun update(task : TaskDomain)

    suspend fun delete(task : TaskDomain)


}