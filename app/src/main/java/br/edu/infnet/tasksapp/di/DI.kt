package br.edu.infnet.tasksapp.di

import androidx.room.Room
import br.edu.infnet.tasksapp.data.AppDatabase
import br.edu.infnet.tasksapp.data.dao.TaskDao
import br.edu.infnet.tasksapp.data.repository.TaskRepositoryImpl
import br.edu.infnet.tasksapp.domain.usecase.GetAllTasksUseCase
import br.edu.infnet.tasksapp.domain.usecase.InsertTasksUseCase
import br.edu.infnet.tasksapp.presentation.activities.main.MainActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database-task"
        ).build()
    }

    factory {
        get<AppDatabase>().taskDao()
    }

    factory<TaskRepositoryImpl>{
        TaskRepositoryImpl(get<TaskDao>())
    }

    factory<InsertTasksUseCase>{
        InsertTasksUseCase(repository = get<TaskRepositoryImpl>())
    }

    factory<GetAllTasksUseCase>{
        GetAllTasksUseCase(repository = get<TaskRepositoryImpl>() )
    }

    viewModel<MainActivityViewModel>{
        MainActivityViewModel(
            androidContext(),
            getAllTasksUseCase = get<GetAllTasksUseCase>(),
            insertTasksUseCase = get<InsertTasksUseCase>()
        )
    }
}