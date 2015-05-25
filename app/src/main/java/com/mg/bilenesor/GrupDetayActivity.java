package com.mg.bilenesor;

import android.app.ProgressDialog;
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


public class GrupDetayActivity extends ActionBarActivity {
        String grup_id;
        ListView list;
        GrupDetayListViewAdapter adapter;
        EditText editsearch;
        ArrayList<Gruplar> arraylist = new ArrayList<Gruplar>();
        public ProgressDialog pDialog;
        Json jsonParser = new Json();
        public JSONObject json;
        private static final String TAG_GRUP= "katilimcilar";
        private static final String TAG_GRUP_ADI = "katilimci_adi";
        private static final String TAG_GRUP_ID = "katilimci_id";
        public ArrayList<String> katilimci_ids,katilimci_ads;
        public JSONArray grup;

        private static final String url_katilimci_listele = "http://bilenesor.co.nf/katilimci_listele.php";

        class fillAdapter extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(GrupDetayActivity.this);
                pDialog.setMessage("Katılımcılar Yükleniyor...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

            }

            @Override
            public Void doInBackground(Void... params) {

                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("grup_id",grup_id));
                json = jsonParser.makeHttpRequest(url_katilimci_listele, "POST", params1);

                try {
                    grup = json.getJSONArray(TAG_GRUP);

                    katilimci_ids = new ArrayList<String>();
                    katilimci_ads = new ArrayList<String>();

                    for (int i = 0; i < grup.length(); i++) {
                        JSONObject item = grup.getJSONObject(i);
                        String katilimciadi = item.getString(TAG_GRUP_ADI);
                        String katilimciid =item.getString(TAG_GRUP_ID);

                        katilimci_ids.add(katilimciid);
                        katilimci_ads.add(katilimciadi);
                    }



                    for (int i = 0; i < katilimci_ads.size() ; i++)
                    {
                        Gruplar grup = new Gruplar(katilimci_ads.get(i),katilimci_ids.get(i));
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
                list = (ListView) findViewById(R.id.liste);

                // Pass results to ListViewAdapter Class
                adapter = new GrupDetayListViewAdapter(GrupDetayActivity.this, arraylist);

                // Binds the Adapter to the ListView
                list.setAdapter(adapter);

                editsearch = (EditText) findViewById(R.id.search);

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
            setContentView(R.layout.activity_main);
            //TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            //
            // olusturan_id = "22";//tm.getSimSerialNumber();
            //  list = (ListView) findViewById(R.id.liste);
            grup_id = getIntent().getExtras().getString("id");
            new fillAdapter().execute();


        }
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
             getMenuInflater().inflate(R.menu.menu_grup_detay, menu);

            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
// TODO Auto-generated method stub
            switch(item.getItemId()){
                case R.id.grup_ara:
                    Intent yeni=new Intent(GrupDetayActivity.this, GrupAraActivity.class);
                    startActivity(yeni);break;
                case R.id.gruplarim:
                    Intent yeni1=new Intent(GrupDetayActivity.this, MainActivity.class);
                    startActivity(yeni1);break;
                case R.id.katilimci_ekle:
                    Intent yeni2=new Intent(GrupDetayActivity.this, KatilimciEkleActivity.class);
                    yeni2.putExtra("grup_id",grup_id);
                    startActivity(yeni2);
                    break;

            }
            return super.onOptionsItemSelected(item);
        }

    }




