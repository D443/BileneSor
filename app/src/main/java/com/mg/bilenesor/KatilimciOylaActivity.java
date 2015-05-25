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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class KatilimciOylaActivity extends ActionBarActivity implements View.OnClickListener  {

    String puan;
    Button kayit;
    String katilimci_id;
    String oylayan_id;
    private ProgressDialog pDialog;
    private static final String TAG_GRUP_ID = "grup_id";
    String grup_id;
    Json jsonParser = new Json();

    private static final String Url_Save = "http://www.bilenesor.co.nf/katilimci_oyla.php";
    private static final String TAG_SUCCESS = "success" ;
    private static final String TAG_MESSAGE = "message" ;

    class SignUpExecute extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KatilimciOylaActivity.this);
            pDialog.setMessage("Katılımcı Oylanıyor...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("puan", puan));
            params.add(new BasicNameValuePair("katilimci_id", katilimci_id));
            params.add(new BasicNameValuePair("degerlendiren", oylayan_id));


            JSONObject json = jsonParser.makeHttpRequest(Url_Save, "POST",
                    params);

            try {

                int success = json.getInt(TAG_SUCCESS);
                Log.d("Başarılı" + success, "");
                if (success == 1) {
                     grup_id = json.getString(TAG_GRUP_ID);
                   Intent i = new Intent(KatilimciOylaActivity.this, GrupOylaDetayActivity.class);
                    i.putExtra("id",grup_id);
                   startActivity(i);
                   finish();


                }
                else if (success == 0) {

                   grup_id = json.getString(TAG_GRUP_ID);
                   Intent i = new Intent(KatilimciOylaActivity.this, GrupOylaDetayActivity.class);
                   i.putExtra("id",grup_id);
                   startActivity(i);

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
                Toast.makeText(KatilimciOylaActivity.this, file_url, Toast.LENGTH_LONG)
                        .show();
            }


        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_katilimci_oyla);
        katilimci_id = getIntent().getExtras().getString("katilimci_id");

        Spinner sampleSpinner = (Spinner) findViewById(R.id.sampleSpinner);

        String[] content = { "1", "2", "3", "4", "5","6","7","8","9","10" };

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, content);

        adapter.setDropDownViewResource(R.layout.spinner_layout);

        sampleSpinner.setAdapter(adapter);

        sampleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // TODO Auto-generated method stub
                puan = parent.getItemAtPosition(pos).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

        TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        oylayan_id = tm.getSimSerialNumber();
        kayit = (Button) findViewById(R.id.btn_oyla);
        kayit.setOnClickListener(this);

    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_oyla:
                if(isOnline()){
                    new SignUpExecute().execute();
                }
                else {
                    Toast.makeText(KatilimciOylaActivity.this, "Lütfen İnternet Bağlantınızı Kontrol Ediniz.", Toast.LENGTH_LONG) .show();} break; default: break;
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
        getMenuInflater().inflate(R.menu.menu_katilimci_oyla, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.grup_ara:
                Intent yeni=new Intent(KatilimciOylaActivity.this, GrupAraActivity.class);
                startActivity(yeni);break;
            case R.id.gruplarim:
                Intent yeni1=new Intent(KatilimciOylaActivity.this, MainActivity.class);
                startActivity(yeni1);break;}

        return super.onOptionsItemSelected(item);
    }
}
