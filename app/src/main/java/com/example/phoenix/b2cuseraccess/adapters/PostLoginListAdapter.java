package com.example.phoenix.b2cuseraccess.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phoenix.b2cuseraccess.R;
import com.example.phoenix.b2cuseraccess.models.Product;

import java.util.ArrayList;

/**
 * Created by Phoenix on 13-Aug-17.
 */

public class PostLoginListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private ArrayList<Product> products;
    TextView titleText;
    EditText priceText, discountText;
    ImageView plImage;
    CheckBox plCheckBox;

    public PostLoginListAdapter(Context context, ArrayList<Product> products) {
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
            view = inflater.inflate(R.layout.item_postlogin, null);
            setFieldValues(view, position);
        }
        return view;
    }

    private void setFieldValues(View view, int position) {
        titleText = (TextView) view.findViewById(R.id.titlePLoginItem);
        plImage = (ImageView) view.findViewById(R.id.imageViewPLogin);
        priceText = (EditText) view.findViewById(R.id.priceEditText);
        discountText = (EditText) view.findViewById(R.id.discountEditText);
        plCheckBox = (CheckBox) view.findViewById(R.id.checkBoxPLogin);
        Product product = products.get(position);
        titleText.setText(product.getName());
        plImage.setImageResource(product.getImageResId());
        priceText.setText(String.valueOf(product.getPrice()));
        plCheckBox.setChecked(false);
    }
}
