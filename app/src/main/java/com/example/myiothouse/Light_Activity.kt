package com.example.myiothouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony.Mms.Part.FILENAME
import android.util.Log
import kotlinx.android.synthetic.main.activity_light_.*

class Light_Activity : AppCompatActivity() {

    val ON = true
    val OFF = false

//    val change_doorlock = Intent(this, Door_lock_Activity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_)


        var pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        var editor = pref.edit()

        on_offBtn.isChecked = pref.getBoolean("state", false)


        on_offBtn.setOnClickListener {view->
            if (on_offBtn.isChecked) {
                publish("test", "off")
                editor.putBoolean("state", ON)
                editor.commit()
            }else{
                publish("test", "on")
                editor.putBoolean("state", OFF)
                editor.commit()
            }
        }

        change_activity_doorlock.setOnClickListener {
            finish()
        }

    }

}
