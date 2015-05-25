package com.mg.bilenesor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
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


public class GrupEkleActivity extends ActionBarActivity implements View.OnClickListener {

    Button kayit;
    EditText ad,kod;
    String sim_seri_no;
    private ProgressDialog pDialog;
    Json jsonParser = new Json();
    private static final String Url_Save = "http://www.bilenesor.co.nf/grup_ekle.php";
    private static final String TAG_SUCCESS = "success" ;
    private static final String TAG_MESSAGE = "message" ;

    class GrupSaveExecute extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GrupEkleActivity.this);
            pDialog.setMessage("Grup Kayıt Ediliyor...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String adi = ad.getText().toString();
            String kodu = kod.getText().toString();


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("adi", adi));
            params.add(new BasicNameValuePair("grup_kodu", kodu));
            params.add(new BasicNameValuePair("olusturan_id", sim_seri_no));


            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(Url_Save, "POST",
                    params);

            try {

                int success = json.getInt(TAG_SUCCESS);
                Log.d("success" + success,"");
                if (success == 1) {
                    // successfully updated

                    Intent calisanMenu = new Intent(GrupEkleActivity.this, MainActivity.class);
                    startActivity(calisanMenu);
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
                Toast.makeText(GrupEkleActivity.this, file_url, Toast.LENGTH_LONG)
                        .show();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grup_ekle);
        kayit = (Button) findViewById(R.id.btn_kaydet);
        ad = (EditText) findViewById(R.id.txt_ad);
        kod=(EditText) findViewById(R.id.txt_kod);
        TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        sim_seri_no = tm.getSimSerialNumber();

        kayit.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grup_ekle, menu);
        return true;
    }
    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_kaydet:
                if(isOnline()){
                    String adi = ad.getText().toString();
                    String kodu = kod.getText().toString();
                    if (adi.matches("")|| kodu.matches("") ) {


                        Toast.makeText(GrupEkleActivity.this, "Lütfen Boş Alanları Doldurunuz.", Toast.LENGTH_LONG).show();
                    }
                    else {

                        new GrupSaveExecute().execute();

                    }


                }
                else {
                    Toast.makeText(GrupEkleActivity.this, "Lütfen İnternet Bağlantınızı Kontrol Ediniz.", Toast.LENGTH_LONG) .show();} break; default: break;
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.grup_ara:
                Intent yeni=new Intent(GrupEkleActivity.this, GrupAraActivity.class);
                startActivity(yeni);break;

            case R.id.grup_sil:
                Intent yeni1=new Intent(GrupEkleActivity.this, GrupAraActivity.class);
                startActivity(yeni1);break;
            case R.id.gruplarim:
                Intent yeni2=new Intent(GrupEkleActivity.this, MainActivity.class);
                startActivity(yeni2);break;
        }

        return super.onOptionsItemSelected(item);
    }
}
