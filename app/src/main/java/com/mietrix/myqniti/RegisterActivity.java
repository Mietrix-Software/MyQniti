package com.mietrix.myqniti;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button register;
    EditText name, phone, email, address, pass, confirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        register = findViewById(R.id.registerBtn);
        name = findViewById(R.id.enName);
        address = findViewById(R.id.enAddress);
        phone = findViewById(R.id.enPhone);
        email = findViewById(R.id.enEmail);
        pass = findViewById(R.id.enPass);
        confirmPass = findViewById(R.id.enConfirmPass);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nm = name.getText().toString().trim();
                final String em = email.getText().toString().trim();
                final String ph = phone.getText().toString().trim();
                final String adr = address.getText().toString().trim();
                final String pss = pass.getText().toString().trim();
                String conpss = confirmPass.getText().toString().trim();

                if (nm.length() < 5) {
                    Toast.makeText(getApplicationContext(), "Please enter minimum 5 characters for Name",
                            Toast.LENGTH_LONG).show();
                } else if (em.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Please enter proper email address",
                            Toast.LENGTH_LONG).show();
                } else if (ph.length() < 10) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter proper phone number", Toast.LENGTH_LONG).show();
                } else if (adr.length() < 10) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter proper address", Toast.LENGTH_LONG).show();
                } else if (pss.length() < 8) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter minimum 8 character for Password", Toast.LENGTH_LONG).show();
                } else if (!pss.equals(conpss)) {
                    Toast.makeText(getApplicationContext(),
                            "Your password does not match", Toast.LENGTH_LONG).show();
                } else {

                    final ProgressDialog loading = ProgressDialog.show(RegisterActivity.this, "Please Wait", "Contacting Server", false, false);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            Config.URL_API+"register.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            loading.dismiss();

                            if (response.equalsIgnoreCase("Success")) {

                                Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_LONG)
                                        .show();
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            } else if (response.equalsIgnoreCase("Exist")) {

                                Toast.makeText(RegisterActivity.this, "Email already exist", Toast.LENGTH_LONG)
                                        .show();
                            } else {

                                Toast.makeText(RegisterActivity.this, "Cannot Register.Please try again later", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(RegisterActivity.this, "No internet . Please check your connection",
                                        Toast.LENGTH_LONG).show();
                            } else {

                                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("username", nm);
                            params.put("userphone", ph);
                            params.put("useremail", em);
                            params.put("userpass", pss);
                            params.put("useraddr", adr);
                            return params;
                        }

                    };

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                }
            }
        });
    }
}
