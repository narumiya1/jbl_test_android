package com.example.jbl

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jbl.databinding.ActivityHomeBinding
import com.example.jbl.views.PeriodaActivity
import com.example.jbl.views.PeriodaActivityNew
import com.example.jbl.views.SettingActivity
import com.example.jbl.views.SystemAdminActivite

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Cek dan minta izin
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                showPermissionDialog()
            } else {
//                navigateToMainActivity()
//                Toast.
            }
        } else {
            navigateToMainActivity() // Izin tidak diperlukan pada versi Android sebelum M
        }
        binding.setting.setOnClickListener{
            startActivity(Intent(this, SettingActivity::class.java))
            finish()
        }
        binding.systemAdmin.setOnClickListener{
            startActivity(Intent(this, SystemAdminActivite::class.java))
            finish()
        }
        binding.perioda.setOnClickListener{
            startActivity(Intent(this, PeriodaActivityNew::class.java))
            finish()
        }
    }
    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Izin Diperlukan")
            .setMessage("Aplikasi ini membutuhkan izin READ_PHONE_STATE untuk melanjutkan.")
            .setPositiveButton("Izinkan") { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_PHONE_STATE),
                    PERMISSION_REQUEST_CODE
                )
            }
            .setNegativeButton("Tolak") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Izin diperlukan untuk melanjutkan.", Toast.LENGTH_SHORT).show()
            }
            .create()
            .show()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, navigasi ke MainActivity
                navigateToMainActivity()
            } else {
                // Izin ditolak
                Toast.makeText(this, "Izin ditolak. Tidak dapat melanjutkan.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun navigateToMainActivity() {
        val intent = Intent(this, PeriodaActivity::class.java)
        startActivity(intent)
        finish()
    }
}