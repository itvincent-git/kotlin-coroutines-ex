package net.kotlin.coroutines.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_multi_request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import net.kotlin.coroutines.lib.bindLifecycle

class MultiRequestActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_request)

        GlobalScope.bindLifecycle(this) {
            GlobalScope.async {
                val result = requestA().await() + requestB().await()
                LogUtil.debug("result A + B = ${result}")
                GlobalScope.async(context = Dispatchers.Main) {
                    result_value.text = "$result"
                }
            }
        }
    }

    suspend fun requestA() = GlobalScope.async {
        val channel = Channel<Long>()
        requestDelayCallback(500L) {
            channel.send(it)
        }
        val ret = channel.receive()
        ret
    }

    suspend fun requestB() = GlobalScope.async {
        val channel = Channel<Long>()
        requestDelayCallback(1000) {
            channel.send(it)
        }
        channel.receive()
    }

    suspend fun requestDelayCallback(delayTime: Long, callback: suspend (Long) -> Unit) = GlobalScope.async {
        delay(delayTime)
        callback(delayTime)
    }
}
