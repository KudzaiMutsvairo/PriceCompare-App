package zw.co.pricecompareapp;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import zw.co.pricecompareapp.models.DataReceived;
import zw.co.pricecompareapp.models.Item;
import zw.co.pricecompareapp.viewmodel.GetData;
import zw.co.pricecompareapp.viewmodel.OkHttpGetData;

import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileOutputStream;

public class FragmentCamera extends Fragment {
    private CameraKitView cameraKitView;
    GetData getData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_camera, container, false);
        //isStoragePermissionGranted();
        cameraKitView = (CameraKitView)view.findViewById(R.id.camera);
        getData = new GetData(getActivity());
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabCheck);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onResume() {
        super.onResume();

        cameraKitView.onResume();
    }

    @Override
    public void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    void captureImage(){
        cameraKitView.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, final byte[] photo) {
                boolean flag = false;

                File savedPhoto = new File(Environment.getExternalStorageDirectory(), "pchk.jpg");
                try{
                    FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                    outputStream.write(photo);
                    outputStream.close();
                    flag = true;
                }catch (java.io.IOException e){
                    e.printStackTrace();
                }
                if(flag){
                    try {
                        //DataReceived dataReceived = getData.uploaduserimage();
                        OkHttpGetData okHttpGetData = new OkHttpGetData(getActivity());
                        DataReceived dataReceived = okHttpGetData.uploadOkHttp();
                        if (dataReceived == null){
                            Toasty.error(getActivity(), "Error: Could not process image ", Toast.LENGTH_SHORT, true).show();
                        }else{
                            sendObject(dataReceived);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void sendObject(DataReceived dt){
        Bundle bundle=new Bundle();
        bundle.putSerializable("dataReceived",dt);

        FragmentManager fragmentManager2 = getFragmentManager();
        FragmentTransaction ft2 = fragmentManager2.beginTransaction();
        FragmentResult frag2 = new FragmentResult();
        frag2.setArguments(bundle);
        ft2.addToBackStack("xyz");
        ft2.hide(FragmentCamera.this);
        ft2.add(R.id.frameMain, frag2);
        ft2.commit();
    }
}
