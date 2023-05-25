package com.example.coffeeshop_onlinedb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MyImage {
    private Context context;
    private final static String UPLAODURL = "http://10.0.2.2/Mobile2022/upload2.php";
   // private final static String DOWNLAODFOLDER = "http://10.0.2.2/Mobile2022/images/";

    public MyImage(Context context) {
        this.context = context;


    }



  /*  public ImageRequest downloadImage(String id) {
        String url = DOWNLAODFOLDER + id + ".jpg";
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                iv.setImageBitmap(response);
                prog.setVisibility(View.INVISIBLE);
            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prog.setVisibility(View.INVISIBLE);
                iv.setImageBitmap(null);
                Toast.makeText(context, "can't get image", Toast.LENGTH_SHORT).show();
            }
        });

        return request;
    }
*/
    public StringRequest uplaodImage(int id, Bitmap bitmap) {
        StringRequest request = new StringRequest(Request.Method.POST, UPLAODURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Bitmap bmap = bitmap;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmap.compress(Bitmap.CompressFormat.JPEG,50,bos);
                byte[] bb = bos.toByteArray();
                String image = Base64.encodeToString(bb, Base64.DEFAULT);

                Map<String, String> params = new HashMap<>();
                params.put("PID", String.valueOf(id));
                params.put("file", image);
                return params;
            }
        };

        return request;
    }
}
