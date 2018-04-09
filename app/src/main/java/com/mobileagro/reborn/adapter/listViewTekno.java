package com.mobileagro.reborn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.mana_livechatv2.R;

import java.util.ArrayList;

/**
 * Created by ThinkPad T440s VPro on 21/12/2016.
 */

public class listViewTekno extends BaseAdapter {
    Context context;
    ArrayList<String> Teknos;
    ArrayList<String> Docs;
    private static LayoutInflater inflater = null;
    public listViewTekno(Context context, ArrayList<String> Tekns, ArrayList<String> docs) {
        this.context = context;
        this.Teknos = Tekns;
        this.Docs = docs;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return Teknos.size();
    }

    @Override
    public Object getItem(int position) {
        return Docs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.varietas_list, null);
        String kesVarMK = Teknos.get(position);
        TextView text1 = (TextView) vi.findViewById(R.id.title);
        text1.setText(kesVarMK);
        return vi;
    }
}
