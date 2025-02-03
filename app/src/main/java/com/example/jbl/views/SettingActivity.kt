package com.example.jbl.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import com.example.jbl.HomeActivity
import com.example.jbl.R

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        // Set Toolbar sebagai ActionBar
//        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        toolbar.title = "Settings"
//        toolbar.setNavigationIcon(R.drawable.baseline_next_week_24)
//        toolbar.setNavigationOnClickListener {
//            val intent = Intent(this, SystemAdminActivite::class.java)
//            startActivity(intent)
//            finish() // Menutup activity saat tombol kembali diklik
//        }


    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, PeriodaActivityNew::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
    // Inflate menu di AppBar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    // Handle klik pada item menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_next -> {
                // Intent ke tampilan berikutnya
                val intent = Intent(this, SystemAdminActivite::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}