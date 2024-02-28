package br.edu.infnet.tasksapp.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import br.edu.infnet.tasksapp.data.db
import br.edu.infnet.tasksapp.data.repository.TaskRepositoryImpl
import br.edu.infnet.tasksapp.domain.model.TaskDomain
import br.edu.infnet.tasksapp.domain.usecase.GetAllTasksUseCase
import br.edu.infnet.tasksapp.domain.usecase.InsertTasksUseCase
import br.edu.infnet.tasksapp.domain.usecase.UpdateTasksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flowOn

class MainActivityViewModel(
    private val getAllTasksUseCase : GetAllTasksUseCase,
    private val insertTasksUseCase: InsertTasksUseCase,
    private val updateTasksUseCase: UpdateTasksUseCase
) : ViewModel() {

    val state : LiveData<MainActivityState> = liveData {
        emit(MainActivityState.Loading)
        val state = try {
            val tasks = getAllTasksUseCase()
            if(tasks.isEmpty()){
                MainActivityState.Empty
            }else{
                MainActivityState.Success(tasks)
            }
        }catch (e : Exception){
            Log.e("Error", e.message.toString())
            MainActivityState.Error(e.message.toString())
        }
        emit(state)
    }

    fun insert(title : String, description : String) = viewModelScope.launch{
        insertTasksUseCase(TaskDomain(
            title = title,
            description = description
        ))
    }

    fun update(id : Int, title : String, description : String) = viewModelScope.launch {
        updateTasksUseCase(TaskDomain(
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
            return MainActivityViewModel(
                getAllTasksUseCase = GetAllTasksUseCase(repository),
                insertTasksUseCase = InsertTasksUseCase(repository),
                updateTasksUseCase = UpdateTasksUseCase(repository)
            ) as T
        }
    }
}