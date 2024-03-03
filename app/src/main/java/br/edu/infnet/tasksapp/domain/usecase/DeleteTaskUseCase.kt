package br.edu.infnet.tasksapp.domain.usecase

import br.edu.infnet.tasksapp.domain.model.TaskDomain
import br.edu.infnet.tasksapp.domain.repository.TaskRepository

class DeleteTaskUseCase constructor(
    private val repository : TaskRepository
) {
    suspend operator fun invoke(task : TaskDomain) = repository.delete(task)
}