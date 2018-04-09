package com.mobileagro;

/**
 * Created by riko on 14/06/2016.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.user.mana_livechatv2.R;

public class Tab1List extends Fragment {
    Button btn1;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1_list,container,false);
        TextView tv = (TextView) v.findViewById(R.id.currLoc);
        btn1 = (Button) v.findViewById(R.id.map_button);
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        };
        btn1.setOnClickListener(listener);
        globalKab gKab = globalKab.getInstance();
        int data = gKab.getData();
        String kabName = gKab.getName();
        tv.setText(kabName);
        String hasil = "Hasil ===> " + String.valueOf(data);
        Log.d(hasil, hasil );
        return v;
    }


}
