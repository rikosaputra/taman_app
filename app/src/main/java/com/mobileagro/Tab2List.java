package com.mobileagro;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileagro.adapter.listLahanAdapter;
import com.mobileagro.demo1.globalLahan;

import java.util.ArrayList;
import com.example.user.mana_livechatv2.R;

public class Tab2List extends Fragment {
    Button button1;
    TextView Tv;
    int popHeight = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2_list,container,false);
        button1 = (Button) v.findViewById(R.id.map_button);
        Tv = (TextView) v.findViewById(R.id.tview);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        };
        button1.setOnClickListener(listener);
        globalLahan gLahan = globalLahan.getInstance();
        ArrayList<String> Komoditas = gLahan.getKom();
        ArrayList<String> Persen = gLahan.getPersen();
        ArrayList<String> Avg = gLahan.getAvg();

        String[] komArray = new String[Komoditas.size()];
        komArray = Komoditas.toArray(komArray);
        String[] persenArray = new String[Persen.size()];
        persenArray = Persen.toArray(persenArray);
        String[] avgArray = new String[Avg.size()];
        avgArray = Avg.toArray(avgArray);
        /*
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(v.getContext(),R.layout.tab_2_list_item,Komoditas);
        ListView listView = (ListView) v.findViewById(R.id.listview);
        listView.setAdapter(listAdapter);
        */
        final ListView listView = (ListView) v.findViewById(R.id.listview);
        // listView.setAdapter(new listLahanAdapter(v.getContext(), komArray, persenArray, avgArray));
        listView.setAdapter(new listLahanAdapter(v.getContext(), komArray, persenArray));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView) view.findViewById(R.id.title)).getText().toString();
                final LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View viewPopUp = layoutInflater.inflate(R.layout.show_info, null);
                final PopupWindow windowPopUp = new PopupWindow(viewPopUp, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
                Button closeButton = (Button) viewPopUp.findViewById(R.id.buttonClose);

                LinearLayout linLayout = (LinearLayout) viewPopUp.findViewById(R.id.linearLayout);

                WebView engine = (WebView) viewPopUp.findViewById(R.id.webView);
                engine.setWebViewClient(new MyWebViewClient());
                engine.getSettings().setJavaScriptEnabled(true);

                // viewPopUp.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                // popHeight = viewPopUp.getMeasuredHeight();
                linLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                popHeight = linLayout.getMeasuredHeight();

                Log.d("Height Popup Window---", String.valueOf(popHeight));

                RelativeLayout layout = (RelativeLayout) viewPopUp.findViewById(R.id.relLayout);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
                params.height = ((int)(popHeight*0.8));
                Log.d("Window Popup Window---", String.valueOf(popHeight*0.8));


                String urlPath = TextUtils.htmlEncode(item.toLowerCase());
                String url = "http://bbsdlp.litbang.pertanian.go.id/kriteria/" + urlPath + ".php";
                Log.d("URL ::: " , url);
                engine.loadUrl(url);
                closeButton.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        windowPopUp.dismiss();
                    }
                });
                windowPopUp.showAsDropDown(Tv);
                Toast.makeText(getActivity().getBaseContext(), item, Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }
    private class MyWebViewClient  extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
