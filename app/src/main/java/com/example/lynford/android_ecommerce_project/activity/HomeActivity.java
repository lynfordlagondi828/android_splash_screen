package com.example.lynford.android_ecommerce_project.activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.lynford.android_ecommerce_project.helper.ProductListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private ListView listView;
    private ArrayList<Product>list = new ArrayList<Product>();
    private ProductListAdapter adapter;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savaInstanceState){
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_home);


        listView = findViewById(R.id.listview);
        adapter = new ProductListAdapter(this,list);
        listView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        fetch_all_products();

    }

    private void fetch_all_products() {

        progressDialog.setMessage("Loading");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.GET_ALL_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG,"response: " + response.toString());

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("error") == false){

                        JSONArray jsonArray = jsonObject.getJSONArray("products");

                        for (int i = 0; i<jsonArray.length(); i++){

                            JSONObject json = (JSONObject) jsonArray.get(i);

                            String id = json.getString("id");
                            String name = json.getString("name");
                            String description = json
                                    .getString("description");
                            String image = json.getString("image");
                            int price = Integer.parseInt(json.getString("price"));


                            String sku = json.getString("sku");


                            Product p = new Product();

                            p.setId(id);
                            p.setName(name);
                            p.setDescription(description);
                            p.setImage(image);
                            p.setPrice(price);
                            p.setSku(sku);


                            list.add(p);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    String id = list.get(i).getId();


                                    Intent intent = new Intent(HomeActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("id",id);
                                    startActivity(intent);
                                }
                            });
                        }

                        adapter.notifyDataSetChanged();
                    }

                }catch (JSONException e){
                    Log.e(TAG,"json exception");
                }

                hidepDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(HomeActivity.this, "Network Error!!!", Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showDialog() {

       if (!progressDialog.isShowing())
           progressDialog.show();
    }

    private void hidepDialog() {


        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id== R.id.action_icon){


          Intent intent= new Intent(this,CartActivity.class);
          startActivity(intent);

            return  true;

        } else if (id == R.id.action_order){

            Toast.makeText(this, "order", Toast.LENGTH_SHORT).show();
            return  true;
        }



        return super.onOptionsItemSelected(item);
    }
}
