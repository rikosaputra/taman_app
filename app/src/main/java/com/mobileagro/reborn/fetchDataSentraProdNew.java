package com.mobileagro.reborn;

import android.os.AsyncTask;
import android.widget.TextView;

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
 * Created by ThinkPad T440s VPro on 18/10/2016.
 */

public class fetchDataSentraProdNew {

    public static TextView tv1;
    public static ArrayList<String> id_komoditases = new ArrayList<>();
    public static ArrayList<String> komoditases = new ArrayList<>();
    public static ArrayList<String> prods = new ArrayList<>();
    public static ArrayList<String> months = new ArrayList<>();

    public static void  setTv1(TextView tv) { tv1 = tv; }
    public static class getSentraProds extends AsyncTask<String, Void, String > {

        String lokasi = "";
        @Override
        protected void onPreExecute() {
            tv1.setText("Menentukan Lokasi, Harap Tunggu...");
        }
        @Override
        protected void onPostExecute(String result) {
            if (lokasi.equals("")) {
                lokasi = "Posisi tidak dapat diketahui";
            }
            tv1.setText(lokasi);
            this.delegate.processSentraFinish();
        }
        @Override
        protected String doInBackground(String... params) {
            id_komoditases.clear();
            komoditases.clear();
            prods.clear();
            months.clear();

            String postParameters = "lat=" + params[0] + "&lng=" + params[1];
            String readStream = "";
            try {
                URL url = new URL("http://202.56.170.37/mobile-agro/varietas/varietas_kab.php");
                System.out.println("SENTRA PRODUKSI ------------ " + postParameters);
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
                            String kab_id = jsonObj.getString("kab_id");
                            String kabupaten = jsonObj.getString("kabupaten");
                            String provinsi = jsonObj.getString("provinsi");
                            String pulau = jsonObj.getString("pulau");
                            lokasi = kabupaten +", "+provinsi+", "+ pulau;
                            SentraNewActivity.kabId = kab_id;
                            VarietasAllNewActivity.KabId = kab_id;
                            /*
                            String bulan_data = jsonObj.getString("bln_data");
                            String jml_prod = jsonObj.getString("jml_prod");
                            String komoditas_id = jsonObj.getString("kom_id");
                            String nama_komoditas = jsonObj.getString("nama_komoditas");

                            months.add(bulan_data);
                            prods.add(jml_prod);
                            id_komoditases.add(komoditas_id);
                            komoditases.add(nama_komoditas);
                            */
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

        public AsyncResponse delegate = null;
        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processSentraFinish();
        }
        public getSentraProds(AsyncResponse delegate) {
            this.delegate = delegate;
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
