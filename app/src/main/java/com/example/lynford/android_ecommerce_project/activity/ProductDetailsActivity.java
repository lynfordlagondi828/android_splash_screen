package com.example.lynford.android_ecommerce_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lynford.android_ecommerce_project.R;
import com.example.lynford.android_ecommerce_project.app.AppController;
import com.example.lynford.android_ecommerce_project.app.Config;
import com.example.lynford.android_ecommerce_project.model.Product;
import com.example.lynford.android_ecommerce_project.helper.ProductDetailsAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProductDetailsActivity extends AppCompatActivity implements ProductDetailsAdapter.ProductDetailsAdapterListener {

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();


    String id;

    private ListView listView;
    private ProductDetailsAdapter adapter;
    private ArrayList<Product>list = new ArrayList<Product>();


    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_product_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Product Details");

        Intent intent= getIntent();

        id = intent.getStringExtra("id");

        listView = findViewById(R.id.listview);
        adapter = new ProductDetailsAdapter(this,list,this);
        listView.setAdapter(adapter);


        fetch_pDetails();


    }

    private void fetch_pDetails() {

       // String id = EndPoints.CHAT_ROOM_MESSAGE.replace("_ID_", chatRoomId);

        String pId = Config.GET_PRODUCT_DETAILS.replace("_ID_",id);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, pId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG,"response: " + response.toString());

                try{

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("error")==false){


                        JSONArray jsonArray = jsonObject.getJSONArray("products");

                        for (int i = 0; i<jsonArray.length();i++){

                            JSONObject json = (JSONObject) jsonArray.get(i);

                            String name = json.getString("name");
                            String desc = json.getString("description");
                            String image = json.getString("image");
                            int price = Integer.parseInt(json.getString("price"));
                            String sku = json.getString("sku");

                            Product p = new Product();

                            p.setId(id);
                            p.setName(name);
                            p.setDescription(desc);
                            p.setImage(image);
                            p.setPrice(price);
                            p.setSku(sku);


                            list.add(p);


                        } adapter.notifyDataSetChanged();
                    }

                }catch (JSONException e){
                    Log.d(TAG,e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG,"volley error" + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAddCartPressed(Product product) {

        String pid = product.getId();
        String name = product.getName();
        String desc = product.getDescription();
        String image = product.getImage();
        int price = product.getPrice();
        String mprice = Integer.valueOf(price).toString();
        String sku = product.getSku();
        String status = "on_cart";

        add_to_cart(pid,name,desc,image,mprice,sku,status);


    }

    private void add_to_cart(final String pid, final String name, final String desc, final String image,
                             final String mPrice, final String sku, final String status) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.ADD_TO_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG,"response: " + response.toString());

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");

                    if (jsonObject.getBoolean("error") == false){

                        Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    Log.d(TAG,"json exception" + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG,"volley error" + error.getMessage());
                Toast.makeText(ProductDetailsActivity.this, "volley error", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String,String>getParams(){

                Map<String,String>maps = new HashMap<String, String>();

                maps.put("product_id",pid);
                maps.put("name",name);
                maps.put("price",mPrice);
                maps.put("description",desc);
                maps.put("image",image);
                maps.put("sku",sku);
                maps.put("status",status);


                return maps;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
