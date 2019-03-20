package zw.co.pricecompareapp.viewmodel;

import android.content.Context;
import android.content.Intent;

public class GetImage {
    private Context ctx;

    public GetImage(Context context){
        this.ctx = context;

    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //startActivityForResult(galleryIntent, GALLERY);
    }
}
