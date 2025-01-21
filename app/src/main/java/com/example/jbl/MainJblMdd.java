package com.example.jbl;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mdd.aar.deviceid.AarDeviceId;
import com.mdd.aar.deviceid.DeviceEnvironment;
import com.mdd.aar.deviceid.http.responses.UnlockAarResponse;
import com.medicom.dudikov.mybanklibrary.halDriver;
import com.medicom.dudikov.mybanklibrary.nativeLib;
import com.medicom.dudikov.mybanklibrary.readerLib;
import com.medicom.organicdrv.OrganicDriver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainJblMdd extends AppCompatActivity {

    private String debugToken;
    private AarDeviceId aarDevice;
    private int idxDriver = halDriver.USE_WISEASY_ENGGINE;
    private final String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhdXRoLXNlcnZpY2U6MS4wLjAiLCJzdWIiOnsibmFtZSI6IlRyYW5zSmFrYXJ0YSIsInVzZXJuYW1lIjoidGoiLCJhdXRoX2xldmVsIjoiTUVSQ0hBTlRfR1JPVVAiLCJtaWQiOiJUSiJ9LCJpYXQiOjE1ODkxMjc5NTksImV4cCI6MTc0NjkxMjcxOSwibmJmIjoxNTg5MTI3OTYxfQ.YmCfzXR3j2B9LDogPUKgF32OD4d3hNucrQeadM7tywu3JHB31xvlVaqfzmzBdBgm8Xn8s4u20_6tGQCQ9rTq62UGWIoMr2Tq_sUmcRoX61N_QfgCJVbamtmDh_lGcpwQIzm1782kJsMYQRLRCk7bYfTt6RCpbNJzwLJIrFDHNeAEhnNs7L3-v4PFmIf4hbjpmTqPlWZIMvJrBG8SUdTdsmlzehus--XGzU2l4y5CIj6m09qfxZluRP45KFlHLbPln4DIac-8wpKhDYx3gSiIuacCI4dWIc837GuC8yDyr3TmIz4VV7PsjEl_NmQ6tEof2CyBy-L3NJRqLluGRGtA0w";
    nativeLib nativeLibrary;
    Context context;
    private readerLib myReader;
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_jbl_mdd);

        tv1 = (TextView) findViewById(R.id.tv1);

        tv1.setOnClickListener(v -> {
//            AsyncTask.execute(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        getbalance();
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            });
            new Thread() {
                @Override
                public void run() {
                    try {
                        getbalance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();

        });

        AarDeviceId aarDevice = new AarDeviceId(this);

        AsyncTask.execute(() -> {

            try {
                aarDevice.init(accessToken, DeviceEnvironment.PROD);
                Log.d("aarDevice.init", "success");
            } catch (Exception e) {
                String msg = e.getMessage();
                Log.d("aarDevice.init", "Failed");
                e.printStackTrace();
            }

            try {
                nativeLibrary = new nativeLib(this, idxDriver);
                nativeLibrary.setRandom("0123456789AB0123456789AB");
                debugToken = nativeLibrary.generateDebugCert();
                Log.d("debugToken", debugToken);
                UnlockAarResponse unlockAarResponse = aarDevice.unlockLibrary("9ef471364bca478faf77f8cc0710b7c4",
                        DeviceEnvironment.UNLOCK, aarDevice.getDeviceId(), debugToken,
                        "1");
                String debugResponse = unlockAarResponse.getData().getDebugResponse();
                Log.d("debugResponse", debugResponse);
                myReader = new readerLib(this, true, halDriver.USE_WEPOY_ENGGINE);
                myReader.activateDebug(this, debugResponse);
            } catch (Exception e) {
                String msg = e.getMessage();
                e.printStackTrace();
                try {
                    nativeLibrary = new nativeLib(this, idxDriver);
                    nativeLibrary.setRandom("0123456789AB0123456789AB");
                    debugToken = nativeLibrary.generateDebugCert();
                    UnlockAarResponse unlockAarResponse = aarDevice.unlockLibrary("9ef471364bca478faf77f8cc0710b7c4",
                            DeviceEnvironment.UNLOCK, aarDevice.getDeviceId(), debugToken,
                            "1");
                    String debugResponse = unlockAarResponse.getData().getDebugResponse();
                    Log.d("debugResponse", debugResponse);
                    myReader = new readerLib(this, true, halDriver.USE_WEPOY_ENGGINE);
                    myReader.activateDebug(this, debugResponse);
                } catch (Exception ex) {
                    String msgs = e.getMessage();
                    e.printStackTrace();
                }
            }
        });
    }

    private void getbalance() throws Exception {
        byte[] uid = new byte[16];
        byte[] uidLen = new byte[1];
        int[] cardType = new int[1];
        cardType[0] = OrganicDriver.CARDTYPE_UNKNOWN;
        if (myReader.findCard(5000, uid, uidLen, cardType)) {
            final long start = System.currentTimeMillis();
            final int[] bankType = new int[1];
            final int[] balance = new int[1];
            final String[] cardNumber = new String[1];
            final int[] errorCode = new int[1];
            Date date = new Date();
            String StrDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(date);
            try {
                if (myReader.readerGetBalance(cardType[0], StrDate, bankType, balance, cardNumber, errorCode)) {
                    final long stop = System.currentTimeMillis();
                    System.out.println("bank type " +bankType[0]);
                    System.out.println("balance type " +balance[0]);
                    System.out.println("cardNumber type " +cardNumber[0]);

                    myReader.beep();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            myReader.readerClDisconnect();
        }
    }
}
