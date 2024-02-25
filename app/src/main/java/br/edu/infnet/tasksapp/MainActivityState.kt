package br.edu.infnet.tasksapp

import br.edu.infnet.tasksapp.domain.model.TaskDomain

sealed interface MainActivityState {
    object Loading : MainActivityState
    object Empty : MainActivityState
    data class Success(val tasks : List<TaskDomain>) : MainActivityState
    data class Error(val message : String) : MainActivityState
}