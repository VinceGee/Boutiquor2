package zw.co.vokers.vinceg.boutiquor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;

import java.util.HashMap;

import zw.co.vokers.vinceg.boutiquor.R;
import zw.co.vokers.vinceg.boutiquor.db.SQLiteHandler;
import zw.co.vokers.vinceg.boutiquor.db.SessionManager;
import zw.co.vokers.vinceg.boutiquor.fragments.HelpFragment;
import zw.co.vokers.vinceg.boutiquor.fragments.HistoryFragment;
import zw.co.vokers.vinceg.boutiquor.fragments.HomeFragment;
import zw.co.vokers.vinceg.boutiquor.fragments.ProfileFragment;
import zw.co.vokers.vinceg.boutiquor.fragments.TrackFragment;
import zw.co.vokers.vinceg.boutiquor.models.SPManipulation;
import zw.co.vokers.vinceg.boutiquor.models.ShoppingCartItem;

/**
 * Created by Vince G on 16/1/2019
 */

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        ResultCallback<People.LoadPeopleResult> {

    private static ProgressDialog pDialog;
    GoogleApiClient mGoogleApiClient;
    boolean mSignInClicked;
    private SQLiteHandler db;
    private SessionManager session;
    String name, email,mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());


        if (!session.isLoggedIn()) {
            logoutUser();
        }

        init();

    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        LoginManager.getInstance().logOut();
        if (mGoogleApiClient.isConnected()) {
//                    mGoogleApiClient.disconnect();
//                    // updateUI(false);
//                    System.err.println("LOG OUT ^^^^^^^^^^^^^^^^^^^^ SUCESS");
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Launching the login activity
        Intent intent = new Intent(HomePageActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    public static TextView cartNumber;

    private void init(){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        cartNumber = (TextView) findViewById(R.id.cart_item_number);
        cartNumber.setText(String.valueOf(ShoppingCartItem.getInstance(this).getSize()));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this, CartActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView header_mobile = (TextView) navHeaderView.findViewById(R.id.nav_mobile);
        TextView header_name = (TextView) navHeaderView.findViewById(R.id.nav_name);
        //header_name.setText(SPManipulation.getInstance(this).getName());
        //header_mobile.setText(SPManipulation.getInstance(this).getEmail());

        HashMap<String, String> user = db.getUserDetails();

        name = user.get("fullname");
        email = user.get("email");
        mobile = user.get("mobile");
        header_name.setText(name);
        header_mobile.setText(mobile);

        if(findViewById(R.id.main_fragment_container) != null) {
            HomeFragment homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, homeFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.nav_home:
                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.main_fragment_container, homeFragment).commit();
                break;
            case R.id.nav_addr:
                break;
            case R.id.nav_profile:
                ProfileFragment profileFragment = new ProfileFragment();
                transaction.replace(R.id.main_fragment_container, profileFragment).commit();
                break;
            case R.id.nav_history:
                HistoryFragment historyFragment = new HistoryFragment();
                transaction.replace(R.id.main_fragment_container, historyFragment).commit();
                break;
            case R.id.nav_track:
                startActivity(new Intent(getApplicationContext(), TrackActivity.class));
                break;
                /*TrackFragment trackFragment = new TrackFragment();
                transaction.replace(R.id.main_fragment_container, trackFragment).commit();
                break;*/
            case R.id.nav_help:
                HelpFragment helpFragment = new HelpFragment();
                transaction.replace(R.id.main_fragment_container, helpFragment).commit();
                break;
            case R.id.nav_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
            case R.id.nav_logout:
                SPManipulation.getInstance(this).clearSharedPreference();
                logoutUser();
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public static void showPDialog(){
        if (!pDialog.isShowing()){
            pDialog.show();
        }
    }
    public static void disPDialog(){
        if (pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("LOGOUT", "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mSignInClicked = false;

        // updateUI(true);
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {

    }


}

