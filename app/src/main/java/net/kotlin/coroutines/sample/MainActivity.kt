package net.kotlin.coroutines.sample

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import net.kotlin.coroutines.lib.tryCatch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val activities = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES).activities/*.filter { !it.name.contains("MainActivity") }*/
        rv_activities.layoutManager = LinearLayoutManager(this)
        rv_activities.adapter = object: RecyclerView.Adapter<ActivitiesViewHolder?>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitiesViewHolder {
                return ActivitiesViewHolder(TextView(parent.context))
            }

            override fun getItemCount(): Int {
                return activities.size
            }

            override fun onBindViewHolder(holder: ActivitiesViewHolder, position: Int) {
                holder.textView.text = activities[position].name
                holder.textView.setOnClickListener {
                    startActivity(Intent(this@MainActivity, tryCatch { Class.forName(activities[position].name) }))
                }
            }
        }


    }

    class ActivitiesViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView) {
    }
}
