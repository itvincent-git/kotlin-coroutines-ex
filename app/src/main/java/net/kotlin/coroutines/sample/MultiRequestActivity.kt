package net.kotlin.coroutines.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_multi_request.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.channels.Channel
import net.kotlin.coroutines.lib.asyncWithLifecycle
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
        val channel = Channel<Int>()
        requestDelayCallback(500) {
            channel.send(it)
        }
        val ret = channel.receive()
        ret
    }

    suspend fun requestB() = GlobalScope.async {
        val channel = Channel<Int>()
        requestDelayCallback(1000) {
            channel.send(it)
        }
        channel.receive()
    }


    suspend fun requestDelayCallback(delayTime:Int, callback:suspend (Int) -> Unit) = GlobalScope.async {
        delay(delayTime)
        callback(delayTime)
    }
}
