package com.mg.bilenesor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Muhammed on 14.5.2015.
 */
public class GrupSonucDetayListViewAdapter extends BaseAdapter{

    public String puan ="Henüz Puanınız Bulunmamaktadır..";
    public ProgressDialog pDialog;
    public static String kid;
    Context mContext;
    LayoutInflater inflater;
    private List<Gruplar> katilimcilist = null;
    private ArrayList<Gruplar> arraylist;
    public int pozisyon;
    Json jsonParser = new Json();

    public GrupSonucDetayListViewAdapter(Context context, List<Gruplar> katilimcilist) {
        mContext = context;
        this.katilimcilist = katilimcilist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Gruplar>();
        this.arraylist.addAll(katilimcilist);
    }

    public class ViewHolder {
        TextView isim;
    }

    @Override
    public int getCount() {
        return katilimcilist.size();
    }

    @Override
    public Gruplar getItem(int position) {
        return katilimcilist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_grup_sonuc_detay, null);
            holder.isim = (TextView) view.findViewById(R.id.isimsoyisim);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.isim.setText(katilimcilist.get(position).getIsım());

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                pozisyon= position;
                kid =  katilimcilist.get(pozisyon).getId();

                Intent i = new Intent (mContext,GrupKatilimciSonucDetayActivity.class);
                i.putExtra("kid",kid);

                mContext.startActivity(i);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        katilimcilist.clear();
        if (charText.length() == 0) {
            katilimcilist.addAll(arraylist);
        } else {
            for (Gruplar wp : arraylist) {
                if (wp.getIsım().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    katilimcilist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
