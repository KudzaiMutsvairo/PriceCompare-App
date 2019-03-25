package zw.co.pricecompareapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import zw.co.pricecompareapp.models.DataReceived;
import zw.co.pricecompareapp.models.Item;
import zw.co.pricecompareapp.viewmodel.OkHttpGetData;
import zw.co.pricecompareapp.viewmodel.ProductsAdapter;

public class Results extends AppCompatActivity {
    private String TAG =  "pc: ";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        final String path = getIntent().getExtras().getString("imagePath");

        progressDialog = new ProgressDialog(Results.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Processing Image"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        progressDialog.show();
        new CountDownTimer(1000, 500) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //mTextField.setText("done!");
                Intent intent = new Intent(Results.this, Start.class);
                displayImage(path);
                compute(path);
                progressDialog.dismiss();
            }
        }.start();

    }


    public void compute(String path){
        OkHttpGetData getData =  new OkHttpGetData(this, path);
        DataReceived dtr = null;
        try {
            dtr = getData.uploadOkHttp2();
            if(dtr != null) {
                String name = dtr.getDescription();

                ArrayList<Item> prices = dtr.getData();

                ListView lst = (ListView) findViewById(R.id.lstProduct);
                TextView tvPd = (TextView) findViewById(R.id.tvProduct);

                tvPd.setText(name);
                ProductsAdapter adapter = new ProductsAdapter(this, prices);
                if (prices != null) {
                    lst.setAdapter(adapter);
                }
                if (dtr.getError() != null){
                    Toast.makeText(this, dtr.getError(), Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "No data returned from server", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void displayImage(String filePath){
        File imgFile = new File(filePath);
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath()); //this is the bitmap for the image

            ImageView myImage = (ImageView) findViewById(R.id.ivProductI);//your image view in the recycler view

            myImage.setImageBitmap(myBitmap);//image set to the image view

        }else {
            Log.d(TAG, "displayImage: File does not exist");
        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
