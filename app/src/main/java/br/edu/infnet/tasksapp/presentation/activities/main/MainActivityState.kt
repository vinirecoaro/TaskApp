package br.edu.infnet.tasksapp.presentation.activities.main

import br.edu.infnet.tasksapp.domain.model.TaskDomain

sealed interface MainActivityState {
    data object Loading : MainActivityState
    data object Empty : MainActivityState
    data class Success(val tasks : List<TaskDomain>) : MainActivityState
    data class Error(val message : String) : MainActivityState
}