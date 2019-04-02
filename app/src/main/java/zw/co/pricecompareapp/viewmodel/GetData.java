package zw.co.pricecompareapp.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import zw.co.pricecompareapp.models.DataReceived;

public class GetData {
    //This class does all the uploading and data retrieval from the Server.
    private final static String serverURL =  "https://pricecompareapp.000webhostapp.com/upload.php";
    private ProgressDialog progressDialog;
    private static final String TAG = "pc: ";
    private RequestQueue requestQueue;

    Bitmap image;
    String imageString;
    String imageData = null;
    private Context context;
    DataReceived dataReceived;

    public GetData(Context cntxt){
        this.context = cntxt;
    }

    public DataReceived uploaduserimage(){
        dataReceived=null;
        try {
            requestQueue = Volley.newRequestQueue(context);
            progressDialog = ProgressDialog.show(context, "Please Wait...", "Processing image...");

            Map<String, String> param = new HashMap<>();
            imageData = imageToString(getImageBitMap());
            if (imageData == null) {
                Log.v(TAG, "No image data: imageData is null");
            }
            Log.d(TAG, imageData);
            param.put("image", imageData);
            JSONObject p = new JSONObject(param);
            JSONArray parameters = new JSONArray();
            parameters.put(p);

            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST, serverURL, parameters, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    //convert response object to item object.
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                    dataReceived = new Gson().fromJson(new Gson().toJson(response), DataReceived.class);
                    progressDialog.dismiss();
                    progressDialog.cancel();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("priceCompare", "" + error);
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
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataReceived;
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

    Bitmap getImageBitMap(){
        try {
            File sd = Environment.getExternalStorageDirectory();
            File imageFile = new File(sd + "/pchk.jpg");

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            image = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
            if(image == null) {
                Log.v(TAG, "image object empty");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return image;
    }

    public DataReceived uploadOkHttp() throws IOException {
        DataReceived dataReceived = null;
        return dataReceived;
    }

}
