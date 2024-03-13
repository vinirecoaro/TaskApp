package br.edu.infnet.tasksapp.presentation.activities.edit_task

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import br.edu.infnet.tasksapp.data.DataStoreManager
import br.edu.infnet.tasksapp.data.db
import br.edu.infnet.tasksapp.data.repository.TaskRepositoryImpl
import br.edu.infnet.tasksapp.domain.model.TaskDomain
import br.edu.infnet.tasksapp.domain.usecase.DeleteTaskUseCase
import br.edu.infnet.tasksapp.domain.usecase.GetAllTasksUseCase
import br.edu.infnet.tasksapp.domain.usecase.UpdateTasksUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditTaskViewModel(
    context : Context,
    private val updateTasksUseCase: UpdateTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val dataStoreManager = DataStoreManager.getInstance(context)

    fun update(id : Int, title : String, description : String, userId : String) = viewModelScope.launch {
        updateTasksUseCase(TaskDomain(
            id = id,
            title = title,
            description = description,
            userId = userId
        ))
    }

    fun delete(id : Int, title : String, description : String, userId : String) = viewModelScope.launch {
        deleteTaskUseCase(TaskDomain(
            id = id,
            title = title,
            description = description,
            userId = userId
            ))
    }

    fun getUserId() : Deferred<String> {
        val userId = CompletableDeferred<String>()
        viewModelScope.launch {
            userId.complete(dataStoreManager.getUserId().first())
        }
        return userId
    }

    class Factory : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras : CreationExtras
        ): T {
            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            val repository = TaskRepositoryImpl(application.db.taskDao())
            return EditTaskViewModel(
                application,
                updateTasksUseCase = UpdateTasksUseCase(repository),
                deleteTaskUseCase = DeleteTaskUseCase(repository)
            ) as T
        }
    }
}