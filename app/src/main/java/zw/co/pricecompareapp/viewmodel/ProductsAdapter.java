//File to display data from Server in ListView using an extended ArrayAdapter

package zw.co.pricecompareapp.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zw.co.pricecompareapp.R;
import zw.co.pricecompareapp.models.Item;

public class ProductsAdapter extends ArrayAdapter<Item> {

    public ProductsAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lst_item_result, parent, false);
        }
        // Lookup view for data population
        TextView tvSName = (TextView) convertView.findViewById(R.id.tvShopName);
        TextView tvSAddr = (TextView) convertView.findViewById(R.id.tvShopAddress);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
        // Populate the data into the template view using the data object
        tvSName.setText(item.shopName);
        tvSAddr.setText(item.shopAddress);
        tvPrice.setText(item.price.toString());
        // Return the completed view to render on screen
        return convertView;
    }

}
