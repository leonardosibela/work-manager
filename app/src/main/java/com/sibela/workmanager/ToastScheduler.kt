package com.sibela.workmanager

import android.content.Context
import androidx.work.*

class ToastScheduler {

    companion object {

        fun schedule(context: Context, text: String) {
            val data = workDataOf(
                ToastWorker.TEXT_KEY to text,
            )

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val storageUploadWorker = OneTimeWorkRequestBuilder<ToastWorker>()
                .setInputData(data)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context)
                .beginUniqueWork(
                    ToastWorker.WORKER_NAME,
                    ExistingWorkPolicy.REPLACE,
                    storageUploadWorker
                ).enqueue()
        }
    }
}