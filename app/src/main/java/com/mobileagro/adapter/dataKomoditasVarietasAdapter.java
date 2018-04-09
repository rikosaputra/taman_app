package com.mobileagro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.mana_livechatv2.R;

import java.util.ArrayList;

/**
 * Created by ThinkPad T440s VPro on 31/08/2016.
 */
public class dataKomoditasVarietasAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> Varietases;
    private static LayoutInflater inflater = null;

    public dataKomoditasVarietasAdapter(Context context, ArrayList<String> varietases) {
        this.Varietases = varietases;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return Varietases.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        System.out.println("ARRAY SIZE: " + Varietases.size());

        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.varietas_list, null);

        TextView tv1 = (TextView) vi.findViewById(R.id.title);
        String Varietas = Varietases.get(i);
        tv1.setText(Varietas);
        return vi;
    }

}
