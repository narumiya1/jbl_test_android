package com.example.jbl.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.jbl.HomeActivity;
import com.example.jbl.MainJblMdd;
import com.example.jbl.R;
import com.example.jbl.databinding.ActivityMainBinding;
import com.example.jbl.databinding.ActivityPeriodaNewBinding;

import java.util.ArrayList;
import java.util.List;

public class PeriodaActivityNew extends AppCompatActivity {
    private ActivityPeriodaNewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPeriodaNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        List<String> companyList = new ArrayList<>();
        companyList.add("SHIFT-1");
        companyList.add("SHIFT-2");
        companyList.add("SHIFT-3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, companyList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.spinner_items);
        binding.shift.setAdapter(adapter);

        // Set default selection to the first item in the array
        binding.shift.setSelection(0);
        // Set listener for Spinner item selection
        // Set listener untuk menangkap item yang dipilih
        binding.shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Ambil item yang dipilih
                String selectedItem = companyList.get(position);
                // Lakukan sesuatu dengan item yang dipilih
                System.out.println("Selected: " + selectedItem + " positioin" +position );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ketika tidak ada item yang dipilih
            }
        });
        // To Do Perioda OpenPerioda
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent
                startActivity(new Intent(PeriodaActivityNew.this, MainJblMdd.class));


            }
        });

        binding.buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PeriodaActivityNew.this, SettingActivity.class));

            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PeriodaActivityNew.this, HomeActivity.class));
        finish();

    }

}