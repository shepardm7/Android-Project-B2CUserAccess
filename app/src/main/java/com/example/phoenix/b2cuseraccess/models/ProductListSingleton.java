package com.example.phoenix.b2cuseraccess.models;

import java.util.ArrayList;
import com.example.phoenix.b2cuseraccess.R;

/**
 * Created by Phoenix on 13-Aug-17.
 */

public class ProductListSingleton {

    private ArrayList<Product> products = new ArrayList<>();
    private static ProductListSingleton singletonObj = new ProductListSingleton();

    private ProductListSingleton() {
        products.add(new Product("Asus Zephyrus", R.drawable.asus_zephyrus, 2500));
        products.add(new Product("Oneplus 5", R.drawable.oneplus_5, 900));
        products.add(new Product("Samsung TV", R.drawable.tv, 1000));
        products.add(new Product("Razer Mamba", R.drawable.razer_mamba, 150.50));
    }

    public static ArrayList<Product> getProductList() {
        return  singletonObj.products;
    }

}
