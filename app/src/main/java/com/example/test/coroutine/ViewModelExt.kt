package androidx.lifecycle

import android.util.Log
import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

private const val JOB_KEY = "androidx.lifecycle.ViewModel&WithCustomSupervisorJob.JOB_KEY"
private const val TAG = "CombinedScope"


/**
 * @author QuincyJiang
 * Created at 2020/12/10.
 * 同时使用两种不同Scope，并为之做交集
 */
fun Combined.launch(
    dispatcher: CoroutineDispatcher = (bindScope.coroutineContext[ContinuationInterceptor] as? CoroutineDispatcher)
        ?: Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
): Job {
    val job = self.launch(context = dispatcher, block = block)
    bindScope.coroutineContext[Job]?.invokeOnCompletion {
        it?.let {
            Log.d(TAG, "Bind scope is canceled.")
            job.cancel()
        }
    }?.also {
        Log.d(TAG, "add dispose handler to viewModel, key: $it")
        self.viewModel.setTagIfAbsent(it.toString(), ClosableDisposeHandler(it))
    }
    return job
}

fun <T> Combined.async(
    dispatcher: CoroutineDispatcher = (bindScope.coroutineContext[ContinuationInterceptor] as? CoroutineDispatcher)
        ?: Dispatchers.Main,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    val job = self.async(context = dispatcher, block = block)
    bindScope.coroutineContext[Job]?.invokeOnCompletion {
        it?.let {
            Log.d(TAG, "Bind scope is canceled.")
            job.cancel()
        }
    }?.also {
        Log.d(TAG, "add dispose handler to viewModel, key: $it")
        self.viewModel.setTagIfAbsent(it.toString(), ClosableDisposeHandler(it))
    }
    return job
}


fun CombinedScope.combined(bindScope: CoroutineScope): Combined {
    return Combined(this, bindScope)
}

class Combined(internal val self: CombinedScope, internal val bindScope: CoroutineScope)

/**
 * A combined coroutinesScope
 * All coroutines under this scope will be canceled whenever this or it`s binding scope is canceled.
 * */
val ViewModel.combinedScope: CombinedScope
    get() {
        val thisScope: CombinedScope? = this.getTag(JOB_KEY)
        if (thisScope != null) {
            return thisScope
        }
        return setTagIfAbsent(
            JOB_KEY,
            CombinedScope(this, SupervisorJob() + Dispatchers.Main)
        )
    }

suspend fun switchToMainThread(block: suspend CoroutineScope.() -> Unit) =
    withContext(Dispatchers.Main) {
        block()
    }

class CombinedScope(internal val viewModel: ViewModel, context: CoroutineContext) : Closeable,
    CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}

internal class ClosableDisposeHandler(val handle: DisposableHandle) : Closeable {
    override fun close() {
        Log.d(TAG, "InvokeOnCompletion callback has been disposed")
        handle.dispose()
    }
}
