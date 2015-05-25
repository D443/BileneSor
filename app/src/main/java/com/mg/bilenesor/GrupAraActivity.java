package com.mg.bilenesor;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class GrupAraActivity extends ActionBarActivity implements View.OnClickListener {

    Button ara;
    EditText grup_kodu;

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_grup_ara:
                if (isOnline()) {
                    String grup_kod = grup_kodu.getText().toString();
                    if (grup_kod.matches("")) {

                        Toast.makeText(GrupAraActivity.this, "Lütfen Grup Kodu Giriniz."+grup_kod, Toast.LENGTH_LONG).show();
                    }
                 else {

                      Intent calisanMenu = new Intent(GrupAraActivity.this, GrupAraGoruntuleActivity.class);
                      calisanMenu.putExtra("grup_kodu",grup_kod);
                      startActivity(calisanMenu);
                      finish();

                }
        }
                else {
                    Toast.makeText(GrupAraActivity.this, "Lütfen İnternet Bağlantınızı Kontrol Ediniz.", Toast.LENGTH_LONG) .show();} break; default: break;
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grup_ara);
        ara = (Button) findViewById(R.id.btn_grup_ara);
        grup_kodu = (EditText) findViewById(R.id.txt_grup_kodu);
        ara.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grup_ara, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case R.id.gruplarim:
        Intent yeni1=new Intent(this, MainActivity.class);
        this.startActivity(yeni1);break;
        case R.id.grup_olustur:
        Intent yeni2=new Intent(this, GrupEkleActivity.class);
        this.startActivity(yeni2);
        break;
        }

        return super.onOptionsItemSelected(item);
    }
}
