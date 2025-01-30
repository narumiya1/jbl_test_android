package com.example.jbl.tools;

import android.util.Log;

import com.medicom.dudikov.mybanklibrary.mandiriLib;
import com.medicom.dudikov.mybanklibrary.readerLib;
import com.medicom.organicdrv.OrganicDriver;
import com.medicom.organicdrv.utilsLib;

public class MandiriEnableHelpers {
    private readerLib myReader;
    private mandiriLib mandiriLibrary;
    private int[] errorCode;


    public MandiriEnableHelpers(readerLib myReader, mandiriLib mandiriLibs, int[] errorCode) {
        this.myReader = myReader;
        this.mandiriLibrary = mandiriLibs;
        this.errorCode = errorCode;
    }

    public boolean configureMandiri() {
        if (myReader.mandiriEnabled()) {
            // Whatsapp
            //  String samPin = "F23FDBA3136C4A39";
            // String tid = "09080400";
            //  String mid = "0005";

            // String samPin = "DED456D4EA4DD92C";

            // DEVICE
            String samPin = "F23FDBA3136C4A39";
//            String samPin = "0D706E83BA9FF9";
            String mid = "0009";
            String tid = "06051200";

            byte[] bpin = utilsLib.HexStringToByteArray(samPin);
            byte[] bmid = utilsLib.HexStringToByteArray(mid);
            byte[] btid = utilsLib.HexStringToByteArray(tid);

            myReader.mandiriDeductSetConfig(bpin, bmid, btid);
            myReader.readerMandiriSetSamSlot(OrganicDriver.ORGANIC_SAM1);
            myReader.readerMandiriEnableFastDeduct();

            int[] errorCodes = new int[1];
            if (mandiriLibrary.mandiriLogin(bpin, bmid, btid, errorCodes)) {
                Log.e("validateMandiri", "true : " + Integer.toHexString(errorCode[0]));
                return true;
            } else {
                Log.e("validateMandiri", "false : " + Integer.toHexString(errorCode[0]));
                return false;
            }
        }
        return false;
    }
}