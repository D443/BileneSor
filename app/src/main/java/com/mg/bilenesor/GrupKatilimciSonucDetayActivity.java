package com.mg.bilenesor;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class GrupKatilimciSonucDetayActivity extends ActionBarActivity {

    private TextView txt_sonuc;
    public String kid;
    String line=null;
    String result=null;
    public String puan;
    Json jsonParser = new Json();
    InputStream is=null;
    public String grup_id;
    public void getPuan()
    {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("id",kid));

        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://bilenesor.co.nf/katilimci_sonuc.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("", "Bağlantı başarılı.");
        }
        catch(Exception e)
        {
            Log.e("Hata 1", e.toString());
            Toast.makeText(getApplicationContext(), "Bağlantı sırasında bir hata oluştu." + e,
                    Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("", "Bağlantı başarılı.");
        }
        catch(Exception e)
        {
            Log.e("Hata 2", e.toString());
        }

        try
        {
            JSONObject json_data = new JSONObject(result);
            puan=(json_data.getString("puan"));
            //grup_id=(json_data.getString("grup_id"));
            txt_sonuc = (TextView) findViewById(R.id.txt_sonuc);
            txt_sonuc.setText("Ortalama Puan : " + puan );
        }
        catch(Exception e)
        {
            Log.e("Hata 3", e.toString());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grup_katilimci_sonuc_detay);
        kid = getIntent().getExtras().getString("kid");
        getPuan();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grup_katilimci_sonuc_detay, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.sonuc:
                Intent yeni=new Intent(this, Sonuclar.class);
                startActivity(yeni);break;
            case R.id.gruplarim:
                Intent yeni1=new Intent(this, MainActivity.class);
                startActivity(yeni1);break;

        }

        return super.onOptionsItemSelected(item);
    }
}
