package com.example.user.mana_livechatv2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.example.user.mana_livechatv2.app.EndPoints;
import com.example.user.mana_livechatv2.app.MyApplication;
import com.example.user.mana_livechatv2.model.User;

public class LoginActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();
    private EditText inputName, inputEmail, inputPassword;
    //    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private CheckBox saveLoginCheckBox;
    private Button btnLogin, btnToRegister;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private TextView resultText;
    private String kode;
    private boolean lanjut = false;
    private ProgressDialog pDialog;
    private ImageButton backButton;
    private String REFERER;
    View layer1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MultiDex.install(this);

        super.onCreate(savedInstanceState);

            /**
             * Check for login session. It user is already logged in
             * redirect him to main activity
             * */
            Bundle b = getIntent().getExtras();
            if (b != null) {
                REFERER = b.getString("REFERER");
            } else {
                REFERER = "";
            }

            if (MyApplication.getInstance().getPrefManager().getUser() != null) {
                if (REFERER.equals("ALSINTAN")) {
                    startActivity(new Intent(this, com.mobileagro.reborn.alsintan.alsintan_form.class));
                } else {
                    startActivity(new Intent(this, TabbedActivity.class));
                }
                finish();
            }

            setContentView(R.layout.activity_login);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
//        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
//        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
//        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
            inputName = (EditText) findViewById(R.id.input_name);
//        inputEmail = (EditText) findViewById(R.id.input_email);
            inputPassword = (EditText) findViewById(R.id.input_password);

            saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
            loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            loginPrefsEditor = loginPreferences.edit();
            layer1 = findViewById(R.id.includedLogin);
            btnLogin = (Button) findViewById(R.id.btn_login);
            btnToRegister = (Button) findViewById(R.id.btn_to_register);
            backButton = (ImageButton) findViewById(R.id.back_button_login);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
//        inputName.addTextChangedListener(new MyTextWatcher(inputName));
//        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));

            saveLogin = loginPreferences.getBoolean("saveLogin", false);
            if (saveLogin == true) {
                inputName.setText(loginPreferences.getString("name", ""));
                inputPassword.setText(loginPreferences.getString("password", ""));
                saveLoginCheckBox.setChecked(true);
            }

            btnLogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    login();
                }
            });

            btnToRegister.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(i);
                    finish();
                }
            });
    }
    private void login() {
        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Memasuki...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showDialog();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputName.getWindowToken(), 0);
//        if (!validateName()) {
//            return;
//        }
//
//        if (!validateEmail()) {
//            return;
//        }

        final String name = inputName.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();

        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("name", name);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

        // Check for empty data in the form
        if (!name.isEmpty() && !password.isEmpty()) {
            // login user
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    EndPoints.LOGIN, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "response: " + response);

                    try {
                        JSONObject obj = new JSONObject(response);

                        // check for error flag
                        if (obj.getBoolean("error") == false) {
                            // user successfully logged in

                            JSONObject userObj = obj.getJSONObject("user");
                            kode = userObj.getString("gcm_registration_id");
                            User user = new User(userObj.getString("user_id"),
                                    userObj.getString("name"),
                                    userObj.getString("email"),
                                    userObj.getString("fullname"),
                                    obj.getString("narasumber"));
                            if (kode.length() == 6) {
                                showPopupKode(kode, user);
                            }
                            else {
                                // storing user in shared preferences
                                MyApplication.getInstance().getPrefManager().storeUser(user);

                                // start main activity

                                if (REFERER.equals("ALSINTAN")) {
                                    startActivity(new Intent(getApplicationContext(), com.mobileagro.reborn.alsintan.alsintan_form.class));
                                } else {
                                    startActivity(new Intent(getApplicationContext(), TabbedActivity.class));
                                }
                                hideDialog();
                                finish();
                            }

                        } else {
                            // login error - simply toast the message
                            Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }

//                        // start main activity
//                        startActivity(new Intent(getApplicationContext(), TabbedActivity.class));
//                        finish();


                    } catch (JSONException e) {
                        Log.e(TAG, "json parsing error: " + e.getMessage());
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "Terjadi error, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    hideDialog();
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    Toast.makeText(getApplicationContext(), "Internet Anda bermasalah, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("password", password);

                    Log.e(TAG, "params: " + params.toString());
                    return params;
                }
            };

            //Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(strReq);
        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(),
                    "Data belum lengkap!", Toast.LENGTH_LONG).show();
            hideDialog();
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
    protected void showPopupKode(final String code, final User user) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(com.example.user.mana_livechatv2.LoginActivity.this);
        View promptView = layoutInflater.inflate(R.layout.popup_kode, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.isiPopup);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String input = "" + editText.getText();
                        if (input.equals(code)) {
                            lanjut = true;
                            // storing user in shared preferences
                            MyApplication.getInstance().getPrefManager().storeUser(user);

                            // start main activity
                            if (REFERER.equals("ALSINTAN")) {
                                startActivity(new Intent(getApplicationContext(), com.mobileagro.reborn.alsintan.alsintan_form.class));
                            } else {
                                startActivity(new Intent(getApplicationContext(), TabbedActivity.class));
                            }
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Kode yang Anda masukkan salah!", Toast.LENGTH_SHORT).show();
                            hideDialog();
                        }
                        //resultText.setText("Hello, " + editText.getText());
                    }
                })
                .setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }

//    // Validating name
//    private boolean validateName() {
//        if (inputName.getText().toString().trim().isEmpty()) {
//            inputLayoutName.setError(getString(R.string.err_msg_name));
//            requestFocus(inputName);
//            return false;
//        } else {
//            inputLayoutName.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    // Validating email
//    private boolean validateEmail() {
//        String email = inputEmail.getText().toString().trim();
//
//        if (email.isEmpty() || !isValidEmail(email)) {
//            inputLayoutEmail.setError(getString(R.string.err_msg_email));
//            requestFocus(inputEmail);
//            return false;
//        } else {
//            inputLayoutEmail.setErrorEnabled(false);
//        }
//
//        return true;
//    }

//    private static boolean isValidEmail(String email) {
//        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }

//    private class MyTextWatcher implements TextWatcher {
//
//        private View view;
//        private MyTextWatcher(View view) {
//            this.view = view;
//        }
//
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void afterTextChanged(Editable editable) {
//            switch (view.getId()) {
//                case R.id.input_name:
//                    validateName();
//                    break;
//                case R.id.input_email:
//                    validateEmail();
//                    break;
//            }
//        }
//    }

}
