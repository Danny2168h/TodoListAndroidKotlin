package com.project.todolist.screens.todolist.counterremove

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.project.todolist.Graph

class RemoveItemWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        Graph.todoRepo.deleteTodoItem(
            inputData.getLong("LIST_ID", -1),
            inputData.getString("ITEM_ID")
        )
        return Result.success()
    }
}