package com.example.coffeeshop_onlinedb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class UpdateDrink extends AppCompatActivity {

    ArrayList<Drink> Drinks = new ArrayList<Drink>();
    Spinner spn;
    ArrayAdapter<Drink> adapter;
    Drink SelectedDrink;
    ArrayAdapter<String> CategoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_drink);

        spn = findViewById(R.id.spnAllDrinksSpinner);

        adapter = new ArrayAdapter<Drink>(this,
                android.R.layout.simple_list_item_1,
                Drinks);
        spn.setAdapter(adapter);

        LoadDrinks();

        Spinner spnCategories = findViewById(R.id.spnProductCategory);
        CategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,Drink.categories);
        spnCategories.setAdapter(CategoriesAdapter);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedDrink = Drinks.get(i);
                LoadSelectedDrink(SelectedDrink);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private void LoadDrinks() {
        // Load all drinks into the spinner at the top

        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading drinks...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/Mobile2022/getDrinks.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject d = response.getJSONObject(i);
                                int id = Integer.valueOf(d.getString("pid"));
                                String name = d.getString("name");
                                float price = Float.valueOf(d.getString("price"));
                                String desc = d.getString("description");
                                String Category = d.getString("category");
                                Drink drink = new Drink(id, name, desc, price,Category);

                                Drinks.add(drink);
                            } catch (Exception ex) {
                            }
                        }
                        adapter.notifyDataSetChanged();
                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(UpdateDrink.this, "Drinks loading failed!",Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(request);


    }

    public void LoadSelectedDrink(Drink SelectedDrink){

                        EditText DN = findViewById(R.id.edtProductName);
                        Spinner spnCategory = findViewById(R.id.spnProductCategory);
                        EditText DD = findViewById(R.id.edtProductDescription);
                        EditText DP = findViewById(R.id.edtProductprice);

                            DN.setText(SelectedDrink.getDrink_Name());
                            String catName = SelectedDrink.getDrink_Category();
                            spnCategory.setSelection(CategoriesAdapter.getPosition(catName));
                            DD.setText(SelectedDrink.getDrink_Description());
                            DP.setText(String.valueOf(SelectedDrink.getDrink_Price()));

    }

    public void UpdateDrink(View view) {
        //  Request updateDrink.php that will update the information of a giver drink sent by POST

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
        String url = "http://10.0.2.2/Mobile2022/updateDrink.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(UpdateDrink.this, "Status: " + response,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UpdateDrink.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateDrink.this, "Error occurred:" + error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Drink_ID",String.valueOf(SelectedDrink.PID));
                map.put("Name", name);
                map.put("Price", String.valueOf(Price));
                map.put("Category", category);
                map.put("Description", description);

                return map;
            }
        };
        queue.add(request);



    }


    public void RemoveDrink(View view) {
        //  Request RemoveDrink.php that will remove the selected drink by ID
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDrink.this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete this drink?");
        builder.setPositiveButton("Yes, delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RemoveDrinkByID(SelectedDrink.PID);
                    }
                });
        builder.setNegativeButton("No, cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText( UpdateDrink.this, "Operation aborted", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void RemoveDrinkByID(int Drink_ID){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/Mobile2022/RemoveDrinkByID.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(UpdateDrink.this, "Status: " + response,Toast.LENGTH_LONG).show();
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
                map.put("PID", String.valueOf(Drink_ID));
                return map;
            }
        };
        queue.add(request);
    }

    public void UpdateImage(View view){


                //if everything is ok we will open image chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);


            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                                //calling the method uploadBitmap to upload image
                MyImage myImage=new MyImage(UpdateDrink.this);
                StringRequest request = myImage.uplaodImage(SelectedDrink.PID,bitmap);
                Volley.newRequestQueue(UpdateDrink.this).add(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


       }