package net.kotlin.coroutines.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.coroutines.*
import java.util.*

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
        sendWithCallback {
            deferred.complete(it)
        }
        return deferred
    }

    fun sendWithCallback(callback: (Int) -> Unit) {
        window.decorView.postDelayed({
            callback(Random(System.currentTimeMillis()).nextInt())
        }, 2000)
    }
}
