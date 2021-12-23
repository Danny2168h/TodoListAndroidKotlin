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
        val itemID = inputData.getString("ITEM_ID")
        val itemIDasInt = UUID.fromString(itemID!!).leastSignificantBits.toInt()
        val listID = inputData.getLong("LIST_ID", -1)

        val todoItem = Graph.todoRepo.getListWithID(listID).todoItems.find { it.uniqueID == itemID }

        var builder = NotificationCompat.Builder(
            MainActivity.applicationContext(),
            MainActivity.EXPIRED_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.mountains_darkened)
            .setContentTitle(todoItem!!.title)
            .setContentText("Your Todo item has expired!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(MainActivity.applicationContext())) {
            notify(itemIDasInt, builder.build())
        }
        return Result.success()
    }
}