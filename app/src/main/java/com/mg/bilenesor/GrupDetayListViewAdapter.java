package com.mg.bilenesor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Muhammed on 14.5.2015.
 */
public class GrupDetayListViewAdapter extends BaseAdapter{


    Context mContext;
    LayoutInflater inflater;
    private List<Gruplar> katilimcilarlist = null;
    private ArrayList<Gruplar> arraylist;
    public int pozisyon;
    Json jsonParser = new Json();
    private static final String url_katilimci_sil = "http://bilenesor.co.nf/katilimci_sil.php";
    private static final String TAG_SUCCESS = "success" ;
    private static final String TAG_MESSAGE = "message" ;
public String  katilimci_id;
    public GrupDetayListViewAdapter(Context context, List<Gruplar> katilimcilarlist) {
        mContext = context;
        this.katilimcilarlist = katilimcilarlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Gruplar>();
        this.arraylist.addAll(katilimcilarlist);
    }

    public class ViewHolder {
        TextView isim;
    }

    @Override
    public int getCount() {
        return katilimcilarlist.size();
    }

    @Override
    public Gruplar getItem(int position) {
        return katilimcilarlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_grup_detay, null);
            holder.isim = (TextView) view.findViewById(R.id.isimsoyisim);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.isim.setText(katilimcilarlist.get(position).getIsım());

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                pozisyon= position;

                AlertDialog show = new AlertDialog.Builder(mContext)
                        .setTitle("Sil")
                        .setMessage("Silmek İstediğinize Emin misiniz?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Calisan_Delete().execute();
                                Toast.makeText(mContext, "Başarıyla Silindi ", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        katilimcilarlist.clear();
        if (charText.length() == 0) {
            katilimcilarlist.addAll(arraylist);
        } else {
            for (Gruplar wp : arraylist) {
                if (wp.getIsım().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    katilimcilarlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    class Calisan_Delete extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {

        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            katilimci_id = katilimcilarlist.get(pozisyon).getId();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", katilimci_id));

            JSONObject json = jsonParser.makeHttpRequest(url_katilimci_sil, "POST", params);

            try {

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated

                    Log.d("Silme başarılı", json.getString(TAG_MESSAGE));


                }
                else {
                    Log.d("Silme Başarısız", json.getString(TAG_MESSAGE));

                    return json.getString(TAG_MESSAGE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            katilimcilarlist.remove(katilimcilarlist.get(pozisyon));
            notifyDataSetChanged();
        }
    }



}
