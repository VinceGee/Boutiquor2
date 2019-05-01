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

public class AllFoodAdapter extends RecyclerView.Adapter<AllHolder> implements View.OnClickListener{
    private Context mContext;
    ArrayList<Beer> beers;
    public String TAG = "ALLFOOD";

//
//    public AllFoodAdapter(Context context) {
//        mContext = context;
//    }

    public AllFoodAdapter(Context context, ArrayList<Beer> beers) {
        mContext = context;
        this.beers = beers;
    }

    @Override
    public AllHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_food, parent, false);
        AllHolder allHolder = new AllHolder(v);
        v.setOnClickListener(this);
        return allHolder;
    }

    @Override
    public void onBindViewHolder(AllHolder holder, int position) {

        holder.mTextName.setText(beers.get(position).getmName());
        holder.mTextPrice.setText(String.valueOf(beers.get(position).getmPrice()));
        holder.mTextCategory.setText(beers.get(position).getmCategory());
        holder.mImageView.setImageBitmap(beers.get(position).getmImage());

        holder.itemView.setTag(beers.get(position).getmId());
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

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
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
class AllHolder extends RecyclerView.ViewHolder {
    NetworkImageView mImage;
    ImageView mImageView;
    TextView mTextId, mTextName, mTextCategory, mTextPrice;

    public AllHolder(View itemView) {
        super(itemView);
        // mImage = (NetworkImageView) itemView.findViewById(R.id.food_img);
        mImageView = (ImageView) itemView.findViewById(R.id.food_img);
        mTextName = (TextView) itemView.findViewById(R.id.food_name);
        mTextPrice = (TextView) itemView.findViewById(R.id.food_price);
        mTextCategory = (TextView) itemView.findViewById(R.id.food_category);
    }
}
