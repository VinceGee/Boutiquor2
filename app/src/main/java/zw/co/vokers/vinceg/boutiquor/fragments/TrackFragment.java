package zw.co.vokers.vinceg.boutiquor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import zw.co.vokers.vinceg.boutiquor.AppController;
import zw.co.vokers.vinceg.boutiquor.R;
import zw.co.vokers.vinceg.boutiquor.activities.HomePageActivity;
import zw.co.vokers.vinceg.boutiquor.models.Order;
import zw.co.vokers.vinceg.boutiquor.utils.AppConfig;

/**
 * Created by Vince G on 16/1/2019
 */

public class TrackFragment extends Fragment {
    // LinearLayout Property: no data pass, invisible; get data from track, visible; after search, visible
    private LinearLayout mLinearLayout;
    // search part
    private EditText mEditOrderSearch;
    private TextView mTextSearch;
    // display part
    private TextView mTextID, mTextDate, mTextTotal, mTextStatus,mTextAddress;
    private ImageView mImageStatus;
    private View fragView;

    private String orderId; private Button tracker;
    private Order order;

    private final int[] imageResources = {R.mipmap.pack, R.mipmap.delivery, R.mipmap.fork, R.mipmap.alert};
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Track");
    }

    public TrackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Teliver.init(getActivity(),"48cdf4bd2d82f339b22c7a227fc583bd");
        // Inflate the layout for this fragment


        fragView = inflater.inflate(R.layout.fragment_track, container, false);


        mEditOrderSearch = (EditText) fragView.findViewById(R.id.track_edit_search);
        mTextSearch = (TextView) fragView.findViewById(R.id.track_search);

        order = new Order();

        mLinearLayout = (LinearLayout) fragView.findViewById(R.id.track_detail_block);
        mTextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    orderId = mEditOrderSearch.getText().toString().trim();
                    mLinearLayout.setVisibility(View.VISIBLE);
                    getData(fragView);
                    /*--------insert code to get data---*/
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ERROR", e.toString());
                    Toast.makeText(getActivity(), "Wrong Id Format. Please Try Again!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        Bundle orderBundle = this.getArguments();
        if (orderBundle != null) {
            orderId = orderBundle.getString("ordernumber");
            getData(fragView);
            mLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mLinearLayout.setVisibility(View.INVISIBLE);
        }
        return fragView;
    }

    private void getData(final View view){
        final String TAG = "TRACK_FRAGMENT";
        HomePageActivity.showPDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, buildUrl(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG, jsonObject.toString());

                try{
                    JSONArray orderArray = jsonObject.getJSONArray("sorders");
                    for (int i = 0; i < orderArray.length(); i++) {
                        JSONObject c = orderArray.getJSONObject(i);

                        String ordernum = c.getString("ordernumber");
                        double totalOrder = c.getDouble("total_order");
                        String status = c.getString("order_status");
                        String address = c.getString("order_delivery_add");
                        String date = c.getString("order_date");
                        String complete_date = c.getString("order_completedate");

                        order.setOrderNum(ordernum);
                        order.setTotal(totalOrder);
                        order.setStatus(status);
                        order.setDate(date);
                        order.setmCompleteDate(complete_date);
                        order.setAddress(address);
                    }
                    displayData(view);
                }catch (Exception e){
                    System.out.println(e);
                }
                HomePageActivity.disPDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "ERROR" + volleyError.getMessage());
                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                HomePageActivity.disPDialog();
            }
        });
        Log.e("URL", jsonObjReq.getUrl());
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private String buildUrl() {
        return AppConfig.URL_SEARCH + orderId +"&state=search";
    }

    private void displayData(View view) {
        mTextID = (TextView) view.findViewById(R.id.track_id);
        mTextDate = (TextView) view.findViewById(R.id.track_date);
        mTextTotal = (TextView) view.findViewById(R.id.track_total);
        mTextAddress = (TextView) view.findViewById(R.id.track_address);
        mTextStatus = (TextView) view.findViewById(R.id.track_status);
        mImageStatus = (ImageView) view.findViewById(R.id.track_image_status);
        tracker = (Button) view.findViewById(R.id.trackerButton);
        /*------using parseWord to get the string, parseImage to get the resource id-----*/

        mTextID.setText(order.getOrderNum());
        mTextDate.setText(order.getDate());
        mTextTotal.setText("" + order.getTotal());
        mTextAddress.setText(order.getAddress());
        mTextStatus.setText(parseWord(order.getStatus()));
        mImageStatus.setImageResource(parseImage(order.getStatus()));
        returnTracker(order.getStatus());

        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), ClientTrackerActivity.class));
            }
        });
    }
    // STATUS: 0 - Preparing, 1 - En Route, 2 - Delivered
    private String parseWord(String s) {
        if (s.equals("0")) {
            return "Preparing";
        } else if (s.equals("1")) {
            return "En Route";
        } else if (s.equals("2")) {
            return "Delivered";
        } else {
            return "Error";
        }
    }

    private int parseImage(String s) {
        if (s.equals("0")) {
            return imageResources[0];
        } else if (s.equals("1")) {

            return imageResources[1];
        } else if (s.equals("2")) {
            return imageResources[2];
        } else {
            return imageResources[3];
        }
    }

    private void returnTracker(String s){
        if (s.equals("0")) {
            tracker.setVisibility(View.GONE);
        } else if (s.equals("1")) {
            tracker.setVisibility(View.VISIBLE);
        } else if (s.equals("2")) {
            tracker.setVisibility(View.GONE);
        } else {
            tracker.setVisibility(View.GONE);
        }
    }

}

