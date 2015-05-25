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
 * Created by Muhammed on 23.5.2015.
 */
public class GrupSilListViewAdapter extends BaseAdapter{

    Context mContext;
    LayoutInflater inflater;
    private List<Gruplar> gruplarlist = null;
    private ArrayList<Gruplar> arraylist;
    public int pozisyon;
    Json jsonParser = new Json();
    private static final String url_calisan_sil = "http://bilenesor.co.nf/grup_sil.php";
    private static final String TAG_SUCCESS = "success" ;
    private static final String TAG_MESSAGE = "message" ;
    public String  katilimci_id;
    public GrupSilListViewAdapter(Context context, List<Gruplar> gruplarlist) {
        mContext = context;
        this.gruplarlist = gruplarlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Gruplar>();
        this.arraylist.addAll(gruplarlist);
    }

    public class ViewHolder {
        TextView isim;
    }

    @Override
    public int getCount() {
        return gruplarlist.size();
    }

    @Override
    public Gruplar getItem(int position) {
        return gruplarlist.get(position);
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

        holder.isim.setText(gruplarlist.get(position).getIsım());

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                pozisyon= position;

                AlertDialog show = new AlertDialog.Builder(mContext)
                        .setTitle("Sil")
                        .setMessage("Silmek İstediğinize Emin misiniz?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Grup_Delete().execute();
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
        gruplarlist.clear();
        if (charText.length() == 0) {
            gruplarlist.addAll(arraylist);
        } else {
            for (Gruplar wp : arraylist) {
                if (wp.getIsım().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    gruplarlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    class Grup_Delete extends AsyncTask<String, String, String> {

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

            katilimci_id = gruplarlist.get(pozisyon).getId();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", katilimci_id));

            JSONObject json = jsonParser.makeHttpRequest(url_calisan_sil, "POST", params);

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
            gruplarlist.remove(gruplarlist.get(pozisyon));
            notifyDataSetChanged();
        }
    }



}
