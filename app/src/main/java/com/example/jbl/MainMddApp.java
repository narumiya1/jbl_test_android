package com.example.jbl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mdd.aar.deviceid.AarDeviceId;
import com.mdd.aar.deviceid.DeviceEnvironment;
import com.mdd.aar.deviceid.http.responses.UnlockAarResponse;
import com.medicom.dudikov.mybanklibrary.halDriver;
import com.medicom.dudikov.mybanklibrary.nativeLib;
import com.medicom.dudikov.mybanklibrary.readerLib;
import com.medicom.organicdrv.OrganicDriver;

public class MainMddApp extends AppCompatActivity {
    Button button;
    private String debugToken;
    readerLib myReader;
    nativeLib nativeLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
         button = findViewById(R.id.btn_tes_koneksi);

        // aarDevice
//        AsyncTask.execute(()->{
//
//        };
        AarDeviceId aarDevice = new AarDeviceId(this);
        try {
            nativeLibrary = new nativeLib(this, halDriver.USE_WISEASY_ENGGINE);
        } catch (Exception e) {
            System.out.println(e);
        }

        // native library + Unlock
        try {
            debugToken = nativeLibrary.generateDebugCert();
            System.out.println(debugToken);

            UnlockAarResponse unlockAarResponse = aarDevice.unlockLibrary("19be782242b4fdde4eeccb8c42e92b2b", DeviceEnvironment.UNLOCK, aarDevice.getDeviceId(),
                    debugToken, "1");
            String debugResponse = unlockAarResponse.getData().getDebugResponse();
            myReader = new readerLib(this, true, halDriver.USE_WEPOY_ENGGINE);
            myReader.activateDebug(this, debugResponse);
        } catch (Exception e) {
            System.out.println("Native Library Exception");
            System.out.println(e);
        }

        // cek findCard btn
        button.setOnClickListener(view -> {
            try {
                byte[] uid = new byte[16];
                byte[] uidLen = new byte[1];
                int[] cardType = new int[1];
                cardType[0] = OrganicDriver.CARDTYPE_UNKNOWN;
                boolean result = myReader.findCard(5000, uid, uidLen, cardType);

                System.out.println( uid);
                System.out.println( cardType);

                Toast.makeText(MainMddApp.this, "UID "+uid+" result = " + result, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                System.out.println("Reader Lib");
                System.out.println(e);
            }

        });

    }
}