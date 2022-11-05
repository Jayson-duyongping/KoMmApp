package com.jayson.komm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.jayson.komm.girls.GirlsActivity
import com.jayson.komm.home.HomeActivity
import com.jayson.komm.me.MeActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_home)?.setOnClickListener {
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.btn_girls)?.setOnClickListener {
            intent = Intent(this, GirlsActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.btn_me)?.setOnClickListener {
            intent = Intent(this, MeActivity::class.java)
            startActivity(intent)
        }
    }
}