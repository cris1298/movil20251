package com.example.helloandroid;

import android.util.Log;
import android.view.View;

public class ShowMessageClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Log.i("MAIN_APP", "Clic en boton!!!!!!");
    }
}
