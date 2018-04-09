package com.mobileagro.reborn;

import android.os.AsyncTask;
import android.widget.TextView;

import com.mobileagro.Tab2;
import com.mobileagro.demo1.globalLahan;
import com.mobileagro.globalKab;

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
 * Created by ThinkPad T440s VPro on 10/10/2016.
 */

public class fetchLocationData {

    public static TextView tv1;
    public static void  setTv1(TextView tv) { tv1 = tv; }
    public static class getPositions extends AsyncTask<String, Void, String > {

        ArrayList<Double> lats = new ArrayList<>();
        ArrayList<Double> lngs = new ArrayList<>();
        ArrayList<String> komoditases = new ArrayList<>();
        ArrayList<String> persenKenas = new ArrayList<>();
        ArrayList<String> avgTanams = new ArrayList<>();
        String lokasi = "";
        String KabId;
        @Override
        protected void onPreExecute() {
            tv1.setText("Menentukan Lokasi, Harap Tunggu...");
        }

        @Override
        protected String doInBackground(String... params) {
            globalKab gKab = globalKab.getInstance();
            globalLahan gLahan = globalLahan.getInstance();
            String postParameters = "lat=" + params[0] + "&lng=" + params[1];
            String readStream = "";
            try {
                URL url = new URL("http://202.56.170.37/mobile-agro/varietas/varietas_kab.php");
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
                            String id = jsonObj.getString("kab_id");
                            String kabupaten = jsonObj.getString("kabupaten");

                            String komoditas = jsonObj.getString("komoditas");
                            String persenKena = jsonObj.getString("persentase_terkena");
                            String avgTanam = jsonObj.getString("luas_potensi_tanam_avg");
                            Double lat = jsonObj.getDouble("lat");
                            Double lng = jsonObj.getDouble("lng");

                            lats.add(lat);
                            lngs.add(lng);
                            komoditases.add(komoditas);
                            persenKenas.add(persenKena);
                            avgTanams.add(avgTanam);

                            String provinsi = jsonObj.getString("provinsi");
                            String pulau = jsonObj.getString("pulau");
                            lokasi = kabupaten +", "+provinsi+", "+ pulau;
                            KabId = id;
                            System.out.println("Set KABID = " + KabId);

                            gKab.setData(Integer.parseInt(id));
                        }
                        gKab.setName(lokasi);
                        gLahan.setKom(komoditases);
                        gLahan.setPersen(persenKenas);
                        gLahan.setAvg(avgTanams);
                    }
                } catch (JSONException ep) {
                    ep.printStackTrace();
                }
            } catch (Exception ep) {
                System.out.println("Failed: " + ep);
            }
            return readStream;
        }
        @Override
        protected void onPostExecute(String unused) {

            if (lokasi.equals("")) {
                lokasi = "Posisi tidak dapat diketahui";
            }
            tv1.setText(lokasi);
            Tab2.CurrentKabId = KabId;
        }


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
}
