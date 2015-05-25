package com.mg.bilenesor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class GrupAraGoruntuleActivity extends ActionBarActivity {

    Context mContext;
    String grup_kodu;
    ListView list;
    GrupOylaListViewAdapter adapter;
    EditText editsearch;

    ArrayList<Gruplar> arraylist = new ArrayList<Gruplar>();
    public ProgressDialog pDialog;
    Json jsonParser = new Json();
    public JSONObject json;

    private static final String TAG_GRUP= "gruplar";
    private static final String TAG_GRUP_ADI = "grup_adi";
    private static final String TAG_GRUP_ID = "grup_id";
    public ArrayList<String> grup_ids,grup_ads;
    public JSONArray grup;

    private static final String url_grup_listele = "http://bilenesor.co.nf/grup_ara.php";

    class fillAdapter extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GrupAraGoruntuleActivity.this);
            pDialog.setMessage("Gruplar Yükleniyor...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        public Void doInBackground(Void... params) {

            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("grup_kodu",grup_kodu));
            json = jsonParser.makeHttpRequest(url_grup_listele, "POST", params1);

            try {
                grup = json.getJSONArray(TAG_GRUP);

                grup_ids = new ArrayList<String>();
                grup_ads = new ArrayList<String>();

                for (int i = 0; i < grup.length(); i++) {
                    JSONObject item = grup.getJSONObject(i);
                    String grupadi = item.getString(TAG_GRUP_ADI);
                    String grupid =item.getString(TAG_GRUP_ID);

                    grup_ids.add(grupid);
                    grup_ads.add(grupadi);
                }

                for (int i = 0; i < grup_ads.size() ; i++)
                {
                    Gruplar grup = new Gruplar(grup_ads.get(i),grup_ids.get(i));
                    arraylist.add(grup);
                }

            } catch (Exception e) {
                Log.w("Error", e.getMessage());
                e.printStackTrace();
            }

            return  null;
        }
        protected void onPostExecute(Void args) {

            pDialog.dismiss();
            list = (ListView) findViewById(R.id.liste1);

            // Pass results to ListViewAdapter Class
            adapter = new GrupOylaListViewAdapter(GrupAraGoruntuleActivity.this, arraylist);

            // Binds the Adapter to the ListView
            list.setAdapter(adapter);

            editsearch = (EditText) findViewById(R.id.search1);

            // Capture Text in EditText
            editsearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    // Pass results to ListViewAdapter Class
                    // TODO Auto-generated method stub
                    String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                    adapter.filter(text);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grup_ara_goruntule);

        grup_kodu = getIntent().getExtras().getString("grup_kodu");
        new fillAdapter().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grup_sil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.gruplarim:
                Intent yeni1 = new Intent(this, MainActivity.class);
                this.startActivity(yeni1);
                break;
            case R.id.grup_olustur:
                Intent yeni2 = new Intent(this, GrupEkleActivity.class);
                this.startActivity(yeni2);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
