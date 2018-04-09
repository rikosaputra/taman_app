package com.mobileagro.reborn;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.mana_livechatv2.R;
import com.example.user.mana_livechatv2.app.EndPoints;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterSaprodi extends AppCompatActivity {
    EditText InputName;
    EditText InputEmail;
    EditText InputFullname;
    EditText InputPassword;
    EditText InputPassword2;

    private String TAG = RegisterSaprodi.class.getSimpleName();
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_saprodi);
        InputName = (EditText) findViewById(R.id.input_name);
        InputEmail = (EditText) findViewById(R.id.input_email);
        InputFullname = (EditText) findViewById(R.id.input_fullname);
        InputPassword = (EditText) findViewById(R.id.input_password);
        InputPassword2 = (EditText) findViewById(R.id.input_password2);
    }


    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
    public void clickResgister(View view) {

        registerUser();
    }
    private void registerUser(){
        showDialog();
        RequestQueue queue = Volley.newRequestQueue(this);

        String tokenUrl = EndPoints.REGISTER;
        final String inputName = InputName.getText().toString();
        final String inputEmail = InputEmail.getText().toString();
        final String inputFullname = InputFullname.getText().toString();
        final String inputPassword = InputPassword.getText().toString();
        final String inputPassword2 = InputPassword2.getText().toString();
        if (inputPassword.equals(inputPassword2)&&inputPassword!="") {
            StringRequest strRequest = new StringRequest(Request.Method.POST, tokenUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("Reponse = " + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("error") == false) {
                            Toast.makeText(getApplicationContext(), "Anda sudah berhasil registrasi", Toast.LENGTH_LONG).show();
                        } else {
                            System.out.println("Error Server Connection");
                            hideDialog();

                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "json parsing error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Invalid Server Response", Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }

                }

            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            NetworkResponse networkResponse = error.networkResponse;
                            Log.d(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                            hideDialog();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("username", inputName);
                    params.put("email", inputEmail);
                    params.put("fullname", inputFullname);
                    params.put("password", inputPassword);
                    return params;
                }
            };
            queue.add(strRequest);
        } else {
            Toast.makeText(getApplicationContext(), "Konfirmasi kata sandi tidak sesuai", Toast.LENGTH_SHORT).show();
        }
    }
}
