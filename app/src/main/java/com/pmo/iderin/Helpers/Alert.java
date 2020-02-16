package com.pmo.iderin.Helpers;

import android.content.Context;
import android.widget.Toast;

public class Alert {
    private Context context;

    public Alert(Context context){
        this.context = context;
    }

    public void toast(String message,int length){
         Toast.makeText(context,message,length).show();
    }
}
