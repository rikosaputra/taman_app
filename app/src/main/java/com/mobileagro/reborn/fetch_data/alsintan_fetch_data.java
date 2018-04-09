package com.mobileagro.reborn.fetch_data;

import android.os.AsyncTask;

import com.mobileagro.demo1.fetchListVarietas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ThinkPad T440s VPro on 23/11/2016.
 */

public class alsintan_fetch_data {
    public static ArrayList<String> alsintanIds;
    public static ArrayList<String> alsintanTitles;
    public static ArrayList<String> alsintanDescs;
    public static ArrayList<String> alsintanAlamats;
    public static ArrayList<String> alsintanPhones;
    public static ArrayList<String> alsintanEmails;
    public static ArrayList<String> alsintanPoses;
    public static class fetching extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            alsintanIds = new ArrayList<>();
            alsintanTitles = new ArrayList<>();
            alsintanDescs = new ArrayList<>();
            alsintanAlamats = new ArrayList<>();
            alsintanPhones = new ArrayList<>();
            alsintanEmails = new ArrayList<>();
            alsintanPoses = new ArrayList<>();

            String postParameters = "keyword=" + params[0] + "&kab_id=" + params[1];
            String readStream = "";
            try {
                URL url = new URL("http://202.56.170.37/mobile-agro/cari_alsintan.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);

                urlConnection.setRequestMethod("POST");
                urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(postParameters);
                out.close();

                InputStream in = urlConnection.getInputStream();
                readStream = readStream(in);
                System.out.println("Read-Response: " + readStream);

                try {
                    JSONArray jsonarray = new JSONArray(readStream);
                    if (readStream != null) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObj = jsonarray.getJSONObject(i);
                            String id = jsonObj.getString("id");
                            String title = jsonObj.getString("title");
                            String description = jsonObj.getString("description");
                            String alamat = jsonObj.getString("alamat");
                            String phone = jsonObj.getString("phone");
                            String email = jsonObj.getString("email");
                            String latitude = jsonObj.getString("latitude");
                            String longitude = jsonObj.getString("longitude");

                        }
                    }
                } catch (JSONException ep) {
                    ep.printStackTrace();
                }
            } catch (Exception ep) {
                System.out.println("Failed: " + ep);
            }
            return readStream;
        }
        private String readStream(InputStream in) {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String nextLine = "";
                while ((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish();
        }

        public AsyncResponse delegate = null;

        public fetching(AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish();
        }
    }
}
