package br.edu.infnet.tasksapp.domain.usecase

import br.edu.infnet.tasksapp.domain.repository.TaskRepository

class GetAllTasksUseCase constructor(
    private val repository : TaskRepository
) {
    suspend operator fun invoke(userId : String) = repository.getAll(userId)
}