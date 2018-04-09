package com.mobileagro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import com.example.user.mana_livechatv2.R;

/**
 * Created by ThinkPad T440s VPro on 18/07/2016.
 */
public class listVarietasAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> Komoditas;
    ArrayList<String> tahunMusim;
    ArrayList<String> jenisRawan;
    ArrayList<String> jenisTahun;
    ArrayList<String> luasPotensi;
    ArrayList<String> luasTerkena;
    ArrayList<String> persenTerkena;
    ArrayList<String> statusKerawanan;
    ArrayList<String> luasKerusakan;
    ArrayList<String> persenKerusakan;
    ArrayList<String> statusKerusakan;
    ArrayList<String> varietas;
    private static LayoutInflater inflater = null;
    public listVarietasAdapter(Context context, ArrayList<String> Komoditas, ArrayList<String> tahunMusim, ArrayList<String> jenisRawan,
                               ArrayList<String> jenisTahun, ArrayList<String> luasPotensi, ArrayList<String> luasTerkena, ArrayList<String> persenTerkena,
                               ArrayList<String> statusKerawanan, ArrayList<String> luasKerusakan, ArrayList<String> persenKerusakan,
                               ArrayList<String> statusKerusakan, ArrayList<String> varietas) {
        this.context = context;
        this.Komoditas = Komoditas;
        this.tahunMusim = tahunMusim;
        this.jenisRawan = jenisRawan;
        this.jenisTahun = jenisTahun;
        this.luasPotensi = luasPotensi;
        this.luasTerkena = luasTerkena;
        this.persenTerkena = persenTerkena;
        this.statusKerawanan = statusKerawanan;
        this.luasKerusakan = luasKerusakan;
        this.persenKerusakan = persenKerusakan;
        this.statusKerusakan = statusKerusakan;
        this.varietas = varietas;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return Komoditas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.tab_3_list_item, null);
        TextView tv1 = (TextView) vi.findViewById(R.id.title);
        TextView tv2 = (TextView) vi.findViewById(R.id.tva1);
        TextView tv3 = (TextView) vi.findViewById(R.id.tvb1);
        TextView tv4 = (TextView) vi.findViewById(R.id.tvc1);
        TextView tv5 = (TextView) vi.findViewById(R.id.tvd1);
        TextView tv6 = (TextView) vi.findViewById(R.id.tve1);
        TextView tv7 = (TextView) vi.findViewById(R.id.tvf1);
        TextView tv8 = (TextView) vi.findViewById(R.id.tvg1);
        TextView tv9 = (TextView) vi.findViewById(R.id.tvh1);
        TextView tv10 = (TextView) vi.findViewById(R.id.tvi1);
        TextView tv11 = (TextView) vi.findViewById(R.id.tvj1);
        TextView tv12 = (TextView) vi.findViewById(R.id.tvk1);
        ImageView iv = (ImageView) vi.findViewById(R.id.icon);

        String Komoditase = Komoditas.get(position);
        String tahunMusime = tahunMusim.get(position);
        String jenisRawane = jenisRawan.get(position);
        String jenisTahune = jenisTahun.get(position);
        String luasPotensie = luasPotensi.get(position);
        String luasTerkenae = luasTerkena.get(position);
        String persenTerkenae = persenTerkena.get(position);
        String statusKerawanane = statusKerawanan.get(position);
        String luasKerusakane = luasKerusakan.get(position);
        String persenKerusakane = persenKerusakan.get(position);
        String statusKerusakane = statusKerusakan.get(position);
        String varietase = varietas.get(position);

        tv1.setText(Komoditase);
        tv2.setText(tahunMusime);
        tv3.setText(jenisRawane);
        tv4.setText(jenisTahune);
        tv5.setText(luasPotensie);
        tv6.setText(luasTerkenae);
        tv7.setText(persenTerkenae);
        tv8.setText(statusKerawanane);
        tv9.setText(luasKerusakane);
        tv10.setText(persenKerusakane);
        tv11.setText(statusKerusakane);
        tv12.setText(varietase);

        if (Komoditase.equals("JAGUNG")) {
            iv.setImageResource(R.drawable.jagung_marker);
        } else if (Komoditase.equals("PADI SAWAH")) {
            iv.setImageResource(R.drawable.padi_marker);
        } else if (Komoditase.equals("KEDELAI")) {
            iv.setImageResource(R.drawable.kedelai_marker);
        } else {

        }
        return vi;
    }
}
