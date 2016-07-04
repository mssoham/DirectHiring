package afroradix.xigmapro.com.directhiringcom;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import custom_components.RoundedImageViewWhiteBorder;
import shared_pref.SharedStorage;
import utilities.ImageFile.CreateImagefile;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;

public class UploadImage extends AppCompatActivity /*implements AsyncResponse*/ {
    private EditText description;
    private TextInputLayout input_layout_description;
    private ImageButton upld;
    private RoundedImageViewWhiteBorder dp_pd;
    CreateImagefile createImagefile=new CreateImagefile();
    ImageView imgSpinner;
    String mCurrentPhotoPath;
    Button btnUpload;
    private AnimationDrawable loadingViewAnim;
    private Bitmap bitmap;

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        description=(EditText)findViewById(R.id.description);
        input_layout_description=(TextInputLayout)findViewById(R.id.input_layout_description);
        upld=(ImageButton)findViewById(R.id.upld);
        dp_pd=(RoundedImageViewWhiteBorder)findViewById(R.id.dp_pd);
        upld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }
    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.setFlags(Intent.FLAG_FROM_BACKGROUND);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File f = null;
                        try {

                            f = createImagefile.createImageFile();
                            mCurrentPhotoPath = f.getAbsolutePath();
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        } catch (IOException e) {
                            e.printStackTrace();
                            f = null;
                            mCurrentPhotoPath = null;
                        }
                        startActivityForResult(takePictureIntent, 1);
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {

                Bitmap bmImg = null;
                File imageFile = new File(mCurrentPhotoPath);
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(mCurrentPhotoPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                if(imageFile.exists()) {
                    bmImg = getResizedImage(imageFile);
                    bmImg=rotateBitmap(bmImg, orientation);
                }
                 /*end code for image rotation*/
                dp_pd.setImageBitmap(bmImg);
                dp_pd.setTag(mCurrentPhotoPath);
                /*uploadImage();*/


            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));



                dp_pd.setImageBitmap(thumbnail);
                dp_pd.setTag(picturePath);
                // profile_image_upload();
                /*uploadImage();*/


            }
        }
    }

    /**
     * ============================code for image rotation=======================
     * @param file
     * @return
     */
    public static Bitmap getResizedImage(File file) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
            return null;
        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                : bounds.outWidth;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        if (bounds.outHeight > bounds.outWidth)
            opts.inSampleSize = originalSize / 360;
        else
            opts.inSampleSize = originalSize / 640;
        return BitmapFactory.decodeFile(file.getPath(), opts);
    }
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        try{
            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    return bitmap;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }
            try {
                Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return bmRotated;
            }
            catch (OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /*private void uploadImage(){

        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        String u_id= SharedStorage.getValue(getApplicationContext(), "UserId");
        arrayList.add(new org.apache.http.message.BasicNameValuePair("u_id", u_id));

        if (dp_pd.getTag() != null) {
            arrayList.add(new org.apache.http.message.BasicNameValuePair("image", (String) dp_pd.getTag().toString()));
        } else {
            arrayList.add(new org.apache.http.message.BasicNameValuePair("image", ""));
        }


        RemoteAsync remoteAsync = new RemoteAsync(Urls.image_upload);
        remoteAsync.type = RemoteAsync.IMAGE_UPLOAD;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }

    @Override
    public void processFinish(String type, String output) {
        Log.e("Response-->", output.toString());
        if (type.equals(RemoteAsync.IMAGE_UPLOAD)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->",obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}*/
}
