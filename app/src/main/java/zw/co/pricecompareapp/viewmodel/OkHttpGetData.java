package zw.co.pricecompareapp.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zw.co.pricecompareapp.models.DataReceived;

public class OkHttpGetData {
    private final static String url =  "https://pricecompareapp.000webhostapp.com/upload.php";
    private ProgressDialog progressDialog;
    private static final String TAG = "pc: ";
    private RequestQueue requestQueue;

    Bitmap image;
    String imageString;
    String imageData = null;
    private Context context;
    static DataReceived dataReceived = null;
    String imagePaths;

    public OkHttpGetData(Context context) {
        this.context = context;
    }

    public OkHttpGetData(Context context, String imagep) {
        this.imagePaths = imagep;
        this.context = context;
    }

    private String imageToString(Bitmap bitmap){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            Log.v(TAG, imageString);
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

    Bitmap setImageBitMap(String p){
        try {
            File sd = Environment.getExternalStorageDirectory();
            File imageFile = new File(p);

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

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        //String url = "https://cakeapi.trinitytuts.com/api/add";

        OkHttpClient client = new OkHttpClient();

        JSONObject postdata = new JSONObject();
        progressDialog = ProgressDialog.show(context, "Please Wait...", "Processing image...");
        imageData = imageToString(getImageBitMap());
        try {
            postdata.put("image", imageData);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
                //TODO Make toast here to show error that network failed
                progressDialog.dismiss();
                progressDialog.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                Log.e(TAG, mMessage);
                dataReceived = new Gson().fromJson(mMessage, DataReceived.class);
                if(dataReceived !=  null) {
                    Log.e(TAG, dataReceived.getError());
                }else {
                    Log.e(TAG, "object is null");
                }
                progressDialog.dismiss();
                progressDialog.cancel();
            }
        });
        return dataReceived;
    }


    /************************************************************************/
    public DataReceived uploadOkHttp2() throws IOException {

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        //String url = "https://cakeapi.trinitytuts.com/api/add";

        OkHttpClient client = new OkHttpClient();

        JSONObject postdata = new JSONObject();
        progressDialog = ProgressDialog.show(context, "Please Wait...", "Processing image...");
        imageData = imageToString(setImageBitMap(imagePaths));
        try {
            postdata.put("image", imageData);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
                //TODO Make toast here to show error that network failed
                progressDialog.dismiss();
                progressDialog.cancel();
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                Log.e(TAG, mMessage);
                dataReceived = new Gson().fromJson(mMessage, DataReceived.class);
                if(dataReceived !=  null) {
                    Log.e(TAG, dataReceived.getDescription());
                    //Log.e(TAG, dataReceived.getError());
                }else {
                    Log.e(TAG, "object is null");
                }
                progressDialog.dismiss();
                progressDialog.cancel();
                countDownLatch.countDown();
            }

        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dataReceived;
    }

}
