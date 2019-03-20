package zw.co.pricecompareapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import zw.co.pricecompareapp.models.DataReceived;
import zw.co.pricecompareapp.models.Item;
import zw.co.pricecompareapp.viewmodel.ProductsAdapter;

public class FragmentResult extends Fragment {

    private static final String uploadurl = "https://pricecompareapp.000webhostapp.com/upload.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_result, container, false);
        //Display arraylist in the list view

        DataReceived dtr = (DataReceived) getArguments().getSerializable("dataReceived");
        String name = dtr.getDescription();
        ArrayList<Item> prices = dtr.getData();

        ListView lst = (ListView)view.findViewById(R.id.lstProduct);
        TextView tvPd = (TextView)view.findViewById(R.id.tvProduct);

        tvPd.setText(name);
        ProductsAdapter adapter = new ProductsAdapter(getActivity(), prices);
        if(prices != null) {
            lst.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
