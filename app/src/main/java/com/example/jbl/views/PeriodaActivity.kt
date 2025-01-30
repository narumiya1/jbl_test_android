package com.example.jbl.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jbl.HomeActivity
import com.example.jbl.MainJblMdd
import com.example.jbl.R
import com.example.jbl.databinding.ActivityPeriodaBinding

class PeriodaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeriodaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeriodaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            startActivity(Intent(this, MainJblMdd::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}