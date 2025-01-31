package com.example.jbl.tools;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnecHandlers {

    Context context;

    public BluetoothConnecHandlers(Context context) {
        this.context = context;
    }

    public void detectBluetoothPrinter() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Log.e("Bluetooth", "Bluetooth tidak tersedia di perangkat ini");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Log.e("Bluetooth", "Bluetooth belum diaktifkan");
            return;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Log.d("Bluetooth", "Perangkat ditemukan: " + device.getName() + " - " + device.getAddress());

                // Cek apakah ini adalah printer Wiseasy
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (device.getName().contains("Wiseasy") || device.getBluetoothClass().getDeviceClass() == 1664) {
                    Log.d("BluetoothPrinter", "Printer Wiseasy ditemukan: " + device.getName());
                }
            }
        } else {
            Log.e("Bluetooth", "Tidak ada perangkat Bluetooth yang dipasangkan");
        }
    }

    public void scanBluetoothPrinters(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            Log.e("Bluetooth", "Bluetooth belum diaktifkan");
            return;
        }

        // Buat BroadcastReceiver untuk menangkap hasil scan
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Log.d("BluetoothScan", "Ditemukan: " + device.getName() + " - " + device.getAddress());

                    // Cek apakah ini printer Wiseasy
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    if (device.getName() != null && device.getName().contains("Wiseasy")) {
                        Log.d("BluetoothPrinter", "Printer Wiseasy ditemukan: " + device.getName());
                    }
                }
            }
        };

        // Mendaftarkan BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(receiver, filter);

        // Mulai scan perangkat Bluetooth
        bluetoothAdapter.startDiscovery();
    }


    public void connectAndPrint(String printerMacAddress) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice printerDevice = bluetoothAdapter.getRemoteDevice(printerMacAddress);

        try {
            // UUID untuk komunikasi Serial Port Profile (SPP)
            UUID sppUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            BluetoothSocket bluetoothSocket = printerDevice.createRfcommSocketToServiceRecord(sppUUID);

            bluetoothSocket.connect(); // Coba koneksi ke printer

            // Kirim perintah print (contoh: cetak teks)
            OutputStream outputStream = bluetoothSocket.getOutputStream();
            String printData = "Halo, ini tes cetak Wiseasy!\n\n";
            outputStream.write(printData.getBytes());

            outputStream.flush();
            outputStream.close();
            bluetoothSocket.close();

            Log.d("BluetoothPrint", "Print berhasil dikirim ke Wiseasy!");
        } catch (Exception e) {
            Log.e("BluetoothPrint", "Gagal terhubung ke printer", e);
        }
    }
}
