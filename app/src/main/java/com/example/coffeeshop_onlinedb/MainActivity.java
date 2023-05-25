package com.example.coffeeshop_onlinedb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView CategoryList = findViewById(R.id.CategoriesListView);
        ArrayList<String> categories = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                categories);
        CategoryList.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/Mobile2022/getCategories.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length(); i++){
                            try{
                                JSONObject cat = response.getJSONObject(i);
                                String category = cat.getString("category");
                                categories.add(category);
                            }catch(Exception ex){}
                        }
                        Drink.categories = categories;
                        adapter.notifyDataSetChanged();
                    }
                },
                null);
        queue.add(request);

        CategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Drinks.class);
                intent.putExtra("category",categories.get(i));
                startActivity(intent);
            }
        });

    }

    public void gotoAddDrinkActivity(View view) {
        Intent intent = new Intent(this, AddDrink.class);
        startActivity(intent);
    }

    public void gotoEditDrinkActivity(View view) {
        Intent intent = new Intent(this, UpdateDrink.class);
        startActivity(intent);
    }
}