package br.edu.infnet.tasksapp.data.repository

import br.edu.infnet.tasksapp.api.FirebaseAPI
import br.edu.infnet.tasksapp.data.dao.TaskDao
import br.edu.infnet.tasksapp.data.mapper.toDomain
import br.edu.infnet.tasksapp.data.mapper.toEntity
import br.edu.infnet.tasksapp.domain.model.TaskDomain
import br.edu.infnet.tasksapp.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(private val dao : TaskDao, private val firebaseAPI : FirebaseAPI) : TaskRepository {
    override suspend fun insert(task: TaskDomain): Unit = withContext(Dispatchers.IO){
        val id = dao.insert(task.toEntity())
        val taskToFirebase = TaskDomain(
            id.toInt(),
            task.title,
            task.description,
            task.userId
        )
        firebaseAPI.addTask(taskToFirebase)
    }

    override suspend fun getAll(userId : String): Flow<List<TaskDomain>> = withContext(Dispatchers.IO) {
        dao.getAll(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun update(task: TaskDomain) = withContext(Dispatchers.IO){
        dao.update(task.toEntity())
    }

    override suspend fun delete(task: TaskDomain) : Unit = withContext(Dispatchers.IO){
        dao.delete(task.toEntity())
        firebaseAPI.deleteTask(task)
    }
}