package com.mobileagro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.mana_livechatv2.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by ThinkPad T440s VPro on 17/10/2016.
 */

public class listSentraProdAdapter extends BaseAdapter {
    Context context;
    String[] Komoditas;
    String[] ProdTh;

    private static LayoutInflater inflater = null;
    public listSentraProdAdapter(Context context, String[] Komoditas, String[] ProdTh) {
        this.context = context;
        this.Komoditas = Komoditas;
        this.ProdTh = ProdTh;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Komoditas.length;
    }

    @Override
    public Object getItem(int position) {
        return Komoditas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.activity_sentra_new_list, null);
        TextView tvTitle = (TextView) vi.findViewById(R.id.title);
        TextView tvProd = (TextView) vi.findViewById(R.id.produksi_tahunan);
        TextView tvKet = (TextView) vi.findViewById(R.id.keterangan);

        String komdditas = Komoditas[position];
        String prodTh = ProdTh[position];
        if (prodTh!=null && !prodTh.isEmpty()) {
            int prodInt = Integer.parseInt(prodTh);
            String prod = "";
            if (!prodTh.equals("")) {
                DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
                symbols.setGroupingSeparator('.');
                DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
                prod = formatter.format(prodInt);
            }
            tvTitle.setText(komdditas);
            tvProd.setText(prod);
            tvKet.setText("Per Tahun");
        }
        return vi;
    }
}
