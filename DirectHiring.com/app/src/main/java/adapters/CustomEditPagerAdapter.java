package adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import afroradix.xigmapro.com.directhiringcom.R;
import utilities.ImageFile.CreateImagefile;
import utilities.async_tasks.ImageDownloaderTask;
import utilities.data_objects.DirectHiringModel;
import utilities.data_objects.UserPhotosLoadBean;

/**
 * Created by xyxz on 7/22/2016.
 */
public class CustomEditPagerAdapter /*extends PagerAdapter implements View.OnClickListener*/ {
   /* private static String mCurrentPhotoPath;
    private static ImageView imageView_edit,add_image,delete_image;
    private static Context mContext;
    UserPhotosLoadBean userPhotosLoadBean=new UserPhotosLoadBean();
    int count=0;
    private String img_url="http://xigmapro.website/dev4/directhiring/public/resource/site/images/users/";
    LayoutInflater mLayoutInflater;
    CreateImagefile createImagefile=new CreateImagefile();
    ProgressDialog progressDialog;
    private AnimationDrawable loadingViewAnim;
    private Bitmap bitmap;

    private Uri filePath;
    DirectHiringModel dataModel=DirectHiringModel.getInstance();
    ArrayList<UserPhotosLoadBean> userPhotosLoadBeanArrayList=new ArrayList<UserPhotosLoadBean>();

    public CustomEditPagerAdapter(Context context, ArrayList<UserPhotosLoadBean> userPhotosLoadBeanArrayList) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.userPhotosLoadBeanArrayList=userPhotosLoadBeanArrayList;
    }
    @Override
    public int getCount() {
        if (userPhotosLoadBeanArrayList.size()<=3){
            count=userPhotosLoadBeanArrayList.size()+1;
        }else{
            count=userPhotosLoadBeanArrayList.size();

        }
        //Log.e("size---->", String.valueOf(userPhotosLoadBeanArrayList.size()));
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_myprfl_item, container, false);

        if(position<userPhotosLoadBeanArrayList.size()){
            userPhotosLoadBean=userPhotosLoadBeanArrayList.get(position);
        }


        imageView_edit = (ImageView) itemView.findViewById(R.id.imageView_edit);
        add_image = (ImageView) itemView.findViewById(R.id.add_image);
        delete_image = (ImageView) itemView.findViewById(R.id.delete_image);
        if(userPhotosLoadBeanArrayList.size()<3){
            count=userPhotosLoadBeanArrayList.size();
            if(position==count){
                imageView_edit.setVisibility(View.GONE);
                add_image.setVisibility(View.VISIBLE);
                delete_image.setVisibility(View.GONE);
            }
        }
        add_image.setOnClickListener(this);
        //imageView.setImageResource(mResources[position]);
        if (imageView_edit != null) {
            new ImageDownloaderTask(imageView_edit).execute(img_url+"/"+userPhotosLoadBean.getImage());
            Log.e("images:", userPhotosLoadBean.getImage());
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public void onClick(View v) {
        if(v==add_image){
            selectImage();
        }
        if(v==delete_image){

        }
    }
    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.setFlags(Intent.FLAG_FROM_BACKGROUND);
                    if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
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
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        CustomEditPagerAdapter.onActivityResult(requestCode, resultCode, data);
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
                 *//*end code for image rotation*//*
                imageView_edit.setImageBitmap(bmImg);
                imageView_edit.setTag(mCurrentPhotoPath);
                *//*uploadImage();*//*


            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = mContext.getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));



                imageView_edit.setImageBitmap(thumbnail);
                imageView_edit.setTag(picturePath);
                // profile_image_upload();
                *//*uploadImage();*//*


            }
        }
    }

    *//**
     * ============================code for image rotation=======================
     * @param file
     * @return
     *//*
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
    }*/
}
