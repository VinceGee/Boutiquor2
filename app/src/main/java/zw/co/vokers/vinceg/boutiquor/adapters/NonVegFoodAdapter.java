package zw.co.vokers.vinceg.boutiquor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import zw.co.vokers.vinceg.boutiquor.R;
import zw.co.vokers.vinceg.boutiquor.models.Beer;

/**
 * Created by Vince G on 16/1/2019
 */

public class NonVegFoodAdapter extends RecyclerView.Adapter<NonVegHolder> implements View.OnClickListener{
    private Context mContext;

    private ArrayList<Beer> beers;

    public NonVegFoodAdapter(Context context, ArrayList<Beer> beers) {
        mContext = context;
        this.beers = beers;
    }

    @Override
    public NonVegHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_food, parent, false);
        NonVegHolder nonVegHolder = new NonVegHolder(v);
        v.setOnClickListener(this);
        return nonVegHolder;
    }

    @Override
    public void onBindViewHolder(NonVegHolder holder, int position) {
        holder.mTextCategory.setVisibility(View.INVISIBLE);
        holder.mTextCateTitle.setVisibility(View.INVISIBLE);


        holder.mTextName.setText(beers.get(position).getmName());
        holder.mTextPrice.setText(String.valueOf(beers.get(position).getmPrice()));
        holder.mImageView.setImageBitmap(beers.get(position).getmImage());

        holder.itemView.setTag(beers.get(position).getmName());
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }


    public void notifyData(ArrayList<Beer> beers) {
//        Log.d("notifyData ", beers.size() + "");
        this.beers = beers;
        notifyDataSetChanged();
    }
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , String data);
    }

    private AllFoodAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(AllFoodAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view,String.valueOf(view.getTag()));
        }
        else{
            Log.e("CLICK", "ERROR");
        }
    }
}

class NonVegHolder extends RecyclerView.ViewHolder {
    NetworkImageView mImage;
    ImageView mImageView;
    TextView mTextId, mTextName, mTextCategory, mTextPrice, mTextCateTitle;

    public NonVegHolder(View itemView) {
        super(itemView);
        // mImage = (NetworkImageView) itemView.findViewById(R.id.food_img);
        mImageView = (ImageView) itemView.findViewById(R.id.food_img);
        mTextName = (TextView) itemView.findViewById(R.id.food_name);
        mTextPrice = (TextView) itemView.findViewById(R.id.food_price);
        mTextCategory = (TextView) itemView.findViewById(R.id.food_category);
        mTextCateTitle = (TextView) itemView.findViewById(R.id.food_category_title);
    }
}

