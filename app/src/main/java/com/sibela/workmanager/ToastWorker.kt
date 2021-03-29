package com.sibela.workmanager

import android.content.Context
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters

class ToastWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    companion object {
        const val WORKER_NAME = "com.sibela.workmanager.ToastWorker"
        const val TEXT_KEY = "TEXT_KEY"
    }

    private val text = workerParameters.inputData.getString(TEXT_KEY)!!

    override fun doWork(): Result {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        return Result.success()
    }
}