package br.edu.infnet.tasksapp.presentation.activities.edit_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import br.edu.infnet.tasksapp.data.db
import br.edu.infnet.tasksapp.data.repository.TaskRepositoryImpl
import br.edu.infnet.tasksapp.domain.model.TaskDomain
import br.edu.infnet.tasksapp.domain.usecase.DeleteTaskUseCase
import br.edu.infnet.tasksapp.domain.usecase.GetAllTasksUseCase
import br.edu.infnet.tasksapp.domain.usecase.UpdateTasksUseCase
import kotlinx.coroutines.launch

class EditTaskViewModel(
    private val updateTasksUseCase: UpdateTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {
    fun update(id : Int, title : String, description : String) = viewModelScope.launch {
        updateTasksUseCase(TaskDomain(
            id = id,
            title = title,
            description = description
        ))
    }

    fun delete(id : Int, title : String, description : String) = viewModelScope.launch {
        deleteTaskUseCase(TaskDomain(
            id = id,
            title = title,
            description = description
        ))
    }

    class Factory : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras : CreationExtras
        ): T {
            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            val repository = TaskRepositoryImpl(application.db.taskDao())
            return EditTaskViewModel(
                updateTasksUseCase = UpdateTasksUseCase(repository),
                deleteTaskUseCase = DeleteTaskUseCase(repository)
            ) as T
        }
    }
}