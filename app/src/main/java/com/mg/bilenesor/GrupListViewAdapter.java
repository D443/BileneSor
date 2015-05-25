package com.mg.bilenesor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class GrupListViewAdapter extends BaseAdapter {


        Context mContext;
        LayoutInflater inflater;
        private List<Gruplar> gruplarlist = null;
        private ArrayList<Gruplar> arraylist;
        public int pozisyon;
        public String id;


        public GrupListViewAdapter(Context context, List<Gruplar> gruplarlist) {
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
                view = inflater.inflate(R.layout.grup_detay, null);
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
                    id =  gruplarlist.get(pozisyon).getId();

                    Log.d("id"+ id ," ");
                    Intent i = new Intent (mContext,GrupDetayActivity.class);
                    i.putExtra("id",id);
                    mContext.startActivity(i);


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

    }



