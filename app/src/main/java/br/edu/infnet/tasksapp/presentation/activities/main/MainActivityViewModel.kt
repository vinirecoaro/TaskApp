package br.edu.infnet.tasksapp.presentation.activities.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.infnet.tasksapp.api.FirebaseAPI
import br.edu.infnet.tasksapp.data.DataStoreManager
import br.edu.infnet.tasksapp.domain.model.TaskDomain
import br.edu.infnet.tasksapp.domain.usecase.GetAllTasksUseCase
import br.edu.infnet.tasksapp.domain.usecase.InsertTasksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class MainActivityViewModel(
    context : Context,
    private val getAllTasksUseCase : GetAllTasksUseCase,
    private val insertTasksUseCase: InsertTasksUseCase,
) : ViewModel() {

    private val firebaseAPI = FirebaseAPI.instance
    private val dataStoreManager = DataStoreManager.getInstance(context)

    fun insert(title : String, description : String, userId : String) = viewModelScope.launch{
        insertTasksUseCase(TaskDomain(
            title = title,
            description = description,
            userId = userId
        ))
    }

    suspend fun logoff(){
        viewModelScope.async (Dispatchers.IO){
            firebaseAPI.logoff()
        }
    }

    fun getUserId() : Flow<String> {
        return dataStoreManager.getUserId()
    }

}