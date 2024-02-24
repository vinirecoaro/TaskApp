package br.edu.infnet.tasksapp.domain.usecase

import br.edu.infnet.tasksapp.domain.model.TaskDomain
import br.edu.infnet.tasksapp.domain.repository.TaskRepository

class UpdateAllTasksUseCase constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task : TaskDomain) = repository.update(task)
}