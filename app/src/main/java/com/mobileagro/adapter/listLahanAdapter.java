package com.mobileagro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.user.mana_livechatv2.R;

/**
 * Created by ThinkPad T440s VPro on 18/07/2016.
 */
public class listLahanAdapter extends BaseAdapter {
    Context context;
    String[] dataKom;
    String[] dataPersenKena;
    String[] dataAvg;
    String[] dataKeslah;
    private static LayoutInflater inflater = null;

    public listLahanAdapter(Context context, String[] dataKom, String[] dataKeslah) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.dataKom = dataKom;
        this.dataKeslah = dataKeslah;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataKom.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return dataKom[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.tab_2_list_item, null);
        TextView text1 = (TextView) vi.findViewById(R.id.title);
        TextView text2 = (TextView) vi.findViewById(R.id.luas_potensi);
        ImageView iv = (ImageView) vi.findViewById(R.id.icon);
        String Komoditi = dataKom[position];
        switch(Komoditi) {
            case "SI":
                text1.setText("Sawah Irigasi");
                break;
            case "SP":
                text1.setText("Sawah Pasang Surut");
                break;
            case "PG":
                text1.setText("Padi Gogo");
                break;
            case "SL":
                text1.setText("Sawah Lebak");
                break;
            case "SH":
                text1.setText("Sawah Tadah Hujan");
                break;
            case "KD":
                text1.setText("Kedelai");
                break;
            case "JG":
                text1.setText("Jagung");
                break;
        }
        text2.setText("Kesesuaian Lahan: " + dataKeslah[position] + " ha");
        if (Komoditi.equals("JG")) {
            iv.setImageResource(R.drawable.jagung_marker);
        } else if (Komoditi.equals("SI")) {
            iv.setImageResource(R.drawable.padi_marker);
        } else if (Komoditi.equals("SP")) {
            iv.setImageResource(R.drawable.padi_marker);
        } else if (Komoditi.equals("PG")) {
            iv.setImageResource(R.drawable.padi_marker);
        } else if (Komoditi.equals("SL")) {
            iv.setImageResource(R.drawable.padi_marker);
        } else if (Komoditi.equals("SH")) {
            iv.setImageResource(R.drawable.padi_marker);
        } else if (Komoditi.equals("KD")) {
            iv.setImageResource(R.drawable.kedelai_marker);
        }  else if (Komoditi.equals("JG")) {
            iv.setImageResource(R.drawable.jagung_marker);
        } else {

        }
        return vi;
    }
}
