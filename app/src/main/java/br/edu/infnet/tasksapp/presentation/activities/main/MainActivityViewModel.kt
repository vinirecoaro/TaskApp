package br.edu.infnet.tasksapp.presentation.activities.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import br.edu.infnet.tasksapp.api.FirebaseAPI
import br.edu.infnet.tasksapp.data.db
import br.edu.infnet.tasksapp.data.repository.TaskRepositoryImpl
import br.edu.infnet.tasksapp.domain.model.TaskDomain
import br.edu.infnet.tasksapp.domain.usecase.GetAllTasksUseCase
import br.edu.infnet.tasksapp.domain.usecase.InsertTasksUseCase
import br.edu.infnet.tasksapp.domain.usecase.UpdateTasksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class MainActivityViewModel(
    private val getAllTasksUseCase : GetAllTasksUseCase,
    private val insertTasksUseCase: InsertTasksUseCase,
    private val updateTasksUseCase: UpdateTasksUseCase,
) : ViewModel() {

    private val _state = MutableSharedFlow<MainActivityState>()
    val state : SharedFlow<MainActivityState> = _state
    private val firebaseAPI = FirebaseAPI.instance

    init {
        getAllTasks()
    }

     fun getAllTasks() = viewModelScope.launch {
        getAllTasksUseCase()
            .flowOn(Dispatchers.Main)
            .onStart {
                _state.emit(MainActivityState.Loading)
            }.catch {
                _state.emit(MainActivityState.Error("Erro"))
            }.collect {tasks ->
                if(tasks.isEmpty()){
                    _state.emit(MainActivityState.Empty)
                }else{
                    _state.emit(MainActivityState.Success(tasks))
                }
            }
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

    suspend fun logoff(){
        viewModelScope.async (Dispatchers.IO){
            firebaseAPI.logoff()
        }
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