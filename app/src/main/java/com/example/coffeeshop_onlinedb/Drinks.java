package com.example.coffeeshop_onlinedb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Drinks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        ListView DrinkListView = findViewById(R.id.DrinkListView);
        ArrayList<Drink> Drinks = new ArrayList<>();
        ArrayAdapter<Drink> adapter = new ArrayAdapter<Drink>(this,
                android.R.layout.simple_list_item_1,
                Drinks);
        DrinkListView.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/Mobile2022/getDrinks.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length(); i++){
                            try{
                                JSONObject d = response.getJSONObject(i);
                                int id = Integer.valueOf(d.getString("pid"));
                                String name = d.getString("name");
                                float price = Float.valueOf(d.getString("price"));
                                String desc = d.getString("description");
                                Drink drink = new Drink(id,name,desc,price,category);
                                if(category.equals(d.getString("category")))
                                    Drinks.add(drink);
                            }catch(Exception ex){}
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                null);
        queue.add(request);

        DrinkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Drinks.this, DrinkDetails.class);
                intent.putExtra("drink_name",Drinks.get(i).Drink_Name);
                intent.putExtra("drink_id",String.valueOf(Drinks.get(i).PID));
                startActivity(intent);
            }
        });

        DrinkListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Drinks.this);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to delete this drink?");
                builder.setPositiveButton("Yes, delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RemoveDrink(Drinks.get(i).PID);
                            }
                        });
                builder.setNegativeButton("No, cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText( Drinks.this, "Operation aborted", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });
    }
    public void RemoveDrink(int PID){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/Mobile2022/RemoveDrinkByID.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Drinks.this, "Status: " + response,Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(getIntent());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finish();
                        startActivity(getIntent());// Toast.makeText(Drinks.this, "Error occurred:" + error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("PID", String.valueOf(PID));
                return map;
            }
        };
        queue.add(request);


    }

}