package com.mobileagro.demo1;

import android.os.AsyncTask;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

import com.mobileagro.Tab2;
import com.mobileagro.globalKab;

import com.example.user.mana_livechatv2.R;
import com.mobileagro.reborn.LahanNewActivity;

/**
 * Created by ThinkPad T440s VPro on 17/07/2016.
 */
public class fetchDataLahan {
    public static GoogleMap map;
    public static TextView tv1;
    public static void setGoogleMap(GoogleMap gMap) {
        map = gMap;
    }
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
                            LahanNewActivity.kabId = id;
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
            setMarkers();
            if (lokasi.equals("")) {
                lokasi = "Posisi tidak dapat diketahui";
            }
            tv1.setText(lokasi);
            Tab2.CurrentKabId = KabId;
        }

        private void setMarkers() {
            if (lats.size()>0) {
                Double[] latArray = new Double[lats.size()];
                latArray = lats.toArray(latArray);
                Double[] lngArray = new Double[lngs.size()];
                lngArray = lngs.toArray(lngArray);

                String[] komArray = new String[komoditases.size()];
                komArray = komoditases.toArray(komArray);
                Double[] latAdd = {0.020, 0.010, 0.020, 0.020, 0.010, 0.000};
                Double[] lngAdd = {0.000, 0.020, 0.010, 0.000, 0.000, 0.020};
                LatLng latLng = null;

                for (int i = 0; i < latArray.length; i++) {
                    latLng = new LatLng(latArray[i] + latAdd[i], lngArray[i] + lngAdd[i]);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(komArray[i]);
                    String Kategori = komArray[i];
                    if (Kategori.equals("JAGUNG")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.jagung_marker));
                    } else if (Kategori.equals("PADI SAWAH")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.padi_marker));
                    } else if (Kategori.equals("KEDELAI")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.kedelai_marker));
                    } else {
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    }
                    map.addMarker(markerOptions);
                }

                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
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


