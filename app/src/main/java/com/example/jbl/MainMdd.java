package com.example.jbl;

import static sdk4.wangpos.libemvbinder.utils.HEX.bytesToHex;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jbl.databinding.ActivityMainBinding;
import com.example.jbl.retrofit.client.APIClient;
import com.example.jbl.retrofit.client.ApiInterface;
import com.example.jbl.tools.CardTypeHandler;
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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMdd extends AppCompatActivity {
    private ActivityMainBinding binding;
    ApiInterface apiInterface;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private CardTypeHandler cardTapHandler;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    ProgressDialog progress;
    // Timer 2 Detik
    private static final long START_TIME_IN_MILLIS = 10000;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private String debugToken;
    readerLib myReader;
    nativeLib nativeLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // refference
        /*
        https://www.digitalocean.com/community/tutorials/retrofit-android-example-tutorial
        https://daily.dev/blog/retrofit-tutorial-for-android-beginners
        */
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       /* cardTapHandler = new CardTypeHandler(this);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (!cardTapHandler.isNfcSupported()) {
            Toast.makeText(this, "NFC tidak didukung di perangkat ini", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            Toast.makeText(this, "NFC  didukung di perangkat ini", Toast.LENGTH_SHORT).show();

        } */

        /*pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT
        );
        apiInterface = APIClient.getClient().create(ApiInterface.class); */

        /**
         GET List Users
         **/
        // Api Example Reqres.in
        /* Call<ResponseUser> call2 = apiInterface.doGetUserList("2");
        call2.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {

                ResponseUser userList = response.body();
                Integer text = userList.getPage();
                Integer total = userList.getTotal();
                Integer totalPages = userList.getTotalPages();
                List<DataItem> datumList = userList.getData();
                Toast.makeText(getApplicationContext(), text + " page\n" + total + " total\n" + totalPages + " totalPages\n", Toast.LENGTH_SHORT).show();
                Log.d("TAG datumList",datumList +"");

                for (DataItem datum : datumList) {
                    Toast.makeText(getApplicationContext(), "id : " + datum.getId() + " name: " + datum.getFirstName() + " " + datum.getLastName() + " avatar: " + datum.getAvatar(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                call.cancel();
            }
        });

        Call<ResponseUser> call = apiInterface.doGetListResources();
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                Log.d("TAG Response",response.code()+"");

                String displayResponse = "";

                ResponseUser resource = response.body();
                Integer text = resource.getPage();
                Integer total = resource.getTotal();
                Integer totalPages = resource.getTotalPages();
                List<DataItem> datumList = resource.getData();

                displayResponse += text + " Page\n" + total + " Total\n" + totalPages + " Total Pages\n";

                for (DataItem datum : datumList) {
                    displayResponse += datum.getId() + " " + datum.getEmail() + " " + datum.getFirstName() + " " + datum.getLastName() + "\n";
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                call.cancel();
            }
        });
        */



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
            }
        }


        binding.btnBukaFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homeFragment = new HomeFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, homeFragment);
                fragmentTransaction.commit();
            }
        });



        AarDeviceId aarDevice = new AarDeviceId(getApplicationContext());
        try {
            nativeLibrary = new nativeLib(this, halDriver.USE_WISEASY_ENGGINE);
        } catch (Exception e) {
            System.out.println(e);
        }

        // native library
        try {
            // nativr Lib
            debugToken = nativeLibrary.generateDebugCert();
            System.out.println(debugToken);

            // unlock
            UnlockAarResponse unlockAarResponse = aarDevice.unlockLibrary("19be782242b4fdde4eeccb8c42e92b2b", DeviceEnvironment.UNLOCK, aarDevice.getDeviceId(),
                    debugToken, "1");
            String debugResponse = unlockAarResponse.getData().getDebugResponse();
            myReader = new readerLib(this, true, halDriver.USE_WEPOY_ENGGINE);
            myReader.activateDebug(this, debugResponse);


        } catch (Exception e) {
            System.out.println("Native Library Exception");

            System.out.println(e);

        }
        binding.btnTesKoneksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    byte[] uid = new byte[16];
                    byte[] uidLen = new byte[1];
                    int[] cardType = new int[1];
                    cardType[0] = OrganicDriver.CARDTYPE_UNKNOWN;
                    boolean result = myReader.findCard(5000, uid, uidLen, cardType);

                    System.out.println( uid);
                    System.out.println( cardType);

                    Toast.makeText(MainMdd.this, "UID "+uid+" result = " + result, Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    System.out.println("Reader Lib");
                    System.out.println(e);
                }

            }
        });

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                binding.timer.setText("Seconds remaining: " + seconds);
                Log.d("TAG Start Timer ", seconds + "");

            }

            @Override
            public void onFinish() {
                binding.timer.setText("Done!");
                mTimerRunning = false;
            }
        }.start();
        mTimerRunning = true;
    }

    private void pauseTimer() {
        Log.d("TAG", "pauseTimer");

        countDownTimer.cancel();
        mTimerRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Mulai kembali timer jika activity di-resume
//        if (mTimerRunning) {
//            startTimer();
//        }
        // Intent untuk mendeteksi NFC
      /*  PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        String[][] techLists = new String[][]{
                new String[]{IsoDep.class.getName()},
        };

        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, techLists);
        } */
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Timer
//        pauseTimer();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        /* if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            cardTapHandler.onCardTapped(tag);
        } */
     /*   if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if (tag != null) {
                Log.d("NFC", "Tapping");
//                progressBar.setVisibility(View.VISIBLE);
//                progress.setCancelable(false);
                handleCard(tag);
//                pauseTimer();
//                startTimer();

            }
        }
    */
    }

    // Handle Card
    private void handleCard(Tag tag) {

        String[] techList = tag.getTechList();
        for (String tech : techList) {
            Log.d("NFC", "Technology found: " + tech);
        }
        IsoDep isoDep = IsoDep.get(tag);
        if (isoDep == null) {
            Log.e("NFC", "Kartu tidak mendukung teknologi IsoDep.");
            return;
        }

        try {
            isoDep.connect();

            // APDU Command untuk membaca AID kartu Flazz atau Jaklingko
           /* byte[] selectAidCommand = new byte[]{
                    (byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00,
                    (byte) 0x07, // Panjang AID
                    (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x60, (byte) 0x00, // AID BCA Flazz
                    (byte) 0x00
            };

            byte[] response = isoDep.transceive(selectAidCommand);

            if (isSuccess(response)) {
                Log.d("NFC", "AID Terpilih: " + bytesToHex(response));

                // Membaca saldo
                byte[] readBalanceCommand = new byte[]{
                        (byte) 0x00, (byte) 0xB2, (byte) 0x01, (byte) 0x0C, (byte) 0x00
                };

                byte[] balanceResponse = isoDep.transceive(readBalanceCommand);
                if (isSuccess(balanceResponse)) {
                    String balance = parseBalance(balanceResponse);
                    Log.d("NFC", "Saldo: " + balance);
                } else {
                    Log.e("NFC", "Gagal membaca saldo!");
                }
            } else {
                Log.e("NFC", "Gagal memilih AID!");
            } */
            byte[] selectAidCommand = new byte[]{
                    (byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00, // CLA, INS, P1, P2
                    (byte) 0x07,                                       // Panjang AID
                    (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, // AID BCA Flazz
                    (byte) 0x04, (byte) 0x60, (byte) 0x00,
                    (byte) 0x00                                        // Le (optional)
            };

            byte[] response = isoDep.transceive(selectAidCommand);
            Log.d("NFC", "Response AID: " + bytesToHex(response));
            String byteHex = bytesToHex(response);
            // Periksa apakah pemilihan AID berhasil
//            if (!isSuccess(response)) {
//                Log.e("NFC", "Gagal memilih AID.");
//                return;
//            }
            // Perintah untuk memeriksa saldo
           /* byte[] readBalanceCommand = new byte[]{
                    (byte) 0x80, // CLA (Class byte)
                    (byte) 0x5C, // INS (Instruction byte)
                    (byte) 0x00, // P1
                    (byte) 0x02, // P2
                    (byte) 0x04  // Le (Expected length of response)
            }; */
            // Jika AID berhasil dipilih, kirim perintah untuk membaca saldo
            byte[] readBalanceCommand = new byte[]{
                    (byte) 0x80, (byte) 0x5C, (byte) 0x00, (byte) 0x02, (byte) 0x04 // Perintah untuk membaca saldo
            };
            byte[] balanceResponse = isoDep.transceive(readBalanceCommand);
            Log.d("NFC", "Response Balance: " + bytesToHex(balanceResponse));
            String bytebalanceResponse = bytesToHex(balanceResponse);

            if (isSuccessz(balanceResponse)) {
                // Konversi saldo dari response
                int balance = parseBalancez(balanceResponse);
                Log.d("NFC", "Saldo Rp: " + balance);
            } else {
                Log.e("NFC", "Gagal membaca saldo. Respon: " + bytesToHex(balanceResponse));
            }
//            int balance = parseBalance(balanceResponse);
//            Log.d("NFC", "Saldo: " + balance);

            binding.card.setText(byteHex);
            binding.balance.setText(bytebalanceResponse);
//            ProgressUtils.closeProgressDialog();
//            progress.hide();
//            progressBar.setVisibility(View.GONE);

            isoDep.close();
//            isoDep.close();
        } catch (IOException e) {
            Log.e("NFC", "Kesalahan saat membaca kartu: " + e.getMessage());
        }
    }

    private boolean isSuccess(byte[] response) {
        return response != null && response.length >= 2 &&
                response[response.length - 2] == (byte) 0x90 &&
                response[response.length - 1] == (byte) 0x00;
    }

    public static boolean isSuccessz(byte[] response) {
        // Ganti dengan logika pengecekan kesuksesan berdasarkan respon dari kartu
        // Contoh: Periksa byte pertama atau kedua dari response untuk status sukses
        return response != null && response.length >= 2 &&
                response[response.length - 2] == (byte) 0x90 &&
                response[response.length - 1] == (byte) 0x00;
    }

    private int parseBalance(byte[] response) {
        if (response == null || response.length < 4) {
            return 0;
        }

        // Contoh parsing saldo dari 4 byte pertama
        return ((response[0] & 0xFF) << 24) |
                ((response[1] & 0xFF) << 16) |
                ((response[2] & 0xFF) << 8) |
                (response[3] & 0xFF);
    }

    public static int parseBalancez(byte[] response) {
        if (response == null || response.length < 4) {
            Log.e("NFC", "Respons saldo tidak valid.");
            return 0;
        }

        // Parsing saldo dari 2 atau 4 byte pertama (tergantung format kartu)
        // Misalnya, kartu menggunakan 2 byte untuk saldo dalam satuan sen
        int saldo = ((response[0] & 0xFF) << 8) | (response[1] & 0xFF);

        // Ubah dari sen ke Rupiah jika diperlukan (misalnya, saldo dalam satuan sen)
        return saldo / 100;
    }
    /*private String parseBalance(byte[] response) {
        int balance = ((response[1] & 0xFF) << 8) | (response[2] & 0xFF);
        return String.valueOf(balance / 100.0); // Konversi ke format desimal
    }*/

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private final static int ALL_PERMISSIONS_RESULT = 101;
    private ArrayList<String> permissionsToRequest;
    private final ArrayList<String> permissionsRejected = new ArrayList();

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            break;
                        }
                    }

                }

                break;
        }

    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getApplicationContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
