package zw.co.vokers.vinceg.boutiquor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zw.co.vokers.vinceg.boutiquor.AppController;
import zw.co.vokers.vinceg.boutiquor.R;
import zw.co.vokers.vinceg.boutiquor.db.SQLiteHandler;
import zw.co.vokers.vinceg.boutiquor.db.SessionManager;
import zw.co.vokers.vinceg.boutiquor.models.SPManipulation;
import zw.co.vokers.vinceg.boutiquor.utils.AppConfig;
import zw.co.vokers.vinceg.boutiquor.utils.InputValidation;
import zw.co.vokers.vinceg.boutiquor.utils.VinceTextView;

/**
 * Created by Vince G on 16/1/2019
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private final AppCompatActivity activity = SignInActivity.this;
    private static final String TAG = SignInActivity.class.getSimpleName();
    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutPNumber;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText inputPNumber;
    private TextInputEditText inputPassword ;
    private AppCompatButton appCompatButtonLogin;
    private VinceTextView textViewLinkRegister;
    private AppCompatTextView textViewResetPassword;
    private InputValidation inputValidation;
    //private DatabaseHelper databaseHelper;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    public String searchkey;
    Button btn_signIn, mFbButtonSignIn;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;
    private View parent_view;
    private static final int RC_SIGN_IN = 007;
    LoginButton mLoginButton;
    SPManipulation mSPManipulation;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        fbGoogleSignIn();

        parent_view = findViewById(android.R.id.content);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        inputPNumber = (TextInputEditText) findViewById(R.id.usernameInput);

        inputPassword = (TextInputEditText) findViewById(R.id.passwordInput);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.loginButton);

        textViewLinkRegister = (VinceTextView) findViewById(R.id.sign_up);

        appCompatButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = inputPNumber.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!mobile.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(mobile, password);

                    //new checkLogin().execute("");
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter your credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        textViewLinkRegister.setOnClickListener(this);

        ((View) findViewById(R.id.sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
            }
        });



    }

    private void fbGoogleSignIn(){
        mFbButtonSignIn = (Button) findViewById(R.id.button_fb_sign_in);
        /*-----------google sign in---------------*/
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        /*---------------fb sign in-------------------*/
        mFbButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginButton.performClick();
            }
        });
        //mLoginButton = (LoginButton) view.findViewById(R.id.login_button);
        mLoginButton = new LoginButton(getApplicationContext());
        mLoginButton.setReadPermissions("email");

        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("fblogin", "success");
                final GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String id = object.getString("id");
                                    Log.e("fblogin", id);
                                    mSPManipulation = SPManipulation.getInstance(getApplicationContext());
                                    mSPManipulation.setMobile(id);
                                    Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i("fblogin", "fb log in error");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("GoogleSignInResult", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("GoogleSignInResult", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            // start anotger activity
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
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

    private void emptyInputEditText() {
        inputPNumber.setText(null);
        inputPassword.setText(null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("ConnectionResult", "onConnectionFailed:" + connectionResult);
    }


    private void checkLogin(final String mobile, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        session.setLogin(true);


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

                        SPManipulation.getInstance(getApplicationContext()).setName(user.getString("fullname"));
                        SPManipulation.getInstance(getApplicationContext()).setEmail(user.getString("email"));
                        SPManipulation.getInstance(getApplicationContext()).setAddress(user.getString("address"));

                        Intent intent = new Intent(SignInActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("password", password);

                return params;
            }

        };

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

