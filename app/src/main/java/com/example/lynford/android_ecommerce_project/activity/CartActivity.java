package com.example.lynford.android_ecommerce_project.activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lynford.android_ecommerce_project.R;
import com.example.lynford.android_ecommerce_project.app.AppController;
import com.example.lynford.android_ecommerce_project.app.Config;
import com.example.lynford.android_ecommerce_project.helper.CartAdapter;
import com.example.lynford.android_ecommerce_project.model.Cart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    //updated

    /**
     * TAGS
     */
    private static final String TAG = CartActivity.class.getSimpleName();

    /**
     * cart adapter
     */
    private CartAdapter adapter;

    /**
     * ArrayList cart
     */
    private ArrayList<Cart>list = new ArrayList<Cart>();

    /**
     * listview
     * @param bundle
     */
    private ListView listView;
    private TextView total;

    private Button btnCheckOut;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_cart_layout);

        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.cart_listview);
        adapter = new CartAdapter(this,list);
        listView.setAdapter(adapter);

        total = findViewById(R.id.total);

        btnCheckOut = findViewById(R.id.button_check_out);
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if (list.size()>0){
                        launchToCheckOut();
                    } else {
                        Toast.makeText(CartActivity.this, "Cart is Empyty", Toast.LENGTH_SHORT).show();
                    }



            }
        });


        fetch_products_in_cart();

    }



    private double getTotalPrice(ArrayList<Cart> mCart){

         int totalCost = 0;
        for(int i = 0; i < mCart.size(); i++){
            Cart cartObject = mCart.get(i);
            totalCost = totalCost + cartObject.getPrice();


            total.setText("" + totalCost);
        }
        return totalCost;


    }

    private void launchToCheckOut() {

        int amount = Integer.parseInt(String.valueOf(total.getText().toString()));


        final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.activity_check_out,null);

        builder.setView(v).setTitle("Confirm Your Order")
                .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();

        TextView display_total = v.findViewById(R.id.DisplayTotal);
        Button button_confirm = v.findViewById(R.id.button_confirm_order);

        display_total.setText(" " + amount);

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                for (int arr = 0; arr <list.size(); arr++){

                    String pid = list.get(arr).getId();

                    String name = list.get(arr).getName();

                    int mprice = list.get(arr).getPrice();

                    String price = Integer.valueOf(mprice).toString();

                    String description = list.get(arr).getDescription();
                    String image = list.get(arr).getImage();
                    String payment_type = "COD";
                    String sku = list.get(arr).getSku();
                    String status = "Pending";

                    confirm_order(pid,name,price,description,image,payment_type,sku,status);


                }

            }
        });
    }



    /**
     * confirm order
     */
    private void confirm_order(final String pid, final String name, final String price, final String description, final String image,
                               final String payment_type, final String sku, final String status) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CONFIRM_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG,"response: " + response.toString());
                try{

                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    if (jsonObject.getBoolean("error") == false){

                        Toast.makeText(CartActivity.this, ""+message, Toast.LENGTH_SHORT).show();

                        //if success product successfully ordered update status
                        String status = "on_order";
                        update_cart_status_on_order(pid,status);

                    } else {
                        Toast.makeText(CartActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    Log.d(TAG,"json exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG,"error: " + error.getMessage());
                Toast.makeText(CartActivity.this, "Network Error!" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            protected Map<String,String>getParams(){
                Map<String,String>maps = new HashMap<String, String>();


                    maps.put("product_id",pid);
                    maps.put("name",name);
                    maps.put("price",price);
                    maps.put("description",description);
                    maps.put("image",image);
                    maps.put("payment_type",payment_type);
                    maps.put("sku",sku);
                    maps.put("status",status);


                    return maps;


            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    /*
    *Update cart status
     */
    private void update_cart_status_on_order(final String pid, final String status) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATE_CART_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG,"response: " + response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    if (jsonObject.getBoolean("error") == false){

                        Toast.makeText(CartActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CartActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    Log.d(TAG,"Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG,"volley error: " + error.getMessage());

            }
        }){
           protected Map<String,String>getParams(){
               Map<String,String> params = new HashMap<String, String>();

               params.put("product_id",pid);
               params.put("status",status);

               return  params;
           }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    /**
     * fetch products
     */
    private void fetch_products_in_cart() {

        Toast.makeText(this, "fetching cart", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.GET_ALL_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG,"response: " + response.toString());

               try {

                   JSONObject jsonObject = new JSONObject(response);

                   if (jsonObject.getBoolean("error") == false){

                       JSONArray jsonArray = jsonObject.getJSONArray("cart");

                       for (int i = 0; i<jsonArray.length();i++){

                           JSONObject json = (JSONObject) jsonArray.get(i);

                           String id = json.getString("product_id");
                           String name = json.getString("name");
                           String description = json
                                   .getString("description");
                           String image = json.getString("image");
                           int price = Integer.parseInt(json.getString("price"));
                           String sku = json.getString("sku");

                           Cart cart = new Cart();

                           cart.setId(id);
                           cart.setName(name);
                           cart.setDescription(description);
                           cart.setImage(image);
                           cart.setPrice(price);
                           cart.setSku(sku);

                           list.add(cart);


                           getTotalPrice(list);


                       }

                       adapter.notifyDataSetChanged();
                   }

               }catch (JSONException e){
                   Log.d(TAG,"json exception: " + e.getMessage());
               }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG,"response tag: " + error.getMessage());
                Toast.makeText(CartActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
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



}
