package com.example.jbl.tools;

import android.content.Context;
        import android.nfc.NfcAdapter;
        import android.nfc.Tag;
        import android.nfc.tech.IsoDep;
        import android.util.Log;

        import java.io.IOException;

public class CardTypeHandler {

    private Context context;
    private NfcAdapter nfcAdapter;

    public CardTypeHandler(Context context) {
        this.context = context;
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }

    // Callback ketika kartu di-tap
    public void onCardTapped(Tag tag) {
        if (tag == null) {
            Log.e("CardTapHandler", "Tag kosong atau tidak terbaca!");
            return;
        }

        // Pastikan kartu mendukung teknologi IsoDep (sering digunakan untuk e-Toll)
        IsoDep isoDep = IsoDep.get(tag);
        if (isoDep != null) {
            try {
                isoDep.connect();
                // Kirimkan perintah APDU untuk membaca data kartu
                byte[] apduCommand = buildReadCardCommand();
                byte[] response = isoDep.transceive(apduCommand);

                // Parsing data kartu
                parseCardData(response);

                isoDep.close();
            } catch (IOException e) {
                Log.e("CardTapHandler", "Gagal membaca kartu: " + e.getMessage());
            }
        } else {
            Log.e("CardTapHandler", "Kartu tidak mendukung teknologi IsoDep!");
        }
    }

    // Contoh pembuatan command APDU (disesuaikan dengan kartu e-Toll)
    private byte[] buildReadCardCommand() {
        return new byte[]{
                (byte) 0x00, // CLA
                (byte) 0xA4, // INS (Select command)
                (byte) 0x04, // P1
                (byte) 0x00, // P2
                (byte) 0x07, // Lc
                (byte) 0xA0, 0x00, 0x00, 0x00, 0x04, 0x10, 0x10, // AID of e-Toll
                (byte) 0x00  // Le
        };
    }

    // Parsing data kartu
    private void parseCardData(byte[] response) {
        if (response == null || response.length == 0) {
            Log.e("CardTapHandler", "Respons kosong!");
            return;
        }

        // Contoh parsing data (disesuaikan dengan format kartu)
        String cardType = new String(response); // Hanya contoh
        Log.d("CardTapHandler", "Jenis Kartu: " + cardType);

        // Implementasi lain seperti membaca saldo, golongan, dll.
    }

    public boolean isNfcSupported() {
        return nfcAdapter != null && nfcAdapter.isEnabled();
    }
}
