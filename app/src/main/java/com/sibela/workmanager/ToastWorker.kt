package com.sibela.workmanager

import android.content.Context
import android.os.Handler
import android.os.Looper
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
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }, 1_000)
        return Result.success()
    }
}