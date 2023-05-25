package com.example.coffeeshop_onlinedb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DrinkDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_details);

        Intent intent = getIntent();
        String Drink_Name = intent.getStringExtra("drink_name");
        String Drink_id = intent.getStringExtra("drink_id");

        requestImage(Integer.valueOf(Drink_id));

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/Mobile2022/getDrinkByName.php";
        String parameters = "?Drink_Name="+Drink_Name;
        url+=parameters;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(DrinkDetails.this, "Data received successfully",Toast.LENGTH_LONG).show();
                        TextView DN = findViewById(R.id.DrinkName);
                        TextView DD = findViewById(R.id.Drinkdescription);
                        TextView DP = findViewById(R.id.DrinkPrice);
                        try{
                            DN.setText(response.getString("pid"));
                            DD.setText(response.getString("description"));
                            DP.setText(response.getString("price"));
                        }catch (Exception ex){
                            Toast.makeText(DrinkDetails.this, "Error occurred:" + ex.toString(),Toast.LENGTH_LONG).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DrinkDetails.this, "Error occurred:" + error.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(request);
    }

    public void requestImage(int PID){
       ImageView iv = findViewById(R.id.DrinkImageView);
       RequestQueue queue = Volley.newRequestQueue(this);

                String url = "http://10.0.2.2/Mobile2022/getImage.php?PID="+PID;

                ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        iv.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ARGB_8888,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(DrinkDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                queue.add(request);

    }
}