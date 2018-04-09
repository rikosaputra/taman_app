package com.mobileagro.demo1;

import android.os.AsyncTask;

import com.mobileagro.VarietasDetail;

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

/**
 * Created by ThinkPad T440s VPro on 02/09/2016.
 */
public class fetchVarietasDetail {
    public static String NamaVarietas;
    public static String Keterangan;
    public static String ImgId;
    public static String Komditas;
    public static String VarietasId;
    public static class getVarietas extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String postParameters = "name=" + params[0];
            String readStream = "";
            try {
                URL url = new URL("http://202.56.170.37/mobile-agro/varietas/varietas_name.php");
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
                        String namaVarietas = jsonarray.getString(0);
                        String keterangan = jsonarray.getString(3);
                        String imgId = jsonarray.getString(4);
                        String komoditas = jsonarray.getString(6);
                        String varietasId = jsonarray.getString(7);
                        NamaVarietas = "Varietas: " + namaVarietas;
                        Keterangan = keterangan;
                        ImgId = imgId;
                        Komditas = komoditas;
                        VarietasId = varietasId;
                        /*
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObj = jsonarray.getJSONObject(i);
                            String namaVarietas = jsonObj.getString("nama_varietas");
                            String keterangan = jsonObj.getString("varietas_rekomendasi");
                            NamaVarietas = namaVarietas;
                            Keterangan = keterangan;
                        }
                        */
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

        public getVarietas (AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish();
        }
     }
    }
