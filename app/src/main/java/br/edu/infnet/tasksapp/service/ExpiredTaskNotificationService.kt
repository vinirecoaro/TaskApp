package br.edu.infnet.tasksapp.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import br.edu.infnet.tasksapp.domain.usecase.GetAllTasksUseCase
import org.koin.android.ext.android.inject

class ExpiredTaskNotificationService : Service() {

    private lateinit var appContext : Context
    private val getAllTasksUseCase : GetAllTasksUseCase by inject()

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)

        val userId = intent.getStringExtra("user_id")

    }
}