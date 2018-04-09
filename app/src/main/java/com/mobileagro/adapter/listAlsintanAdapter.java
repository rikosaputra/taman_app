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
 * Created by ThinkPad T440s VPro on 23/11/2016.
 */

public class listAlsintanAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> alsintanId;
    ArrayList<String> alsintanTitles;
    ArrayList<String> alsintanDescs;
    ArrayList<String> alsintanAlamats;
    ArrayList<String> alsintanPoses;
    ArrayList<String> alsintanKats;
    ArrayList<String> alsintanPhones;
    ArrayList<String> alsintanEmails;
    private static LayoutInflater inflater = null;
    public listAlsintanAdapter(Context context, ArrayList<String> alsintanId, ArrayList<String> alsintanTitles) {
        // , ArrayList<String> alsintanDescs,
        // ArrayList<String> alsintanPoses, ArrayList<String> alsintanKats, ArrayList<String> alsintanPhones, ArrayList<String> alsintanEmails, ArrayList<String> alsintanAlamats
        this.context = context;
        this.alsintanId = alsintanId;
        this.alsintanTitles = alsintanTitles;
        this.alsintanDescs = alsintanDescs;
        this.alsintanPoses = alsintanPoses;
        this.alsintanKats = alsintanKats;
        this.alsintanPhones = alsintanPhones;
        this.alsintanEmails = alsintanEmails;
        this.alsintanAlamats = alsintanAlamats;
    }
    @Override
    public int getCount() {
        return alsintanTitles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        String[] alsintStr = new String[alsintanId.size()];
        String alsinId = alsintStr[position];
        return Long.parseLong(alsinId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.alsintan_list_item, null);
        String title = alsintanTitles.get(position);
        String description = alsintanDescs.get(position);
        String alamat = alsintanAlamats.get(position);
        String phone = alsintanPhones.get(position);
        String email = alsintanEmails.get(position);
        String pos = alsintanPoses.get(position);
        String kategori = alsintanKats.get(position);

        TextView titleText = (TextView) vi.findViewById(R.id.title);
        titleText.setText(title);
        return vi;
    }
}
