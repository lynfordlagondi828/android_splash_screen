package com.example.lynford.android_ecommerce_project.helper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.lynford.android_ecommerce_project.R;
import com.example.lynford.android_ecommerce_project.app.AppController;
import com.example.lynford.android_ecommerce_project.model.Cart;
import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {


    Handler handler = null;
    int quantity = 1;

    private ArrayList<Cart>list;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public CartAdapter(Activity activity, ArrayList<Cart> list) {
        this.list = list;
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i,  View view, ViewGroup viewGroup) {



        if (view == null){

            view = layoutInflater.inflate(R.layout.list_item_cart,null);
            handler = new Handler();

            handler.networkImageView = view.findViewById(R.id.pImage);
            handler.pname = view.findViewById(R.id.pName);
            handler.price = view.findViewById(R.id.pPrice);



            view.setTag(handler);

        } else handler = (Handler) view.getTag(); {

            final Cart cart = list.get(i);

            handler.networkImageView.setImageUrl(cart.getImage(),imageLoader);
            handler.pname.setText(cart.getName());
            handler.price.setText("" + cart.getPrice());

        }

        return view;
    }



    static class Handler{
        NetworkImageView networkImageView;
        TextView pname, price;

    }









}

