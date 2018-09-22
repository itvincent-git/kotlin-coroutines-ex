package net.kotlin.coroutines.lib

import android.util.Log

/**
 * 捕获block的异常，然后返回block的值，默认异常是打印logcat日志
 * Created by zhongyongsheng on 2018/9/20.
 */
fun <T> tryCatch(catchBlock:(Throwable) -> Unit = { t -> Log.i("TryEx", "tryCatchLogcat print:", t) },
        tryBlock:() -> T
): T? {
    try {
       return tryBlock()
    } catch (t: Throwable) {
        catchBlock(t)
    }
    return null
}
