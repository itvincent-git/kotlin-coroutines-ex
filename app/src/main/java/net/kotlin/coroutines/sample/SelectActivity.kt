package net.kotlin.coroutines.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_select.button
import kotlinx.android.synthetic.main.activity_select.button2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import net.slog.SLoggerFactory
import net.stripe.lib.forEach
import net.stripe.lib.lifecycleScope

class SelectActivity : AppCompatActivity() {
    val log = SLoggerFactory.getLogger("SelectActivity")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

//        GlobalScope.launch {
//            val a = produce<String> {
//                repeat(6) {
//                    send("Hello $it")
//                    delay(100)
//                }
//            }
//            val b = produce<String> {
//                repeat(4) {
//                    send("World $it")
//                    delay(150)
//                }
//            }
//            repeat(8) { // 打印最早的八个结果
//                log.debug(selectAorB(a, b))
//            }
//        }
        val high = Channel<Long>(1024)
        val low = Channel<Long>(1024)

        button.setOnClickListener {
            lifecycleScope.launch {
                repeat(50) {
                    low.send(-System.currentTimeMillis())
                }
                log.debug("low sent")
            }
            lifecycleScope.launch {
                repeat(50) {
                    high.send(System.currentTimeMillis())
                }
                log.debug("high sent")
            }
        }

        button2.setOnClickListener {
            lifecycleScope.launch {
                repeat(50) {
                    low.send(-System.currentTimeMillis())
                    delay(100)
                }
            }
        }

        lifecycleScope.launch {
            priorityProducer(high, low).forEach {
                log.debug("priority receive $it")
            }
        }
    }

    fun CoroutineScope.priorityProducer(high: ReceiveChannel<Long>, low: ReceiveChannel<Long>) = produce<Long> {
        while (isActive) { // 循
            val value = select<Long?> {
                high.onReceiveOrNull {
                    it
                }

                low.onReceiveOrNull {
                    it
                }
            }
            if (value == null) {
                log.debug("channel closed")
                break
            }
            send(value)
        }
    }

//    private suspend fun selectAorB(a: ReceiveChannel<String>, b: ReceiveChannel<String>): String =
//        select {
//            a.onReceiveOrNull { value ->
//                if (value == null)
//                    "Channel 'a' is closed"
//                else
//                    "a -> '$value'"
//            }
//            b.onReceiveOrNull { value ->
//                if (value == null)
//                    "Channel 'b' is closed"
//                else
//                    "b -> '$value'"
//            }
//            log.debug("select")
//        }
}
