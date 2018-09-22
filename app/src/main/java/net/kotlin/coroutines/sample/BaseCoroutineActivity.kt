package net.kotlin.coroutines.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.Channel
import net.kotlin.coroutines.lib.asyncWithLifecycle

class BaseCoroutineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_coroutine)

        GlobalScope.async {
            LogUtil.debug("async")
        }

        repeat(3) {
            GlobalScope.launch {
                LogUtil.debug("launch $it")
            }
        }

        GlobalScope.launch(context = newSingleThreadContext("new-thread")) {
            LogUtil.debug("launch with context")
        }

        GlobalScope.asyncWithLifecycle(this) {
            LogUtil.debug("delay")
            delay(3000)
            100
        }.apply {
            GlobalScope.launch {
                LogUtil.debug("async after delay 3000 result ${this@apply.await()}")
            }
        }

        GlobalScope.async {
            val c1 = async {
                delay(1000)
                100
            }
            val c2 = async {
                delay(1000)
                200
            }
            LogUtil.debug("async await = ${c1.await() + c2.await()}")
        }

        GlobalScope.async {
            withTimeout(1300L) {
                repeat(1000) { i ->
                    LogUtil.debug("I'm sleeping $i ...")
                    delay(500L)
                }
            }
        }

        GlobalScope.async {
            val channel = Channel<Int>()
            launch {
                // this might be heavy CPU-consuming computation or async logic, we'll just send five squares
                for (x in 1..5) channel.send(x * x)
            }
            // here we print five received integers:
            repeat(5) { LogUtil.debug("channel receive:${channel.receive()}") }
            LogUtil.debug("Done!")
        }

    }
}
