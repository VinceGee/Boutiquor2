package zw.co.vokers.vinceg.boutiquor.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zw.co.vokers.vinceg.boutiquor.R;

/**
 * Created by Vince G on 16/1/2019
 */

public class AddressFragment extends Fragment {


    public AddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adress, container, false);
    }

}

