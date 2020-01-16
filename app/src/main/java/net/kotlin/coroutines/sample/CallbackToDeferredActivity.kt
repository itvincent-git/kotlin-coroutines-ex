package net.kotlin.coroutines.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 将原来使用callback的方法，改为协程的方式
 */
class CallbackToDeferredActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_callback_to_deferred)

        GlobalScope.launch(context = Dispatchers.Main) {
            val response = sendRequest().await()
            Toast.makeText(this@CallbackToDeferredActivity, "response $response", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendRequest(): Deferred<Int> {
        val deferred = CompletableDeferred<Int>()
        saveToRepositoryCallback({
            deferred.complete(it)
        }, {
        })
        return deferred
    }

    suspend fun saveToRepository(): Int {
        return suspendCoroutine { continuation ->
            saveToRepositoryCallback({
                continuation.resume(it)
            }, {
                continuation.resumeWithException(it)
            })
        }
    }

    fun saveToRepositoryCallback(success: (Int) -> Unit, error: (Throwable) -> Unit) {
        window.decorView.postDelayed({
            success(Random(System.currentTimeMillis()).nextInt())
        }, 2000)
    }
}
