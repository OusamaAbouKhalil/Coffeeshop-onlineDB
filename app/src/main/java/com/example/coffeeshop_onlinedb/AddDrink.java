package com.example.coffeeshop_onlinedb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddDrink extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                Drink.categories);
        Spinner spnCat = findViewById(R.id.spnProductCategory);
        spnCat.setAdapter(adapter);

    }

    public void InsertNewDrink(View view) {

        EditText edtName = findViewById(R.id.edtProductName);
        String name = edtName.getText().toString();
        EditText edtPrice = findViewById(R.id.edtProductprice);
        float Price = Float.valueOf(edtPrice.getText().toString());
        Spinner spnCat = findViewById(R.id.spnProductCategory);
        String category = Drink.categories.get(spnCat.getSelectedItemPosition());
        EditText edtDesc = findViewById(R.id.edtProductDescription);
        String description = edtDesc.getText().toString();

        if (name.equals("") || category.equals("")){
            Toast.makeText(this, "Name or category cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://10.0.2.2/Mobile2022/AddDrink.php";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(AddDrink.this, "Status: " + response,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddDrink.this, MainActivity.class);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AddDrink.this, "Error occurred:" + error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Name", name);
                    map.put("Price", String.valueOf(Price));
                    map.put("Category", category);
                    map.put("Description", description);

                    return map;
                }
            };
            queue.add(request);




    }
}