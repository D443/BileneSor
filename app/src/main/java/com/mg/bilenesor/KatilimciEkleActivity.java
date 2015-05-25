package com.mg.bilenesor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class KatilimciEkleActivity extends ActionBarActivity implements View.OnClickListener{
    String grup_id;
    Button kayit;
    EditText ad;
    private ProgressDialog pDialog;
    Json jsonParser = new Json();

    private static final String Url_Save = "http://www.bilenesor.co.nf/katilimci_ekle.php";
    private static final String TAG_SUCCESS = "success" ;
    private static final String TAG_MESSAGE = "message" ;

    class SignUpExecute extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KatilimciEkleActivity.this);
            pDialog.setMessage("Katilimci Kayıt Ediliyor...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String adi = ad.getText().toString();
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("adi", adi));
            params.add(new BasicNameValuePair("grup_id", grup_id));
            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(Url_Save, "POST",
                    params);

            try {

                int success = json.getInt(TAG_SUCCESS);
                Log.d("Kayıt Başarılı " + success, "");
                if (success == 1) {
                    // successfully updated

                    Intent i = new Intent(KatilimciEkleActivity.this, GrupDetayActivity.class);
                    i.putExtra("id",grup_id);
                    startActivity(i);
                    finish();


                }
                else {
                    Log.d("Kayıt Başarısız", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();

            if (file_url != null) {
                Toast.makeText(KatilimciEkleActivity.this, file_url, Toast.LENGTH_LONG)
                        .show();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_katilimci_ekle);
        kayit = (Button) findViewById(R.id.btn_katilimci_ekle);
        ad = (EditText) findViewById(R.id.txt_katilimci_ekle);
        grup_id = getIntent().getExtras().getString("grup_id");


        kayit.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_katilimci_ekle:
                if(isOnline()){
                    new SignUpExecute().execute();
                }
                else {
                    Toast.makeText(KatilimciEkleActivity.this, "Lütfen İnternet Bağlantınızı Kontrol Ediniz.", Toast.LENGTH_LONG) .show();} break; default: break;
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_katilimci_ekle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.grup_ara:
                Intent yeni=new Intent(KatilimciEkleActivity.this, GrupAraActivity.class);
                startActivity(yeni);break;
            case R.id.gruplarim:
                Intent yeni1=new Intent(KatilimciEkleActivity.this, MainActivity.class);
                startActivity(yeni1);break;


        }


        return super.onOptionsItemSelected(item);
    }
}
