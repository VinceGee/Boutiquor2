package zw.co.vokers.vinceg.boutiquor.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import zw.co.vokers.vinceg.boutiquor.AppController;
import zw.co.vokers.vinceg.boutiquor.R;
import zw.co.vokers.vinceg.boutiquor.activities.HomePageActivity;
import zw.co.vokers.vinceg.boutiquor.adapters.AllFoodAdapter;
import zw.co.vokers.vinceg.boutiquor.models.Beer;
import zw.co.vokers.vinceg.boutiquor.utils.AppConfig;

/**
 * Created by Vince G on 16/1/2019
 */

public class AllTabFragment extends Fragment {

    private String TAG = "EVERYTHING";


    ArrayList<Beer> beers = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private AllFoodAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;



    public AllTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_tab, container, false);

        // Request Data From Web Service
        if (beers.size()==0){
            objRequestMethod();
        }


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_all);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AllFoodAdapter(getActivity(), beers);
        adapter.setOnItemClickListener(new AllFoodAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Bundle itemInfo = new Bundle();
                for (int i = 0; i< beers.size(); i++){
                    if (beers.get(i).getmId() == Integer.valueOf(data)) {
                        itemInfo.putInt("id", beers.get(i).getmId());
                        itemInfo.putString("name", beers.get(i).getmName());
                        itemInfo.putString("category", beers.get(i).getmCategory());
                        itemInfo.putString("description", beers.get(i).getmDesc());
                        itemInfo.putDouble("price", beers.get(i).getmPrice());
                        itemInfo.putString("imageUrl", beers.get(i).getmImageUrl());
                        break;
                    }

                }
                DoroDetailFragment doroDetailFragment = new DoroDetailFragment();
                doroDetailFragment.setArguments(itemInfo);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fragment_container, doroDetailFragment)
                        .addToBackStack(AllTabFragment.class.getName())
                        .commit();
            }
        });
        mRecyclerView.setAdapter(adapter);
        return view;
    }


    private void objRequestMethod(){
        HomePageActivity.showPDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, AppConfig.URL_EVERYTHING, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG, jsonObject.toString());

                try{
                    JSONArray foodsJsonArr = jsonObject.getJSONArray("Everything");
                    for (int i = 0; i < foodsJsonArr.length(); i++) {
                        JSONObject c = foodsJsonArr.getJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String category = c.getString("category");
                        String price = c.getString("price");
                        String description = c.getString("description");
                        String imageUrl = c.getString("imageUrl");
                        String image = c.getString("image");

                        final Beer curBeer = new Beer();
                        curBeer.setmCategory(category);
                        curBeer.setmName(name);
                        curBeer.setmDesc(description);
                        curBeer.setmPrice(Double.valueOf(price));
                        curBeer.setmImageUrl(imageUrl);
                        curBeer.setmId(Integer.valueOf(id));

                        beers.add(curBeer);
//                        Log.e("Current Beer", curBeer.getName());

                        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                        imageLoader.get(image, new ImageLoader.ImageListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "Image Load Error: " + error.getMessage());
                            }
                            @Override
                            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                if (response.getBitmap() != null) {
                                    curBeer.setmImage(response.getBitmap());
//                                    Log.e("SET IMAGE", curBeer.getName());
                                    adapter.notifyData(beers);
                                }
                            }
                        });
                        beers.get(i).setmImage(curBeer.getmImage());
                    }

                }catch (Exception e){
                    System.out.println(e);
                }
                HomePageActivity.disPDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "ERRROOORRR" + volleyError.getMessage());
                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                HomePageActivity.disPDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


}

