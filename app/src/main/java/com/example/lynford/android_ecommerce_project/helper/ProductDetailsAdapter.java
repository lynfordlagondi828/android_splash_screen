package com.example.lynford.android_ecommerce_project.helper;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.lynford.android_ecommerce_project.R;
import com.example.lynford.android_ecommerce_project.activity.HomeActivity;
import com.example.lynford.android_ecommerce_project.app.AppController;
import com.example.lynford.android_ecommerce_project.model.Product;

import java.util.ArrayList;

public class ProductDetailsAdapter extends BaseAdapter {

    ArrayList<Product>list;
    private Activity activity;
    private LayoutInflater layoutInflater;

    private ProductDetailsAdapterListener listener;

    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ProductDetailsAdapter(Activity activity, ArrayList<Product> list, ProductDetailsAdapterListener listener) {
        this.list = list;
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
        this.listener = listener;


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
    public View getView(final int i, View view, ViewGroup viewGroup) {


        Handler handler = null;

        if (view == null){

            view = layoutInflater.inflate(R.layout.list_product_details_item,null);
            handler = new Handler();

            handler.pname = view.findViewById(R.id.productName);
            handler.pprice = view.findViewById(R.id.productPrice);
            handler.pdesc = view.findViewById(R.id.productDescription);
            handler.imageView = view.findViewById(R.id.productImage);



            view.setTag(handler);


        }  else handler = (Handler) view.getTag(); {

            final Product product = list.get(i);

            handler.pname.setText(product.getName());
            handler.pprice.setText("Price: ₱" + product.getPrice());
            handler.pdesc.setText(product.getDescription());
            handler.imageView.setImageUrl(product.getImage(),imageLoader);

            Button btnAddCart = view.findViewById(R.id.button_add_cart);
            btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAddCartPressed(product);
                }
            });

            Button btnContinueShopping = view.findViewById(R.id.button_continue_shopping);
            btnContinueShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(activity, HomeActivity.class);
                    activity.startActivity(intent);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    activity.finish();

                }
            });
        }



        return view;
    }

    static class Handler{

        TextView pname,pprice,pdesc;
        NetworkImageView imageView;

    }


    public interface ProductDetailsAdapterListener{

        void onAddCartPressed(Product product);
    }
}
