package com.mobileagro.reborn.saprodi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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
import com.example.user.mana_livechatv2.TabbedActivity;
import com.example.user.mana_livechatv2.app.EndPoints;
import com.example.user.mana_livechatv2.app.MyApplication;
import com.example.user.mana_livechatv2.model.User;
import com.mobileagro.reborn.RegisterSaprodi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginSaprodi extends AppCompatActivity {
    EditText InputPassword;
    EditText InputName;
    CheckBox saveLoginCheck;
    private String TAG = RegisterSaprodi.class.getSimpleName();
    private ProgressDialog pDialog;
    private String kode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_saprodi);

        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Harap tunggu...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        InputName = (EditText) findViewById(R.id.input_name);
        InputPassword = (EditText) findViewById(R.id.input_password);
        saveLoginCheck = (CheckBox) findViewById(R.id.saveLoginCheckBox);

        if (MyApplication.getInstance().getPrefManager().getUser() != null) {
            Intent intent = new Intent(LoginSaprodi.this, SaprodiInfo.class);
            intent.putExtra("USERID", MyApplication.getInstance().getPrefManager().getUser().getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
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
    private void saveLogin() {

    }
    public void clickLoginSaprodi(View view) {
        if (saveLoginCheck.isChecked()) {
            saveLogin();
        }
        loginUser();
    }
    private void loginUser(){
        showDialog();
        RequestQueue queue = Volley.newRequestQueue(this);

        String tokenUrl = EndPoints.LOGIN;
        final String inputName = InputName.getText().toString();
        final String inputPassword = InputPassword.getText().toString();
        if (inputPassword!="") {
            StringRequest strRequest = new StringRequest(Request.Method.POST, tokenUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("Reponse = " + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("error") == false) {
                            Toast.makeText(getApplicationContext(), "Anda sudah berhasil login", Toast.LENGTH_LONG).show();

                            JSONObject userObj = obj.getJSONObject("user");
                            kode = userObj.getString("gcm_registration_id");
                            User user = new User(userObj.getString("user_id"),
                                    userObj.getString("name"),
                                    userObj.getString("email"),
                                    userObj.getString("fullname"),
                                    obj.getString("narasumber"));

                            MyApplication.getInstance().getPrefManager().storeUser(user);
                            hideDialog();

                            Intent intent =  new Intent(getBaseContext(),SaprodiInfo.class);
                            intent.putExtra("USERID", MyApplication.getInstance().getPrefManager().getUser().getId());
                            startActivity(intent);
                            // finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Username atau kata sandi anda tidak cocok", Toast.LENGTH_LONG).show();
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

                    params.put("name", inputName);
                    params.put("password", inputPassword);
                    return params;
                }
            };
            queue.add(strRequest);
        } else {
            Toast.makeText(getApplicationContext(), "Format input tidak sesuai", Toast.LENGTH_SHORT).show();
        }
    }
}
