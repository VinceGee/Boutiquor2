package zw.co.vokers.vinceg.boutiquor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zw.co.vokers.vinceg.boutiquor.AppController;
import zw.co.vokers.vinceg.boutiquor.R;
import zw.co.vokers.vinceg.boutiquor.db.SQLiteHandler;
import zw.co.vokers.vinceg.boutiquor.utils.AppConfig;

/**
 * Created by Vince G on 3/3/2019
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private EditText username, mobile, email, password, repassword, address, fullname;
    private Button btn_signUp;
    private TextView toSignIn;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        db = new SQLiteHandler(this);

        fullname = (EditText) findViewById(R.id.sign_up_fullname);
        username = (EditText) findViewById(R.id.sign_up_username);
        mobile = (EditText) findViewById(R.id.sign_up_mobile);
        email = (EditText) findViewById(R.id.sign_up_email);
        password = (EditText) findViewById(R.id.sign_up_pwd);
        repassword = (EditText) findViewById(R.id.sign_up_pwd2);
        address = (EditText) findViewById(R.id.sign_up_address);

        toSignIn = (TextView) findViewById(R.id.to_sign_in);
        pDialog = new ProgressDialog(this);

        toSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }
        });

        btn_signUp = (Button) findViewById(R.id.sign_up_btn);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!repassword.getText().toString().equals(password.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Passwords do not match. Please try again.", Toast.LENGTH_LONG).show();
                    return;
                }

                String inputName = fullname.getText().toString().trim();
                String inputUsername = username.getText().toString().trim();
                String inputMobile = mobile.getText().toString().trim();
                String inputEmail = email.getText().toString().trim();
                String inputPassword = password.getText().toString().trim();
                String inputAddress = address.getText().toString().trim();

                if (!inputName.isEmpty() && !inputUsername.isEmpty() && !inputMobile.isEmpty() && !inputEmail.isEmpty() && !inputPassword.isEmpty() && !inputAddress.isEmpty()){
                    registerUser(inputName, inputUsername, inputMobile, inputEmail, inputPassword, inputAddress);
                } else{
                    Toast.makeText(getApplicationContext(),
                            "Please enter all your details!", Toast.LENGTH_LONG)
                            .show();
                }



            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up:
                // Navigate to RegisterActivity
               /* Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);*/
                break;
            /*case R.id.resetPassword:
                // Navigate to RegisterActivity
                Intent intentReset = new Intent(getApplicationContext(), ResetPassword.class);
                startActivity(intentReset);
                break;*/
        }
    }

    private void registerUser(final String name, final String username, final String mobile, final String email,
                              final String password, final String address) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("mid");

                        JSONObject user = jObj.getJSONObject("user");
                        String unique = user.getString("unique_id");
                        String zita = user.getString("fullname");
                        String basa = user.getString("role");
                        String yuzanemi = user.getString("username");
                        String foni = user.getString("mobile");
                        String imaili = user.getString("email");
                        String pasiwedhi = user.getString("passw");
                        String salt = user.getString("salt");
                        String adhiresi = user.getString("address");
                        String created = user.getString("created");

                        // Inserting row in users table
                        db.addUser(unique,zita, basa, yuzanemi, foni, imaili, pasiwedhi, salt,  adhiresi, created);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try to login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("fullname", name);
                params.put("username", username);
                params.put("mobile", mobile);
                params.put("email", email);
                params.put("password", password);
                params.put("address", address);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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


