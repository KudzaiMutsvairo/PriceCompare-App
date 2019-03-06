package zw.co.pricecompareapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    BottomNavigationView navigation;

    public final String TAG = "MainActivity";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            Class fragmentClass = null;

            switch (item.getItemId()) {
                case R.id.navigation_text:{
                    fragmentClass = FragmentText.class;
                    //navigation.setSelectedItemId(R.id.navigation_text);
                    break;}
                case R.id.navigation_camera:{
                    fragmentClass = FragmentCamera.class;
                    //navigation.setSelectedItemId(R.id.navigation_camera);
                    break;}
                case R.id.navigation_settings:{
                    fragmentClass = FragmentSettings.class;
                    //navigation.setSelectedItemId(R.id.navigation_settings);
                    break;}
                default: {
                    fragmentClass = FragmentCamera.class;
                    //navigation.setSelectedItemId(R.id.navigation_camera);
                    break;
                }

            }
            changeFragment(fragmentClass);
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        int indexItem = 1;
        navigation.getMenu().getItem(indexItem).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        isStoragePermissionGranted();

        Class frag = FragmentCamera.class;
        changeFragment(frag);
    }

    public void changeFragment(Class frag){
        if(frag != null) {
            Fragment fragment = null;
            try {
                fragment = (Fragment) frag.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frameMain, fragment).commit();
        }
    }

    public  boolean isStoragePermissionGranted() {
        boolean result = false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission is granted");
                    result = true;
                } else {

                    Log.v(TAG, "Permission is revoked");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    result = false;
                }
            } else { //permission is automatically granted on sdk<23 upon installation
                Log.v(TAG, "Permission is granted");
                result = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission is granted");
                    result = true;
                } else {

                    Log.v(TAG, "Permission is revoked");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    result = false;
                }
            } else { //permission is automatically granted on sdk<23 upon installation
                Log.v(TAG, "Permission is granted");
                result = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

}
