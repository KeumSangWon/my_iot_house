package com.example.myiothouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_door_lock_.*
import kotlinx.android.synthetic.main.activity_main.*


class Door_lock_Activity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_door_lock_)

        openBtn.setOnClickListener {
//            publish("test", "open")
            Log.i("testNo1", "on")
        }

        change_activity_light.setOnClickListener {
            val change_light = Intent(this, Light_Activity::class.java)
            startActivity(change_light)
        }
    }

}
