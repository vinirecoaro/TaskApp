package br.edu.infnet.tasksapp.data.repository

import br.edu.infnet.tasksapp.data.dao.TaskDao
import br.edu.infnet.tasksapp.data.mapper.toDomain
import br.edu.infnet.tasksapp.data.mapper.toEntity
import br.edu.infnet.tasksapp.domain.model.TaskDomain
import br.edu.infnet.tasksapp.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(private val dao : TaskDao) : TaskRepository {
    override suspend fun insert(task: TaskDomain) = withContext(Dispatchers.IO){
        dao.insert(task.toEntity())
    }

    override suspend fun getAll(): Flow<List<TaskDomain>> = withContext(Dispatchers.IO) {
        dao.getAll().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun update(task: TaskDomain) = withContext(Dispatchers.IO){
        dao.update(task.toEntity())
    }

    override suspend fun delete(task: TaskDomain) = withContext(Dispatchers.IO){
        dao.delete(task.toEntity())
    }
}