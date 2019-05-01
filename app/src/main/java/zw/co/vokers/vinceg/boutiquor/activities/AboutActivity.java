package zw.co.vokers.vinceg.boutiquor.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import zw.co.vokers.vinceg.boutiquor.R;

/**
 * Created by Vince G on 3/3/2019
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        Toolbar toolbar = new Toolbar(this);
        toolbar.setTitle("About");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            toolbar.setElevation(8f);
        toolbar.setBackgroundColor(getPrimaryColor(this));
        toolbar.setTitleTextColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.TaskDescription taskDescription =
                    new ActivityManager.TaskDescription
                            (null, null, getPrimaryColor(this));
            this.setTaskDescription(taskDescription);
        }

        Element call = new Element().setTitle("Call Us");
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneCall(AboutActivity.this, getString(R.string.dev_phone));
            }
        });

        /*Element credits = new Element().setTitle("Credits");
        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView webView = new WebView(AboutActivity.this);
                String data = getResources().getString(R.string.creds);
                webView.loadData(data, "text/html; charset=utf-8", "UTF-8");
                new AlertDialog.Builder(AboutActivity.this)
                        .setTitle("Credits")
                        .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setView(webView)
                        .show();
            }
        });*/

        View aboutView = new AboutPage(this)
                .setDescription("Boutiquor was developed by Vokers.")
                .addEmail("info@vokers.co.zw")
                .addFacebook("VokersOfficial")
                .addGitHub("Vokers")
                .addPlayStore("zw.co.vokers.vinceg.boutiquor")
                .addTwitter("VokersOfficial")
                .addWebsite("http://www.vokers.co.zw")
                .addYoutube("UC5L2nul3OyZIBRhLKp9PuXA")
                .setImage(R.drawable.vokers_icon)
                .addItem(call)
                .create();
        aboutView.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.addView(toolbar);
        linearLayout.addView(aboutView);
        setContentView(linearLayout);

        final Drawable upArrow;
        if (Build.VERSION.SDK_INT >= 21) {
            upArrow = getResources().getDrawable(R.drawable.left_arrow);
            upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        } else
            upArrow = getResources().getDrawable(R.drawable.left);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }

    public static int getPrimaryColor(Context context) {
        TypedValue primaryColor = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, primaryColor, true);
        return primaryColor.data;
    }

    public static void phoneCall(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)); ///ACTION_CALL will actually call the person
        context.startActivity(intent);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

