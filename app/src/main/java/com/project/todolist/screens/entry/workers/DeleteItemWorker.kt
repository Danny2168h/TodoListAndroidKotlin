package com.project.todolist.screens.entry.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.project.todolist.Graph

class DeleteItemWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        Graph.provide(this.applicationContext)
        Graph.todoRepo.deleteTodoItemFromActive(
            inputData.getLong("LIST_ID", -1),
            inputData.getString("ITEM_ID")
        )
        return Result.success()
    }
}