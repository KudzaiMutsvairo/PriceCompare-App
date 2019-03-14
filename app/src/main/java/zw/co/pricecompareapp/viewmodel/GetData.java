package zw.co.pricecompareapp.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import zw.co.pricecompareapp.MainActivity;
import zw.co.pricecompareapp.R;
import zw.co.pricecompareapp.models.Item;

public class GetData {
    //This class does all the uploading and data retrieval from the Server.
    private final static String serverURL =  "https://pricecompareapp.000webhostapp.com/upload.php";
    private ProgressDialog progressDialog;
    private static final String TAG = "pc: ";
    private RequestQueue requestQueue;

    Bitmap image;
    String imageString;
    String imageData;
    public Context context;
    Item item;

    public GetData(Context cntxt){
        this.context = cntxt;
    }

    public Item uploaduserimage(){
        item=null;
        requestQueue = Volley.newRequestQueue(context);
        progressDialog = ProgressDialog.show(context,"Please Wait...","Processing image...");

        Map<String,String> param = new HashMap<>();
        //imageData =  imageToString(getImageBitMap());
        if (imageData == null){
            Log.v(TAG, "No image data: imageData is null");
        }
        param.put("image",imageData);
        JSONObject parameters = new JSONObject(param);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, serverURL,parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //convert response object to item object.
                item = new Gson().fromJson(new Gson().toJson(response), Item.class);
                progressDialog.dismiss();
                progressDialog.cancel();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("priceCompare",""+error);
                //txtView.setText("Cannot retrieve data: "+error);
                Log.v(TAG, error.toString());
                progressDialog.dismiss();
                progressDialog.cancel();
            }
        });

        requestQueue.add(jsonObjectRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<JSONObject>() {
            @Override
            public void onRequestFinished(Request<JSONObject> request) {
                if (progressDialog !=  null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
        return item;
    }

    private String imageToString(Bitmap bitmap){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(imageString == null){
            Log.v(TAG, "imageString is null");
        }
        return imageString;
    }

    private void displayImage(String filePath){
        File imgFile = new File(filePath);
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath()); //this is the bitmap for the image

            //ImageView myImage = (ImageView) findViewById(R.id.ivSearched);//your image view in the recycler view

            //myImage.setImageBitmap(myBitmap);//image set to the image view

        }else {
            Log.d(TAG, "displayImage: File does not exist");
        }
    }

}
