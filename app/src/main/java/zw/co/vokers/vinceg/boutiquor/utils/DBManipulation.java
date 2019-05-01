package zw.co.vokers.vinceg.boutiquor.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import zw.co.vokers.vinceg.boutiquor.models.Beer;
import zw.co.vokers.vinceg.boutiquor.models.ShoppingCartItem;

import static zw.co.vokers.vinceg.boutiquor.utils.DBHelper.CATEGORY;
import static zw.co.vokers.vinceg.boutiquor.utils.DBHelper.IMAGEURL;
import static zw.co.vokers.vinceg.boutiquor.utils.DBHelper.ITEMID;
import static zw.co.vokers.vinceg.boutiquor.utils.DBHelper.ITEMNAME;
import static zw.co.vokers.vinceg.boutiquor.utils.DBHelper.PRICE;
import static zw.co.vokers.vinceg.boutiquor.utils.DBHelper.QUANTITY;
import static zw.co.vokers.vinceg.boutiquor.utils.DBHelper.TABLENAME;

/**
 * Created by Vince G on 16/1/2019
 */

public class DBManipulation {
    private DBHelper mDBHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private Context mContext;
    private static String mDBName;
    private static DBManipulation mInstance;

    public DBManipulation(Context context) {
        mDBName = DBHelper.DATABASE_NAME;
        this.mContext = context;
        mDBHelper = new DBHelper(context);
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
    }

    public SQLiteDatabase getDb(){
        return mSQLiteDatabase;
    }

    public static DBManipulation getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBManipulation(context);
        }
        return mInstance;
    }


    public void addAll(){
        ArrayList<Integer> foodsList = ShoppingCartItem.getInstance(mContext).getFoodInCart();
        for (Integer i : foodsList){
            Beer curBeer = ShoppingCartItem.getInstance(mContext).getFoodById(i);

            String insertCurrentFood = "INSERT INTO " + TABLENAME + "("
                    + ITEMNAME + ","
                    + QUANTITY + ","
                    + PRICE + ","
                    + CATEGORY + ","
                    + IMAGEURL
                    + ")"
                    + "VALUES ("
                    + "'" + curBeer.getmName() + "'" + ","
                    + ShoppingCartItem.getInstance(mContext).getFoodNumber(curBeer) + ","
                    + curBeer.getmPrice() + ","
                    + "'" + curBeer.getmCategory() + "'" + ","
                    + "'" + curBeer.getmImageUrl() + "'"
                    + ")";
            Log.e("DB", "insert value query: " + insertCurrentFood);
            mSQLiteDatabase.execSQL(insertCurrentFood);
        }
    }


    public void deleteAll(){
        mSQLiteDatabase.execSQL("delete from "+ TABLENAME);
        Log.e("DB", "Delete all: " + "delete from "+ TABLENAME);
    }
}
