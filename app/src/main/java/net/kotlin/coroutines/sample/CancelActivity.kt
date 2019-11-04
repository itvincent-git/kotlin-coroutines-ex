package net.kotlin.coroutines.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cancel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import net.kotlin.coroutines.lib.awaitOrNull
import java.util.concurrent.TimeUnit

class CancelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel)

        val deferred = GlobalScope.async {
            delay(5000)
            "This is the response"
        }

        cancel_btn.setOnClickListener {
            deferred.cancel()
        }

        GlobalScope.async(context = Dispatchers.Main) {
            LogUtil.debug("before await")
            val response = deferred.awaitOrNull(timeout = 3, unit = TimeUnit.SECONDS) { }
            LogUtil.debug("response $response")
            Toast.makeText(this@CancelActivity, "response: $response", Toast.LENGTH_SHORT).show()
        }
    }
}
