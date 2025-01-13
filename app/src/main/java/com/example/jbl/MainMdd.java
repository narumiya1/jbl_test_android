package com.example.jbl;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jbl.databinding.ActivityMainBinding;
import com.medicom.dudikov.mybanklibrary.halDriver;
import com.medicom.dudikov.mybanklibrary.readerLib;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MainMdd extends AppCompatActivity {
   private  ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_main);

        try {
            readerLib myReader = new readerLib(getApplicationContext(), true, halDriver.USE_WEPOY_ENGGINE);
            myReader.activateDebug(getApplicationContext(), "certDebugRespons");
            System.out.println("myReader");
            System.out.println(myReader);

            runOnUiThread(() -> {
                binding.card.setText(myReader.getVersion());
                binding.balance.setText(myReader.getDeviceSN());
            });



            String uidString = "EF115922";
            byte[] uidBytes;
            // Menggunakan UTF-8 encoding
            uidBytes = uidString.getBytes(StandardCharsets.UTF_8);
            // Menggunakan encoding default platform
            // uidBytes = uidString.getBytes();

            String uidLen = "16";
            byte[] uidLenBytes;
            // Menggunakan UTF-8 encoding
            uidLenBytes = uidLen.getBytes(StandardCharsets.UTF_8);
            // Menggunakan encoding default platform
            // uidBytes = uidString.getBytes();
            System.out.println(Arrays.toString(uidBytes));
            int[] cardType = {255, 0, 1, 2};
            myReader.findCard(5000, uidBytes, uidLenBytes, cardType);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println("getsMessage");
            binding.card.setText(e.getMessage());

            System.out.println(msg);
        }


    }

}
