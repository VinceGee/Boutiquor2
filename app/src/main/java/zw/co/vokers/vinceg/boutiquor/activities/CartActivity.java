package zw.co.vokers.vinceg.boutiquor.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import zw.co.vokers.vinceg.boutiquor.R;
import zw.co.vokers.vinceg.boutiquor.fragments.CartFragment;

/**
 * Created by Vince G on 16/1/2019
 */

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        if(findViewById(R.id.cart_container) != null) {
            CartFragment cartFragment = new CartFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.cart_container, cartFragment).commit();
        }
    }
}

