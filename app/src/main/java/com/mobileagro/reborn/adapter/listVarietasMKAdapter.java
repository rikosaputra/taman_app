package com.mobileagro.reborn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.mana_livechatv2.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by ThinkPad T440s VPro on 20/12/2016.
 */

public class listVarietasMKAdapter extends BaseAdapter {
    Context context;
    ArrayList<String>  varietasMKs;
    public listVarietasMKAdapter(Context context, ArrayList<String> VarMks) {
        this.context = context;
        this.varietasMKs = VarMks;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    private static LayoutInflater inflater = null;
    @Override
    public int getCount() {
        return varietasMKs.size();
    }

    @Override
    public Object getItem(int position) {
        return 0;
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
        String kesVarMK = varietasMKs.get(position);
        TextView text1 = (TextView) vi.findViewById(R.id.title);
        text1.setText(kesVarMK);
        return vi;
    }
}
