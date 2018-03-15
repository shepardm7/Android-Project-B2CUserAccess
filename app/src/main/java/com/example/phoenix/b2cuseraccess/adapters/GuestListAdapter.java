package com.example.phoenix.b2cuseraccess.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import com.example.phoenix.b2cuseraccess.R;
import com.example.phoenix.b2cuseraccess.models.Product;

/**
 * Created by Phoenix on 13-Aug-17.
 */

public class GuestListAdapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater = null;
    private ArrayList<Product> products;
    TextView titleText, priceText;
    ImageView guestImage;
    CheckBox guestCheckBox;

    public GuestListAdapter(Context context, ArrayList<Product> products) {
        mContext = context;
        this.products = products;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_guest, null);
            setFieldValues(view, position);
        }
        return view;
    }

    private void setFieldValues(View view, int position) {
        titleText = (TextView) view.findViewById(R.id.titleGuestItem);
        guestImage = (ImageView) view.findViewById(R.id.imageViewGuest);
        priceText = (TextView) view.findViewById(R.id.priceTextView);
        guestCheckBox = (CheckBox) view.findViewById(R.id.checkBoxGuest);
        Product product = products.get(position);
        titleText.setText(product.getName());
        guestImage.setImageResource(product.getImageResId());
        priceText.setText(String.valueOf(product.getPrice()));
        guestCheckBox.setChecked(false);
    }
}
