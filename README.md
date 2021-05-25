# work-manager

An example showing how to use [Work Manager](https://developer.android.com/reference/androidx/work/WorkManager), a Jetpack library compatible with API 14+ for managing deferrable and garanteed background work, deferrable meaning a job that can be postponed and garanteed meaning... well... garanteed.

Here I separate the schedule from the work itself, which also allowed me to create a type safe way to pass paramaters to the work.

## Worker class

First we have to creat our work class, a class that will extend the WorkManager class, override the doWork method were we'll describe the actuall job that we want to execute. In our case is to display a toast on the screen with a value that was passed as a parameter:

```kotlin
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
```

We get the parameters passed to the worker through the workerParameters object of the workmanager class:

```kotlin
private val text = workerParameters.inputData.getString(TEXT_KEY)!!
```

The doWork must return a result, that can be a success or a failure or a success, which will let the workmanager class to understand if the task was executed or not (and should be executed again later):

```kotlin
override fun doWork(): Result {
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed({
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }, 1_000)
    return Result.success()
}

```


## Scheduler class

Our schedule class is a class with a static method called schedule that will receive a context value and the parameters we want to pass to it. In our case is the text that will be displayed. The first thing we have to do with the parameters is to save then in a Data object, a work manager map:

```kotlin
fun schedule(context: Context, text: String) {
    val data = workDataOf(ToastWorker.TEXT_KEY to text)
    ...
}
```

Now we have to create a Constraints object to speficy which conditions we are going to set to our work. I our case, we want our work to run when the user has internet connection:

```kotlin
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .build()
```

Now we must create a WorkRequest of our work class passing our parameters and our constraints like so:

```kotlin
val storageUploadWorker = OneTimeWorkRequestBuilder<ToastWorker>()
    .setInputData(data)
    .setConstraints(constraints)
    .build()
```

Then, all you have to do is to enqueue your workrequest passing the work name, the existing work policy (that will be reponsible to define what will happen with another work with the same name is also enqueued) and the workrequest itself:

```kotlin
WorkManager.getInstance(context)
    .beginUniqueWork(
        ToastWorker.WORKER_NAME,
        ExistingWorkPolicy.REPLACE,
        storageUploadWorker
    ).enqueue()
```
