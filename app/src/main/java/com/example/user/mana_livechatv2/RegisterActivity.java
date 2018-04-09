package com.example.user.mana_livechatv2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.mana_livechatv2.app.EndPoints;
import com.example.user.mana_livechatv2.app.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by USER on 21/07/2016.
 */
public class RegisterActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();
    private EditText inputName, inputEmail, inputFullname, inputPassword, inputPassword2;
    //    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private Button btnToLogin, btnRegister;
    private ProgressDialog pDialog;
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputName = (EditText) findViewById(R.id.input_name);
        inputFullname = (EditText) findViewById(R.id.input_fullname);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputPassword2 = (EditText) findViewById(R.id.input_password2);

        btnToLogin = (Button) findViewById(R.id.btn_to_login);
        btnRegister = (Button) findViewById(R.id.btn_register);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                register();
            }
        });

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void register() {
//        if (!validateName()) {
//            return;
//        }
//

        final String name = inputName.getText().toString().trim();
        final String email = inputEmail.getText().toString().trim();
        final String fullname = inputFullname.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();
        final String password2 = inputPassword2.getText().toString().trim();

        // Check for empty data in the form
        if (!name.isEmpty() && !email.isEmpty() && !fullname.isEmpty() &&
                !password.isEmpty() && !password2.isEmpty()) {
            if (validateEmail(email)) {
                if (password.equals(password2)) {
                    pDialog.setMessage("Mendaftarkan...");
                    showDialog();

                    Random rd = new Random();
                    int kodeRegis = rd.nextInt(999999 - 100000) + 100000;
                    final String kode = Integer.toString(kodeRegis);

                    // login user
                    StringRequest strReq = new StringRequest(Request.Method.POST,
                            EndPoints.REGISTER, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "response: " + response);

                            try {
                                JSONObject obj = new JSONObject(response);

                                // check for error flag
                                if (obj.getBoolean("error") == false) {
                                    sendEmail(kode);
                                    hideDialog();
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                                    alertDialog.setTitle("Registrasi Anda berhasil!");
                                    alertDialog.setMessage("Kode verifikasi telah dikirimkan ke email Anda. " +
                                            "Silahkan cek email Anda dan masukkan kode tersebut pada saat pertama kali " +
                                            "masuk dengan akun Anda.");
                                    alertDialog.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                            finish();
                                        }
                                    });
                                    alertDialog.show();
                                } else {
                                    hideDialog();
                                    // login error - simply toast the message
                                    Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                                Toast.makeText(getApplicationContext(), "Terjadi error, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                            }

                            //                        LayoutInflater layoutInflater = LayoutInflater.from(RegisterActivity.this);
                            //                        View promptView = layoutInflater.inflate(R.layout.popup_sukses, null);
                            //                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(RegisterActivity.this);
                            //                        dlgAlert.setView(promptView);
                            //                        final TextView judul = (TextView) promptView.findViewById(R.id.judulSukses);
                            //                        final TextView teks = (TextView) promptView.findViewById(R.id.sukses);
                            //                        judul.setText("Selamat! Registrasi Anda berhasil");
                            //                        teks.setText("Kode verifikasi sudah dikirimkan ke email Anda. " +
                            //                                "Masukkan kode tersebut untuk bisa masuk dengan akun Anda.");
                            ////                        dlgAlert.setMessage("Kode verifikasi sudah dikirimkan ke email Anda. " +
                            ////                                "Masukkan kode tersebut untuk bisa masuk dengan akun Anda.");
                            ////                        dlgAlert.setTitle("Selamat! Registrasi Anda berhasil");
                            //                        dlgAlert.setPositiveButton("Ok",
                            //                                new DialogInterface.OnClickListener() {
                            //                                    public void onClick(DialogInterface dialog, int which) {
                            //                                        // start main activity
                            //                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            //                                        finish();
                            //                                    }
                            //                                });
                            //                        dlgAlert.setCancelable(true);
                            //                        dlgAlert.create().show();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;
                            Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "Internet Anda bermasalah, silahkan coba lagi",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", name);
                            params.put("email", email);
                            params.put("fullname", fullname);
                            params.put("password", password);
                            params.put("kode", kode);


                            Log.e(TAG, "params: " + params.toString());
                            return params;
                        }
                    };

                    //Adding request to request queue
                    MyApplication.getInstance().addToRequestQueue(strReq);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Password tidak sama!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Format email salah!", Toast.LENGTH_LONG).show();
            }
        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(),
                    "Data belum lengkap!", Toast.LENGTH_LONG).show();
        }

    }

    private boolean validateEmail(String email) {
        // String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // return email.matches(emailPattern);
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

    }

    private void sendEmail(String kode) {
        //Getting content for email
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();

        String subject = "Konfirmasi Registrasi MANA";
        String message = "Selamat! Registrasi Anda telah berhasil dengan username: " + name + "\n" +
                "Untuk bisa mengaktifkan akun Anda, masukkan kode di bawah saat pertama kali login\n";
        message += kode;
        message += "\n Terima kasih";

        //Log.d("send email","masuk method");
        //Creating SendMail object

        sendWebMail.sendEmail asyncTask = new sendWebMail.sendEmail();
        asyncTask.execute(email, subject, message);

        // SendEmail sm = new SendEmail(this, email, subject, message);

        //Executing sendmail to send email
        // sm.execute();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
