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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ThinkPad T440s VPro on 24/08/2016.
 */
public class fetchDataKomoditas {
    public static ArrayList<String> listVarietasRekom = new ArrayList<>();
    public static String Id, Deskripsi, Temperature, CurahHujan, Kelembaban, Drainase, Tekstur, BahanKasar, Kedalaman;
    public static class getKomoditas extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish();
        }

        @Override
        protected String doInBackground(String... params) {
            listVarietasRekom.clear();
            URL url = null;
            String readStream = "";
            String postParameters = "id=" + params[0];
            try {
                url = new URL("http://202.56.170.37/mobile-agro/varietas/komoditas.php");

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
                            String id = jsonObj.getString("id_komoditas");
                            String deskripsi = jsonObj.getString("deskripsi");
                            String temperature = jsonObj.getString("temperature");
                            String curah_hujan = jsonObj.getString("curah_hujan");
                            String kelembaban = jsonObj.getString("kelembaban");
                            String drainase = jsonObj.getString("drainase");
                            String tekstur = jsonObj.getString("tekstur");
                            String bahan_kasar = jsonObj.getString("bahan_kasar");
                            String kedalaman = jsonObj.getString("kedalaman");

                            Id = id;
                            Deskripsi = deskripsi;
                            Temperature = temperature;
                            CurahHujan = curah_hujan;
                            Kelembaban = kelembaban;
                            Drainase = drainase;
                            Tekstur = tekstur;
                            BahanKasar = bahan_kasar;
                            Kedalaman = kedalaman;
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

            /* <-- Fetch Varietas --> */

            try {
                String postParameters2 = "id=" + params[1] + "&komoditas=" + params[2];
                System.out.println("Read-Response KEDUA.. : " + postParameters2);

                String readStream2 = "";
                URL url2 = new URL("http://202.56.170.37/mobile-agro/varietas/varietas_llist.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url2.openConnection();
                urlConnection.setDoOutput(true);

                urlConnection.setRequestMethod("POST");
                urlConnection.setFixedLengthStreamingMode(postParameters2.getBytes().length);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(postParameters2);
                out.close();

                InputStream in = urlConnection.getInputStream();
                readStream2 = readStream(in);
                System.out.println("Read-Response KEDUA.. : " + readStream2);

                try {
                    JSONArray jsonarray2 = new JSONArray(readStream2);
                    // JSONObject jObj = new JSONObject(readStream2);

                    if (readStream2 != null) {

                        for (int i = 0; i < jsonarray2.length(); i++) {

                            String varietasRekomen = jsonarray2.getString(i);
                            listVarietasRekom.add(varietasRekomen);
                        }
                        /*
                        for (int i = 0; i < jsonarray2.length(); i++) {
                            JSONObject jsonObj2 = jsonarray2.getJSONObject(i);
                            String varietasRekomen = jsonObj2.getString("varietas_rekomendasi");

                            System.out.println("Nama Varietas: " + varietasRekomen);

                            listVarietasRekom.add(varietasRekomen);
                        }
                        */
                    }
                } catch (JSONException ep) {
                    ep.printStackTrace();
                }
            } catch (Exception ep) {
                System.out.println("Failed: " + ep);
            }
            /* </-- Fetch Varietas --> */

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
            void processFinish();
        }
        public AsyncResponse delegate = null;
        public getKomoditas(AsyncResponse delegate) {
            this.delegate = delegate;
        }
    }
}
