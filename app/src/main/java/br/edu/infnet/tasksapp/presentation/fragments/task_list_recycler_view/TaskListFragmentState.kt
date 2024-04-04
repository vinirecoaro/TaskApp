package br.edu.infnet.tasksapp.presentation.fragments.task_list_recycler_view

import br.edu.infnet.tasksapp.domain.model.TaskDomain

sealed interface TaskListFragmentState {
    data object Loading : TaskListFragmentState
    data object Empty : TaskListFragmentState
    data class Success(val tasks : List<TaskDomain>) : TaskListFragmentState
    data class Error(val message : String) : TaskListFragmentState
}