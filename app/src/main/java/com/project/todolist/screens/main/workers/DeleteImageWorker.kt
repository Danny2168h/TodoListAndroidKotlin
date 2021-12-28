package com.project.todolist.screens.main.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.project.todolist.Graph

class DeleteImageWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        Graph.provide(this.applicationContext)
        val workManager = WorkManager.getInstance(this.applicationContext)
        val todoList = Graph.todoRepo.getListWithID(inputData.getLong("LIST_ID", -1))

        todoList.todoItems.map {
            if (it.imagePath != null) {
            this.applicationContext.deleteFile(it.imagePath)
        }
            workManager.cancelAllWorkByTag(it.notificationID)
        }
        Graph.todoRepo.deleteTodoList(inputData.getLong("LIST_ID", -1))
        return Result.success()
    }
}