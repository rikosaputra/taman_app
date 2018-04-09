package com.mobileagro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mobileagro.adapter.listVarietasAdapter;
import com.mobileagro.demo1.fetchListVarietas;

import com.example.user.mana_livechatv2.R;
import java.util.ArrayList;

public class Tab3List extends Fragment implements fetchListVarietas.fetching.AsyncResponse {
    Button button1;
    TextView tv1, tv2;
    View v;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab_3_list,container,false);
        /*
        button1 = (Button) v.findViewById(R.id.map_button);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        };
        button1.setOnClickListener(listener);
        */
        TextView tv = (TextView) v.findViewById(R.id.currLoc);
        globalKab gKab = globalKab.getInstance();
        int data = gKab.getData();
        String dataStr = String.valueOf(data);
        String kabName = gKab.getName();
        tv.setText(kabName);

        // fetchListVarietas.fetching asyncTask = new fetchListVarietas.fetching(this);
        // asyncTask.execute(dataStr);

/*

        listView.setOnClickListener(new AdapterView.OnClickListener() {

            @Override
            public void onClick(View v) {
                String item = ((TextView) v.findViewById(R.id.varietas_name)).getText().toString();
                final LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View viewPopUp = layoutInflater.inflate(R.layout.vari, null);
                final PopupWindow windowPopUp = new PopupWindow(viewPopUp, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            }
        });
*/
        return v;
    }

    @Override
    public void processFinish() {
        ArrayList<String> komoditas = fetchListVarietas.listKomoditas;
        ArrayList<String> tahunMusim = fetchListVarietas.listTahunMusim;
        ArrayList<String> jenisRawan = fetchListVarietas.listJenisRawan;
        ArrayList<String> jenisTahun = fetchListVarietas.listJenisTahun;
        ArrayList<String> luasPotensi = fetchListVarietas.listLuasPotensi;
        ArrayList<String> luasTerkena = fetchListVarietas.listLuasTerkena;
        ArrayList<String> persenTerkena = fetchListVarietas.listPersenTrkena;
        ArrayList<String> statusKerawanan = fetchListVarietas.listStatusKerawanan;
        ArrayList<String> luasKerusakan = fetchListVarietas.listLuasKerusakan;
        ArrayList<String> persenKerusakan = fetchListVarietas.listPersenKerusakan;
        ArrayList<String> statusKerusakan = fetchListVarietas.listStatusKerusakan;
        ArrayList<String> varietas = fetchListVarietas.listVarietasRekom;

        listView = (ListView) v.findViewById(R.id.list3View);

        listView.setAdapter(new listVarietasAdapter(v.getContext(), komoditas, tahunMusim, jenisRawan,
                jenisTahun, luasPotensi, luasTerkena, persenTerkena, statusKerawanan, luasKerusakan,
                persenKerusakan, statusKerusakan, varietas));

    }
}
