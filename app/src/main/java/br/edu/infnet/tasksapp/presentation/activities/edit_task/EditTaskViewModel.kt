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

    fun update(id : Int, title : String, description : String, userId : String, expirationDate : String) = viewModelScope.launch {
        updateTasksUseCase(TaskDomain(
            id = id,
            title = title,
            description = description,
            userId = userId,
            expirationDate = expirationDate
        ))
    }

    fun delete(id : Int, title : String, description : String, userId : String, expirationDate : String) = viewModelScope.launch {
        deleteTaskUseCase(
            TaskDomain(
                id = id,
                title = title,
                description = description,
                userId = userId,
                expirationDate = expirationDate
            )
        )
    }

}