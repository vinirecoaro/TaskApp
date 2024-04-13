package br.edu.infnet.tasksapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import br.edu.infnet.tasksapp.domain.usecase.GetAllTasksUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
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

        val userId = intent.getStringExtra("user_id")

        CoroutineScope(Dispatchers.IO).launch {
            if (userId != null) {
                getAllTasksUseCase(userId).collect{taskList ->

                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                    val channel = NotificationChannel(
                        "Todo_channel", "Channel_Name",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    notificationManager.createNotificationChannel(channel)

                    var body = "Tem tarefas expiradas: "
                    for (item in taskList){

                        body = "$body\n${item.title}"

                    }
                    val notificationBuilder = NotificationCompat.Builder(appContext, "Todo_channel")
                        .setContentTitle("Tarefas expiradas")
                        .setContentText(body)
                        .setSmallIcon(androidx.core.R.drawable.notification_bg)
                        .setAutoCancel(true)

                    notificationManager.notify(12, notificationBuilder.build())
                }
            }
        }


        return super.onStartCommand(intent, flags, startId)
    }
}