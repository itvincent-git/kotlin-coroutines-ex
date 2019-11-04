package net.kotlin.coroutines.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import net.kotlin.coroutines.lib.launchAll

class LaunchAllActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_all)

        GlobalScope.launch(context = Dispatchers.Main) {
            GlobalScope.launchAll({ delay(100) }, { delay(1000) }, { delay(2000) }).joinAll()
            Toast.makeText(this@LaunchAllActivity, "all join done", Toast.LENGTH_SHORT).show()
        }
    }
}
