package zw.co.vokers.vinceg.boutiquor.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import zw.co.vokers.vinceg.boutiquor.AppController;
import zw.co.vokers.vinceg.boutiquor.R;
import zw.co.vokers.vinceg.boutiquor.activities.CartActivity;
import zw.co.vokers.vinceg.boutiquor.models.Beer;
import zw.co.vokers.vinceg.boutiquor.models.ShoppingCartItem;

/**
 * Created by Vince G on 16/1/2019
 */

public class DoroDetailFragment extends Fragment {
    TextView mStuffName, mStuffDesc, mStuffCategory, mStuffPrice;
    Button mButtonAdd;
    ImageView mImageView;
    Beer beer;
    final private String TAG = "FoodDetail";
    CollapsingToolbarLayout collapsingToolbarLayout;


    View view;

    public DoroDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_food_detail, container, false);

        initView();
        initBeerInfo();

        setButtonListener();


        return view;
    }

    private void initView(){
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.food_detail_collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Beer Name");
        mStuffName = (TextView) view.findViewById(R.id.beer_name);
        mStuffDesc = (TextView) view.findViewById(R.id.beer_desc);
        mStuffCategory = (TextView) view.findViewById(R.id.beer_category);
        mStuffPrice = (TextView) view.findViewById(R.id.beer_price);
        mButtonAdd = (Button) view.findViewById(R.id.food_detail_add);
        mImageView = (ImageView) view.findViewById(R.id.food_detail_image);
    }

    private void initBeerInfo(){
        beer = new Beer();
        beer.setmId(getArguments().getInt("id"));
        beer.setmName(getArguments().getString("name"));
        beer.setmCategory(getArguments().getString("category"));
        beer.setmPrice(getArguments().getDouble("price"));
        beer.setmDesc(getArguments().getString("description"));
        beer.setmImageUrl(getArguments().getString("imageUrl"));

        mStuffName.setText(beer.getmName());
        mStuffCategory.setText(beer.getmCategory());
        mStuffDesc.setText(beer.getmDesc());
        mStuffPrice.setText(String.valueOf(beer.getmPrice()));
        collapsingToolbarLayout.setTitle(beer.getmName());

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(getArguments().getString("imageUrl"), new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    beer.setmImage(response.getBitmap());
                    mImageView.setImageBitmap(beer.getmImage());
                }
            }
        });
    }

    private void setButtonListener(){
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShoppingCartItem.getInstance(getContext()).addToCart(beer);
                TextView cartNumber = (TextView)getActivity().findViewById(R.id.cart_item_number);
                cartNumber.setText(String.valueOf(ShoppingCartItem.getInstance(getContext()).getSize()));

                new AlertDialog.Builder(getActivity()).setTitle("Successful!").setIcon(
                        android.R.drawable.ic_dialog_info)
                        .setMessage("Added 1 " + beer.getmName() + " to cart!")
                        .setPositiveButton("Go to cart", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent cartAct = new Intent(getActivity(), CartActivity.class);
                                startActivity(cartAct);
                            }
                        })
                        .setNegativeButton("Continue", null).show();
            }
        });
    }

}
