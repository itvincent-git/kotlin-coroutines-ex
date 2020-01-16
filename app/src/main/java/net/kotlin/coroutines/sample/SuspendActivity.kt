package net.kotlin.coroutines.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_suspend.button0
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.slog.SLoggerFactory

private val log = SLoggerFactory.getLogger("SuspendActivity")

class SuspendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suspend)
        val userId = 1000L

        button0.setOnClickListener {
            log.debug("run in click starting")
            GlobalScope.launch(Dispatchers.Main) {
                log.debug("run in launch")
                val userInfo = suspendGetUserInfo(userId)
            }
            log.debug("run in click finishing")
        }
    }

    suspend fun suspendGetUserInfo(userId: Long): UserInfo {
        //可切换线程IO线程执行，原来执行的Main线程将空闲执行其它工作
        log.debug("run in suspendGetUserInfo starting")
        GlobalScope.launch(Dispatchers.Main) {
            log.debug("Main is available")
        }
        val userInfo = withContext(Dispatchers.IO) {
            delay(100)
            log.debug("run in withContext")
            getUserFromNetwork(userId)
        }
        log.debug("run in suspendGetUserInfo finishing")
        return userInfo
    }

    data class UserInfo(val userId: Long, val userName: String)

    private fun getUserFromNetwork(userId: Long): UserInfo {
        return UserInfo(1000L, "Peter")
    }
}
