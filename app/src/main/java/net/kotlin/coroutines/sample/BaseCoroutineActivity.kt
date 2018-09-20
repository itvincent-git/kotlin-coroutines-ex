package net.kotlin.coroutines.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.experimental.*
import net.kotlin.coroutines.lib.asyncWithLifecycle

class BaseCoroutineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_coroutine)

        async {
            LogUtil.debug("async")
        }

        repeat(3) {
            launch {
                LogUtil.debug("launch $it")
            }
        }

        launch(context = newSingleThreadContext("new-thread")) {
            LogUtil.debug("launch with context")
        }

        asyncWithLifecycle(this) {
            LogUtil.debug("delay")
            delay(3000)
            100
        }.apply {
            launch {
                LogUtil.debug("async after delay 3000 result ${this@apply.await()}")
            }
        }

        async {
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


    }
}
