package com.project.todolist.screens.main.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.project.todolist.Graph
import com.project.todolist.R

class DeleteListWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        Graph.provide(this.applicationContext)
        val workManager = WorkManager.getInstance(this.applicationContext)
        val todoList = Graph.todoRepo.getListWithID(
            inputData.getLong(
                this.applicationContext.getString(
                    R.string.listID
                ), -1
            )
        )

        todoList.todoItems.map {
            if (it.imagePath != null) {
                this.applicationContext.deleteFile(it.imagePath)
            }
            workManager.cancelAllWorkByTag(it.notificationID)
        }
        Graph.todoRepo.deleteTodoList(
            inputData.getLong(
                this.applicationContext.getString(
                    R.string.listID
                ), -1
            )
        )
        return Result.success()
    }
}