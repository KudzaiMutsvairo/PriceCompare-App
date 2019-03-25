package zw.co.pricecompareapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        String path = getIntent().getExtras().getString("imagePath");
        displayImage(path);
       compute(path);
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
}
