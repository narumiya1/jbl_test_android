package com.example.jbl.tools;


import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressUtils {
    private static ProgressDialog progressDialog;


    public static void showProgressDialog(Activity activity){

        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }

        if(progressDialog == null ) {
            progressDialog = new ProgressDialog(activity);
        }
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        if(!activity.isFinishing())
            progressDialog.show();
    }

    public static void  closeProgressDialog(){
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}