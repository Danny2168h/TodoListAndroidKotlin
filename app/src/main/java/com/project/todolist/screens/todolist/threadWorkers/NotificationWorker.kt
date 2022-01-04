package com.project.todolist.screens.todolist.threadWorkers

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.project.todolist.Graph
import com.project.todolist.MainActivity
import com.project.todolist.R
import java.util.*

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val itemID = inputData.getString(this.applicationContext.getString(R.string.itemID))
        val itemIDasInt = UUID.fromString(itemID!!).leastSignificantBits.toInt()
        val listID = inputData.getLong(this.applicationContext.getString(R.string.listID), -1)

        Graph.provide(this.applicationContext)
        val todoItem = Graph.todoRepo.getListWithID(listID).todoItems.find { it.uniqueID == itemID }

        var builder = NotificationCompat.Builder(
            this.applicationContext,
            MainActivity.EXPIRED_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.mountains_darkened)
            .setContentTitle(this.applicationContext.getString(R.string.TodoItemhasExpired))
            .setContentText("Please check: ${todoItem!!.title}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this.applicationContext)) {
            notify(itemIDasInt, builder.build())
        }
        return Result.success()
    }
}