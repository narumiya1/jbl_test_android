package com.example.jbl;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jbl.tools.MandiriEnableHelpers;
import com.example.jbl.tools.ProgressUtils;
import com.mdd.aar.deviceid.AarDeviceId;
import com.mdd.aar.deviceid.DeviceEnvironment;
import com.mdd.aar.deviceid.http.responses.UnlockAarResponse;
import com.medicom.dudikov.mybanklibrary.halDriver;
import com.medicom.dudikov.mybanklibrary.mandiriLib;
import com.medicom.dudikov.mybanklibrary.nativeLib;
import com.medicom.dudikov.mybanklibrary.readerLib;
import com.medicom.organicdrv.OrganicDriver;
import com.medicom.organicdrv.utilsLib;

import java.math.BigInteger;
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
    mandiriLib mandiriLibrary;

    private TextView tv1;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_jbl_mdd);

        tv1 = (TextView) findViewById(R.id.tv1);
        progressBar = findViewById(R.id.progressBar);


        AarDeviceId aarDevice = new AarDeviceId(this);
        ProgressUtils.showProgressDialog(MainJblMdd.this);

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
            // Mandiri Library Enable Test di AsyncTask
            try {
                mandiriLibrary = new mandiriLib();
                final int[] errorCode = new int[1];

                MandiriEnableHelpers mandiriEnableHelpers =  new MandiriEnableHelpers(myReader, mandiriLibrary, errorCode);
                boolean success =  mandiriEnableHelpers.configureMandiri();
                if (success) {
                    // Tindakan jika konfigurasi berhasil
                    Log.d("mandiriEnableHelpers ","success");
                } else {
                    // Tindakan jika konfigurasi gagal
                    Log.d("mandiriEnableHelpers ","fail ");

                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            ProgressUtils.closeProgressDialog();
        });
        // Test deduct
        tv1.setOnClickListener(v -> {
//            ProgressUtils.showProgressDialog(MainJblMdd.this);

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

    }

    private void getbalance() throws Exception {
        byte[] uid = new byte[16];
        byte[] uidLen = new byte[1];
        int[] cardType = new int[1];
        cardType[0] = OrganicDriver.CARDTYPE_UNKNOWN;
        if (myReader.findCard(5000, uid, uidLen, cardType)) {
            runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
            // validateMandiri & balance type
            final long start = System.currentTimeMillis();
            final int[] bankType = new int[1];
            final int[] balance = new int[1];
            final String[] cardNumber = new String[1];
            final int[] errorCode = new int[1];
            Date date = new Date();
            String StrDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(date);
            try {
                //Get Balance
                if (myReader.readerGetBalance(cardType[0], StrDate, bankType, balance, cardNumber, errorCode)) {
                    final long stop = System.currentTimeMillis();
                    System.out.println("StrDate type " + StrDate);
                    System.out.println("bank type " + bankType[0]);
                    System.out.println("balance type " + balance[0]);
                    System.out.println("cardNumber type " + cardNumber[0]);


                    // Mandiri Enabled
                    //  kode ini dipindah ke Tools MandiriEnableHelpers
                    /* if (myReader.mandiriEnabled()){
//                        String samPin = "0123456789ABCDEF";
                        String samPin = "DED456D4EA4DD92C";
//                        String samPin = "0D706E800FA5B1";
//                        String samPin = "0D706E83BA9FF9";

                        byte[] bpin = utilsLib.HexStringToByteArray(samPin);
                        String mid = "0001";
                        byte[] bmid = utilsLib.HexStringToByteArray(mid);
                        String tid = "01234567";
                        byte[] btid = utilsLib.HexStringToByteArray(tid);
                        myReader.mandiriDeductSetConfig(bpin, bmid, btid);
                        myReader.readerMandiriSetSamSlot(OrganicDriver.ORGANIC_SAM1);
                        myReader.readerMandiriEnableFastDeduct();
                        int[] errorCodes = new int[1];
                        if(mandiriLibrary.mandiriLogin(bpin,bmid,btid, errorCodes)){
                            Log.e("validateMandiri", "true : " +Integer.toHexString(errorCode[0]));
                        }else{
                            Log.e("validateMandiri", "false : " +Integer.toHexString(errorCode[0]));
                        }
                    } */

                    /**
                     mandiriLibrary = new mandiriLib();
                     MandiriEnableHelpers mandiriEnableHelpers = new MandiriEnableHelpers(myReader, mandiriLibrary, errorCode);
                    boolean success = mandiriEnableHelpers.configureMandiri();
                    if (success) {
                        Log.d("status success", "suckess");
                    } else {
                        Log.d("status success", "faileds");

                    } **/

                    // Reader Deductv

                    final int[] bankT = new int[bankType[0]];
                    final int[] balanceT = new int[balance[0]];

                    // Report New
                    String[] reportsCode = new String[1];
                    // Report new
//                    final String reportValuess = "603298409912990400060D706E83B851F52001020001200A0000004E170000020821122239000000E20000831A3EF5";
                    // Report old
                    final String reportValuess = "603298280239830300030D706E8648C8C90123456701200A00000010A70000020821110756070000002502031D3795";

                    reportsCode[0] = reportValuess; // Memasukkan nilai string ke dalam array

                    // Error
                    final int errorValuess = 9000; // Nilai angka dalam bentuk string
                    final int[] errorNew = new int[1]; // Array dengan ukuran 1
                    errorNew[0] = errorValuess; // Memasukkan nilai string ke dalam array

                    // Card Number
                    final String cardNumberValue = cardNumber[0].toString(); // Nilai angka dalam bentuk string
                    final String[] cardNumberNew = new String[1]; // Array dengan ukuran 1
                    cardNumberNew[0] = cardNumberValue; // Memasukkan nilai string ke dalam array

                    System.out.println("Card Number [0]: " + cardNumber[0]);

                   /**  String[] cardData = new String[1]; // Array dengan ukuran 1
                    long cardNumberLong = Long.parseLong(cardData[0]);
                    BigInteger cardNumberBigInt = new BigInteger(cardData[0]);
                    System.out.println("cardNumberLong " + cardNumberLong);
                    System.out.println("cardNumberBigInt " + cardNumberBigInt);

                    cardData[0] = String.valueOf(cardNumberLong); // Memasukkan nilai ke dalam array
                    System.out.println("Card Data: " + cardData[0]);

                    try {
                        BigInteger cardNumberAsBigInt = new BigInteger(cardData[0]);
                        System.out.println("Card Number as BigInteger: " + cardNumberAsBigInt);
                    } catch (NumberFormatException e) {
                        System.err.println("Error: Invalid number format for card data.");
                    } **/
                    // Deduct
                    boolean deduckt = myReader.readerDeduct(cardType[0], StrDate, 2, bankT, balanceT, cardNumberNew, reportsCode, errorNew);
                    Log.d("deduckt result", " " + deduckt);
                    Log.d("reportsCode result", " " + reportsCode);
                    // Log 17 - 19 = BCA TEST
                    // Report Mandiri
                    boolean reportMandiri =  myReader.getReportMandiri(reportsCode, balanceT,errorNew);
                    Log.d("reportMandiri result", " " + reportMandiri);

                    myReader.beep();
                    runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            myReader.readerClDisconnect();
        }
    }
}
