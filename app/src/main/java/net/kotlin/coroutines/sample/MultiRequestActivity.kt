package net.kotlin.coroutines.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class MultiRequestActivity : AppCompatActivity() {
    val channel = Channel<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_request)

        GlobalScope.launch {
            val resultA = requestA()
            val resultB = requestB()
            LogUtil.debug("result A + B = ${resultA.await() + resultB.await()}")
        }
    }


    suspend fun requestA() = GlobalScope.async {
        requestDelayCallback(1000) {
            channel.send(it)
        }
        val ret = channel.receive()
        ret
    }

    suspend fun requestB() = GlobalScope.async {
        requestDelayCallback(2000) {
            channel.send(it)
        }
        channel.receive()
    }


    suspend fun requestDelayCallback(delayTime:Int, callback:suspend (Int) -> Unit){
        delay(delayTime)
        callback(delayTime)
    }
}
