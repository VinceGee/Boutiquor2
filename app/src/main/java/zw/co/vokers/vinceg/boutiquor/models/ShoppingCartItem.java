package zw.co.vokers.vinceg.boutiquor.models;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import zw.co.vokers.vinceg.boutiquor.AppController;
import zw.co.vokers.vinceg.boutiquor.utils.AppConfig;
import zw.co.vokers.vinceg.boutiquor.utils.DBHelper;
import zw.co.vokers.vinceg.boutiquor.utils.DBManipulation;

import static zw.co.vokers.vinceg.boutiquor.utils.DBHelper.TABLENAME;

/**
 * Created by Vince G on 16/1/2019
 */

public class ShoppingCartItem {
    private static ArrayList<Integer> foodsId;
    private static HashMap<Beer, Integer> foodMap;
    private static ShoppingCartItem instance = null;
    private int totalNumber;
    private double totalPrice;

    ShoppingCartItem(){

        foodsId = new ArrayList<Integer>();
        foodMap = new HashMap<Beer, Integer>();
        totalNumber = 0;
        totalPrice = 0;
    }

    ShoppingCartItem(ArrayList<Integer> foodsId, HashMap<Beer, Integer> foodMap, int totalNumber, double totalPrice){
        this.foodsId = foodsId;
        this.foodMap = foodMap;
        this.totalNumber = totalNumber;
        this.totalPrice = totalPrice;
    }

    public void clear(){
        foodMap.clear();
        totalNumber = 0;
        totalPrice = 0.0;
        foodsId.clear();
    }

    public ArrayList<Integer> getFoodInCart(){
        return foodsId;
    }

    public void setNull(){
        instance = null;
    }

    public static synchronized ShoppingCartItem getInstance(Context context){
        if (instance == null){
            Cursor cursor = DBManipulation.getInstance(context).getDb()
                    .rawQuery("SELECT * FROM " + TABLENAME, null);
            if (cursor.getCount() > 0){
                final ArrayList<Integer> idList = new ArrayList<Integer>();
                final HashMap<Beer, Integer> foodInDb = new HashMap<Beer, Integer>();
                int totalNumberInDb = 0;
                int totalPriceInDb = 0;
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String curName = cursor.getString(cursor.getColumnIndex(DBHelper.ITEMNAME));
                    final int curId = Integer.valueOf(cursor.getString(cursor.getColumnIndex(DBHelper.ITEMID)));
                    final int curQuantity = Integer.valueOf(cursor.getString(cursor.getColumnIndex(DBHelper.QUANTITY)));
                    double curPrice = Integer.valueOf(cursor.getString(cursor.getColumnIndex(DBHelper.PRICE)));
                    String curCategory = cursor.getString(cursor.getColumnIndex(DBHelper.CATEGORY));
                    String curUrl = cursor.getString(cursor.getColumnIndex(DBHelper.IMAGEURL));

                    final Beer curBeer = new Beer();
                    curBeer.setmName(curName);
                    curBeer.setmPrice(curPrice);
                    curBeer.setmCategory(curCategory);
                    curBeer.setmImageUrl(curUrl);
                    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                    imageLoader.get(curBeer.getmImageUrl(), new ImageLoader.ImageListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("INIT FOOD", "Image Load Error: " + error.getMessage());
                        }
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                            if (response.getBitmap() != null) {
                                curBeer.setmImage(response.getBitmap());
                                idList.add(curId);
                                foodInDb.put(curBeer, curQuantity);
                            }
                        }
                    });
                    totalNumberInDb += curQuantity;
                    totalPriceInDb += (curBeer.getmPrice() * curQuantity);
                    cursor.moveToNext();
                }
                DBManipulation.getInstance(context).deleteAll();
                instance = new ShoppingCartItem(idList, foodInDb, totalNumberInDb, totalPriceInDb);
            }
            else {
                instance = new ShoppingCartItem();
            }
        }
        return instance;
    }

    public void addToCart(Beer beer){
        if (foodMap.containsKey(beer)){
            int curNum = foodMap.get(beer) + 1;
            foodMap.put(beer, curNum);
        }
        else {
            foodsId.add(beer.getmId());
            foodMap.put(beer, 1);
        }
        totalPrice += beer.getmPrice();
        totalNumber++;
    }

    public Beer getFoodById(int id){
        if (foodsId.contains(id)){
            Iterator it = foodMap.keySet().iterator();
            while (it.hasNext()){
                Beer curBeer = (Beer) it.next();
                int foodNumber = foodMap.get(curBeer);
                if (curBeer.getmId() == id){
                    return curBeer;
                }
            }
        }
        Log.e("GET FOOD BY ID", "No such item found");
        return null;
    }

    public void setFoodNumber(Beer beer, int number){
        int curNumber = foodMap.get(beer);
        int numberChange = Math.abs(curNumber - number);
        if (number > curNumber){
            totalNumber += numberChange;
            totalPrice += numberChange * beer.getmPrice();
        }
        else{
            totalNumber -= numberChange;
            totalPrice -= numberChange * beer.getmPrice();
        }
        if (number == 0){
            foodMap.remove(beer);
            for (int i=0; i<foodsId.size(); i++){
                if (foodsId.get(i) == beer.getmId()){
                    foodsId.remove((Object)foodsId.get(i));
                    break;
                }
            }
            return;
        }
        foodMap.put(beer, number);
    }

    public double getPrice(){
        return totalPrice;
    }

    public int getFoodNumber(Beer beer){
        if (foodMap.containsKey(beer)){
            return foodMap.get(beer);
        }
        return 0;
    }


    public int getSize(){
        return totalNumber;
    }

    public int getFoodTypeSize(){
        return foodsId.size();
    }


    public void placeOrder(String addr, String mobile, String clientName){
        Iterator it = foodMap.keySet().iterator();
        while (it.hasNext()){
            Beer curBeer = (Beer) it.next();
            int cnt = foodMap.get(curBeer);
            Log.e(curBeer.getmName(), "" + cnt);
            objRequestMethod(curBeer, cnt, addr, mobile, clientName);
        }
    }


    private void objRequestMethod(Beer curBeer, int cnt, String addr, String mobile, String clientName){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, buildUrl(curBeer, cnt, addr, mobile, clientName), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Place_Order", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("Place_Order", "ERROR" + volleyError.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private String buildUrl(Beer beer, int cnt, String addr, String mobile, String clientName) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new  Date(System.currentTimeMillis());
        String curTime = formatter.format(curDate);
        String tmpUrl = AppConfig.URL_PLACE_ORDER+ "?state=order"
                + "&order_category=" + beer.getmCategory()
                + "&order_name=" + beer.getmName()
                + "&order_quantity=" + cnt
                + "&total_order=" + (cnt * beer.getmPrice())
                + "&order_delivery_add=" + addr
                + "&order_date=" + curTime
                + "&order_client=" + clientName
                + "&user_phone=" + mobile;
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<tmpUrl.length(); i++){
            if (tmpUrl.charAt(i) == ' '){
                sb.append("%20");
            }
            else {
                sb.append(tmpUrl.charAt(i));
            }
        }
        Log.e("Build URL", sb.toString());
        return sb.toString();
    }

    public void addToDb(Context context){
        DBManipulation.getInstance(context).addAll();
    }
}
