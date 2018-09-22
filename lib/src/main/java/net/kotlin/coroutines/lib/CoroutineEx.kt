package net.kotlin.coroutines.lib

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.experimental.*
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.EmptyCoroutineContext

/**
 * 协程支持生命周期
 * Created by zhongyongsheng on 2018/9/20.
 */
class CoroutineLifecycle {
    var mLastEvent: Lifecycle.Event = Lifecycle.Event.ON_ANY
    var mTargetEvent: Lifecycle.Event = Lifecycle.Event.ON_ANY

    fun <T> observe(lifecycleOwner: LifecycleOwner, deferred: Deferred<T>) {
        lifecycleOwner.lifecycle.addObserver(object: LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            fun onStateChanged(owner:LifecycleOwner, event: Lifecycle.Event) {
                if (mLastEvent == Lifecycle.Event.ON_ANY) {
                    mLastEvent = event
                    when (mLastEvent) {
                        Lifecycle.Event.ON_CREATE -> mTargetEvent = Lifecycle.Event.ON_DESTROY
                        Lifecycle.Event.ON_START -> mTargetEvent = Lifecycle.Event.ON_STOP
                        Lifecycle.Event.ON_RESUME -> mTargetEvent = Lifecycle.Event.ON_PAUSE
                    }
                }
                if (event == mTargetEvent) {
                    //cancel the coroutine
                    deferred.cancel()
                }
                if (owner.lifecycle.currentState == Lifecycle.Event.ON_DESTROY) {
                    owner.lifecycle.removeObserver(this)
                    return
                }
            }
        })
    }
}

/**
 * 执行异步任务并绑定生命周期
 */
fun <T> GlobalScope.asyncWithLifecycle(lifecycleOwner: LifecycleOwner,
                                       context: CoroutineContext = EmptyCoroutineContext,
                                       start: CoroutineStart = CoroutineStart.DEFAULT,
                                       onCompletion: CompletionHandler? = null,
                                       block: suspend CoroutineScope.() -> T): Deferred<T> {
    val job = GlobalScope.async(context, start, onCompletion, block)
    net.kotlin.coroutines.lib.CoroutineLifecycle().observe(lifecycleOwner, job)
    return job
}

/**
 * 为协程block绑定lifecycle生命周期
 */
inline fun <T> GlobalScope.bindLifecycle(lifecycleOwner: LifecycleOwner,
                                       block: CoroutineScope.() -> Deferred<T>): Deferred<T> {
    val job = block.invoke(this)
    net.kotlin.coroutines.lib.CoroutineLifecycle().observe(lifecycleOwner, job)
    return job
}