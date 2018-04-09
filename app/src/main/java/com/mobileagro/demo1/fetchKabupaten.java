package com.mobileagro.demo1;


import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ThinkPad T440s VPro on 17/07/2016.
 */
public class fetchKabupaten {
    public static class getKabs extends AsyncTask<String, Void, ArrayList<String> > {
        ArrayList<String> lokasi = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        @Override
        protected ArrayList doInBackground(String... params) {
            URL url = null;
            String readStream = "";
            try {
                url = new URL("http://202.56.170.37/mobile-agro/varietas/kabupaten_list.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                InputStream in = urlConnection.getInputStream();
                readStream = readStream(in);
                System.out.println("Read-Response: " + readStream);
                try {
                    JSONArray jsonarray = new JSONArray(readStream);
                    if (readStream != null) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObj = jsonarray.getJSONObject(i);
                            String id = jsonObj.getString("kab_id");
                            String kabupaten = jsonObj.getString("kabupaten");
                            String provinsi = jsonObj.getString("provinsi");
                            String pulau = "PULAU " + jsonObj.getString("pulau");
                            String Location = kabupaten + ", " + provinsi + ", " + pulau;
                            lokasi.add(Location);
                            ids.add(id);
                        }
                    }
                } catch (JSONException ep) {
                    ep.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private static String readStream(InputStream in) {
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
            void processFinish(ArrayList<String> output, ArrayList<String> outputIds);
        }

        public AsyncResponse delegate = null;

        public getKabs(AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            delegate.processFinish(lokasi, ids);
        }
    }
}
