package com.mobileagro.demo1;

import android.os.AsyncTask;

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
 * Created by ThinkPad T440s VPro on 24/08/2016.
 */
public class fetchDataKomoditasVarietas {

    public static ArrayList<String> listVarietasRekom = new ArrayList<>();
    public static class fetchVarietas extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String postParameters = "id=" + params[0];
            String readStream = "";
            try {
                URL url = new URL("http://202.56.170.37/mobile-agro/varietas/keseuaian_varietas_list.php");
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
                            String tahunMusim = jsonObj.getString("tahun_musim");
                            String varietasRekomen = jsonObj.getString("varietas_rekomendasi");
                            listVarietasRekom.add(varietasRekomen);
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
        public interface AsyncResponse {
            void processFinish();
        }
        public AsyncResponse delegate = null;

        public fetchVarietas(AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish();
        }
    }
}

