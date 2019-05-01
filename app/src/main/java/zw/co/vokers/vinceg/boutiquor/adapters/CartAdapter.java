package zw.co.vokers.vinceg.boutiquor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import zw.co.vokers.vinceg.boutiquor.R;
import zw.co.vokers.vinceg.boutiquor.activities.HomePageActivity;
import zw.co.vokers.vinceg.boutiquor.fragments.CartFragment;
import zw.co.vokers.vinceg.boutiquor.models.Beer;
import zw.co.vokers.vinceg.boutiquor.models.ShoppingCartItem;

/**
 * Created by Vince G on 16/1/2019
 */

public class CartAdapter extends RecyclerView.Adapter<CartHolder> implements View.OnClickListener{
    private Context mContext;
    private final String TAG = "ADAPTER";

    public CartAdapter(Context context) {
        mContext = context;
    }

    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cardview_cart, parent,false);
        CartHolder cartHolder = new CartHolder(view);
        return cartHolder;
    }

    @Override
    public void onBindViewHolder(final CartHolder holder, int position) {

        int id = ShoppingCartItem.getInstance(mContext).getFoodInCart().get(position);
        final Beer curBeer = ShoppingCartItem.getInstance(mContext).getFoodById(id);
        final int curFoodNumber = ShoppingCartItem.getInstance(mContext).getFoodNumber(curBeer);

        holder.mTextName.setText(curBeer.getmName());
        holder.mTextCategory.setText(curBeer.getmCategory());
        holder.mEditQuantity.setText(String.valueOf(curFoodNumber));
        holder.mTextPrice.setText(String.valueOf(curFoodNumber * curBeer.getmPrice()));
        holder.mImage.setImageBitmap(curBeer.getmImage());

        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curFoodNumber == 0){
                    return;
                }
                int currentNumber = curFoodNumber - 1;
                ShoppingCartItem.getInstance(mContext).setFoodNumber(curBeer, currentNumber);


                HomePageActivity.cartNumber.setText(String.valueOf(ShoppingCartItem.getInstance(mContext).getSize()));
                CartFragment.cart_total.setText(String.valueOf(ShoppingCartItem.getInstance(mContext).getPrice()));
                notifyDataSetChanged();
            }
        });

        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curFoodNumber == 99){
                    return;
                }
                int currentNumber = curFoodNumber + 1;
                ShoppingCartItem.getInstance(mContext).setFoodNumber(curBeer, currentNumber);

                HomePageActivity.cartNumber.setText(String.valueOf(ShoppingCartItem.getInstance(mContext).getSize()));
                CartFragment.cart_total.setText(String.valueOf(ShoppingCartItem.getInstance(mContext).getPrice()));
                notifyDataSetChanged();
            }
        });

    }

    public void deleteData(int position) {
        int id = ShoppingCartItem.getInstance(mContext).getFoodInCart().get(position);
        Beer curBeer = ShoppingCartItem.getInstance(mContext).getFoodById(id);
        ShoppingCartItem.getInstance(mContext).setFoodNumber(curBeer, 0);


        HomePageActivity.cartNumber.setText(String.valueOf(ShoppingCartItem.getInstance(mContext).getSize()));
        CartFragment.cart_total.setText(String.valueOf(ShoppingCartItem.getInstance(mContext).getPrice()));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ShoppingCartItem.getInstance(mContext).getFoodTypeSize();
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

class CartHolder extends RecyclerView.ViewHolder {
    TextView mTextName, mTextCategory, mTextPrice;
    ImageView mImage;
    TextView mEditQuantity;
    Button btn_minus;
    Button btn_plus;
    TextView total_price;
    public CartHolder(View itemView) {
        super(itemView);
        mTextName = (TextView) itemView.findViewById(R.id.cart_name);
        mTextCategory = (TextView) itemView.findViewById(R.id.cart_category);
        mTextPrice = (TextView) itemView.findViewById(R.id.cart_price);
        mEditQuantity = (TextView) itemView.findViewById(R.id.cart_quantity);
        mImage = (ImageView) itemView.findViewById(R.id.cart_image);


        btn_minus = (Button) itemView.findViewById(R.id.cart_btn_minus);
        btn_plus = (Button) itemView.findViewById(R.id.cart_btn_plus);



    }
}

