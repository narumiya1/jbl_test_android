package com.example.jbl;

import android.util.Log;

import com.medicom.dudikov.mybanklibrary.readerLib;
import com.medicom.organicdrv.OrganicDriver;
import com.medicom.organicdrv.utilsLib;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Prepaid implements Runnable {
  private static final String TAG = "Prepaid";
//  private static final int PERMISSION_READ_STATE = 100;
//  private static final int PERMISSION_EXTERNAL = 101;
  private volatile boolean isRunning = true;
  //private final Context context;
  private final readerLib reader;
  private final MyCallback callback;
  private int lastId = 0;   // Akan diisi dengan CardId bila ada tap kartu
  private int CardType = 0; // Jika tidak ada kartu akan bernilai 0

  public Prepaid(MyCallback _callback, readerLib _reader) {
    //this.context = _context;
    this.reader = _reader;
    this.callback = _callback;
  }

  @Override
  public void run() {
    Log.d(TAG, "Starting prepaid thread");
    while (isRunning){
      try {
        getCard();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void stop() {
    isRunning = false;
  }

  private boolean getCard() throws Exception {
    byte[] uid = new byte[16];
    byte[] uidLen = new byte[1];
    int[] cardType = new int[1];
    cardType[0] = OrganicDriver.CARDTYPE_UNKNOWN;
    if (reader.findCard(1000, uid, uidLen, cardType)) {
      String hex = utilsLib.ByteArrayToHexString(uid,0,uidLen[0]);
      BigInteger bigInt = new BigInteger(hex, 16);
      if (lastId != bigInt.intValue()) lastId = bigInt.intValue();
      if (callback != null) callback.onInserted(lastId, cardType[0]);
      reader.readerClDisconnect();
      return true;
    }
    else {
      if (callback != null) callback.onRemoved(lastId);
      CardType = 0;
    }
    return false;
  }

  public boolean getBalance() {
    if (CardType == 0) return false;
    final int[] bankType = new int[1];
    final int[] balance = new int[1];
    final String[] cardNumber = new String[1];
    final int[] errorCode = new int[1];
    //Date date = new Date();
    String StrDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
    try {
      if (reader.readerGetBalance(CardType, StrDate, bankType, balance, cardNumber, errorCode)) {
        //final long stop = System.currentTimeMillis();
        //txt1.setText(utilsLib.ByteArrayToHexString(uid,0,uidLen[0]));

        String bankName;
        switch (bankType[0])
        {
          case 304:
          case 48: bankName = "Mandiri"; break;
          case 20: bankName = "BRI"; break;
          case 21: bankName = "BNI"; break;
          case 53: bankName = "BCA"; break;
          default: bankName = "UNKNOWN"; break;
        }

        //txt2.setText(bankName + ", " + cardNumber[0]);
        //txt3.setText("CardType "+String.valueOf(cardType[0])+ ", BankType "+String.valueOf(bankType[0])+", Rp "+String.valueOf(balance[0]));
        reader.beep();
        return true;
      }
    } catch (Exception e) {
      Log.d(TAG, Objects.requireNonNull(e.getMessage()));
    }
    return false;
  }

  public interface MyCallback {
    void onSuccess(String result);
    void onError(Exception e);
    void onInserted(int cardId, int cardType);
    void onRemoved(int cardId);
  }
}
