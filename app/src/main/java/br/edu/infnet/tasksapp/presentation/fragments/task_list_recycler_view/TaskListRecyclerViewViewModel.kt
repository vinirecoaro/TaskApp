package br.edu.infnet.tasksapp.presentation.fragments.task_list_recycler_view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.infnet.tasksapp.data.DataStoreManager
import br.edu.infnet.tasksapp.domain.usecase.GetAllTasksUseCase
import br.edu.infnet.tasksapp.domain.usecase.InsertTasksUseCase
import br.edu.infnet.tasksapp.presentation.activities.main.MainActivityState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class TaskListRecyclerViewViewModel(
    context : Context,
    private val getAllTasksUseCase : GetAllTasksUseCase,
) : ViewModel() {

    private val _state = MutableSharedFlow<TaskListFragmentState>()
    val state : SharedFlow<TaskListFragmentState> = _state
    private val dataStoreManager = DataStoreManager.getInstance(context)

    fun getAllTasks(userId : String) = viewModelScope.launch {
        getAllTasksUseCase(userId)
            .flowOn(Dispatchers.Main)
            .onStart {
                _state.emit(TaskListFragmentState.Loading)
            }.catch {
                _state.emit(TaskListFragmentState.Error("Erro"))
            }.collect {tasks ->
                if(tasks.isEmpty()){
                    _state.emit(TaskListFragmentState.Empty)
                    _state.emit(TaskListFragmentState.Success(emptyList()))
                }else{
                    _state.emit(TaskListFragmentState.Success(tasks))
                }
            }
    }

    fun getUserId() : Flow<String> {
        return dataStoreManager.getUserId()
    }

}