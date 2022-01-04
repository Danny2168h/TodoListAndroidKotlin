package com.project.todolist.screens.todolist.threadWorkers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.project.todolist.Graph
import com.project.todolist.R

class MoveToCompletedWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        Graph.provide(this.applicationContext)
        val workManager = WorkManager.getInstance(this.applicationContext)
        workManager.cancelAllWorkByTag(inputData.getString(this.applicationContext.getString(R.string.notifID))!!)
        Graph.todoRepo.moveTodoItemToCompleted(
            inputData.getLong(this.applicationContext.getString(R.string.listID), -1),
            inputData.getString(this.applicationContext.getString(R.string.itemID))
        )
        return Result.success()
    }
}