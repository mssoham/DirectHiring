package afroradix.xigmapro.com.directhiringcom.fragments;

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
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import adapters.NotificationlistAdapter;
import afroradix.xigmapro.com.directhiringcom.DashboardActivity;
import afroradix.xigmapro.com.directhiringcom.MYProfile;
import afroradix.xigmapro.com.directhiringcom.R;
import custom_components.RoundedImageViewWhiteBorder;
import shared_pref.SharedStorage;
import utilities.ImageFile.CreateImagefile;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.ImageDownloaderTask;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.DashboardUserImageBean;
import utilities.data_objects.DirectHiringModel;
import utilities.data_objects.UserBean;
import utilities.data_objects.UserPhotosLoadBean;
import utilities.others.CToast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageChangeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageChangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageChangeFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener, AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String img_url="http://xigmapro.website/dev4/directhiring/public/resource/site/images/users/";
    private RoundedImageViewWhiteBorder dp_pd_edit;
    private ImageView viewimage;
    private ViewPager pager_edit;
    private ImageButton upld_edit,add_image;
    CustomPagerAdapter customPagerAdapter;
    CreateImagefile createImagefile=new CreateImagefile();
    String mCurrentPhotoPath,description1;
    ProgressDialog progressDialog;
    private AnimationDrawable loadingViewAnim;
    private Bitmap bitmap;
    private TextView skip;
    private Button submit_edit_photo;
    public static String viewpager="";
    int count=0;

    private Uri filePath;
    DirectHiringModel dataModel=DirectHiringModel.getInstance();

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageChangeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageChangeFragment newInstance(String param1, String param2) {
        ImageChangeFragment fragment = new ImageChangeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageChangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_image_change, container, false);
        getDialog().setTitle("Change Image");
        getDialog().setTitle("Change Image");
        dp_pd_edit=(RoundedImageViewWhiteBorder)view.findViewById(R.id.dp_pd_edit);
        pager_edit=(ViewPager)view.findViewById(R.id.pager_edit);
        upld_edit=(ImageButton)view.findViewById(R.id.upld_edit);
        add_image=(ImageButton)view.findViewById(R.id.add_image);
        viewimage=(ImageView)view.findViewById(R.id.viewimage);
        submit_edit_photo=(Button)view.findViewById(R.id.submit_edit_photo);
        /*customPagerAdapter=new CustomPagerAdapter(getActivity(),dataModel.dashboardUserBeanArrayList.getDashboardUserImageBeanArrayList());*/
        Log.e("Img>>", img_url + "/" + dataModel.userBean.getImage());
        pager_edit.setAdapter(new CustomPagerAdapter(getActivity(),dataModel.userBean.getUserPhotosLoadBeanArrayList()));
        if (!dataModel.userBean.getImage().equals("")) {
            if (dp_pd_edit != null) {
                new ImageDownloaderTask(dp_pd_edit).execute(img_url + "/" + dataModel.userBean.getImage());
            }
        }
        count=dataModel.userBean.getUserPhotosLoadBeanArrayList().size();
        if(count>=3){
            add_image.setVisibility(View.GONE);
        }
        upld_edit.setOnClickListener(this);
        submit_edit_photo.setOnClickListener(this);
        add_image.setOnClickListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v==upld_edit){
            viewpager="dp image";
            selectImage();
        }
        if(v==add_image){
            viewpager="pagerimage";
            selectImage();
        }
        if(v==submit_edit_photo){
            uploadImage();
        }
    }
    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.setFlags(Intent.FLAG_FROM_BACKGROUND);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                if(viewpager.equals("dp image")){
                    dp_pd_edit.setImageBitmap(bmImg);
                    dp_pd_edit.setTag(mCurrentPhotoPath);
                }else{
                    viewimage.setImageBitmap(bmImg);
                    viewimage.setTag(mCurrentPhotoPath);
                    uploadviewImage();
                }
                /*uploadImage();*/


            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                if(viewpager.equals("dp image")){
                    dp_pd_edit.setImageBitmap(thumbnail);
                    dp_pd_edit.setTag(picturePath);
                }else{
                    viewimage.setImageBitmap(thumbnail);
                    viewimage.setTag(picturePath);
                    uploadviewImage();
                }
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

    @Override
    public void processFinish(String type, String output) {
        stop_progress_dialog();
        Log.e("Response-->", output.toString());
        if (type.equals(RemoteAsync.CHANGEPROFILEIMAGE)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->",obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    dataModel.userBean.setImage(obj.getString("image"));
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(),MYProfile.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(type.equals(RemoteAsync.ADDUSERPHOTOS)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    JSONArray user_photos = obj.getJSONArray("images");

                    UserPhotosLoadBean userPhotosLoadBean = new UserPhotosLoadBean();
                    ArrayList<UserPhotosLoadBean> userPhotosLoadBeanArrayList = new ArrayList<UserPhotosLoadBean>();
                    if (user_photos.length() > 0) {
                        for (int j = 0; j < user_photos.length(); j++) {
                            Log.e("loop count", "count");
                            JSONObject imgObj = user_photos.getJSONObject(j);
                            UserPhotosLoadBean usersPhotosLoadBean = new UserPhotosLoadBean();

                            usersPhotosLoadBean.setId(imgObj.getString("id"));
                            usersPhotosLoadBean.setImage(imgObj.getString("img"));

                            userPhotosLoadBeanArrayList.add(usersPhotosLoadBean);
                            /*customPagerAdapter.notifyDataSetChanged();*/
                        }
                    }
                    userPhotosLoadBean.setUserPhotosLoadBeanArrayList(userPhotosLoadBeanArrayList);
                    dataModel.userBean.setUserPhotosLoadBeanArrayList(userPhotosLoadBeanArrayList);
                    /*customPagerAdapter.notifyDataSetChanged();*/
                }else{
                    CToast.show(getActivity(),"failed to add image");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    class CustomPagerAdapter extends PagerAdapter implements View.OnClickListener {
        Context mContext;
        /*int count=0;*/
        LayoutInflater mLayoutInflater;
        ArrayList<UserPhotosLoadBean> userPhotosLoadBeanArrayList=new ArrayList<UserPhotosLoadBean>();

        public CustomPagerAdapter(Context context, ArrayList<UserPhotosLoadBean> userPhotosLoadBeanArrayList) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.userPhotosLoadBeanArrayList=userPhotosLoadBeanArrayList;
        }

        @Override
        public int getCount() {
            //Log.e("size---->", String.valueOf(userPhotosLoadBeanArrayList.size()));
            return userPhotosLoadBeanArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_myprfl_item, container, false);


            UserPhotosLoadBean userPhotosLoadBean=userPhotosLoadBeanArrayList.get(position);
            ImageView imageView_edit = (ImageView) itemView.findViewById(R.id.imageView_edit);
            /*ImageView add_image = (ImageView) itemView.findViewById(R.id.add_image);*/
            ImageView delete_image = (ImageView) itemView.findViewById(R.id.delete_image);
            /*add_image.setOnClickListener(this);*/
            //imageView.setImageResource(mResources[position]);
            if (imageView_edit != null) {
                new ImageDownloaderTask(imageView_edit).execute(img_url+"/"+userPhotosLoadBean.getImage());
                Log.e("images:",userPhotosLoadBean.getImage());
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
           /* if(v==add_image){
            }
            if(v==delete_image){

            }*/
        }
    }
    private void uploadImage(){
        start_progress_dialog();
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        String user_id = SharedStorage.getValue(getActivity(), "UserId");
        arrayList.add(new org.apache.http.message.BasicNameValuePair("user_id", user_id));


        if (dp_pd_edit.getTag() != null) {
            arrayList.add(new org.apache.http.message.BasicNameValuePair("image", (String) dp_pd_edit.getTag().toString()));
        } else {
            arrayList.add(new org.apache.http.message.BasicNameValuePair("image", ""));
        }


        RemoteAsync remoteAsync = new RemoteAsync(Urls.changeProfileImage);
        remoteAsync.type = RemoteAsync.CHANGEPROFILEIMAGE;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }
    private void uploadviewImage(){
        start_progress_dialog();
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        String user_id = SharedStorage.getValue(getActivity(), "UserId");
        arrayList.add(new org.apache.http.message.BasicNameValuePair("user_id", user_id));


        if (viewimage.getTag() != null) {
            arrayList.add(new org.apache.http.message.BasicNameValuePair("image", (String) viewimage.getTag().toString()));
        } else {
            arrayList.add(new org.apache.http.message.BasicNameValuePair("image", ""));
        }


        RemoteAsync remoteAsync = new RemoteAsync(Urls.addUserPhotos);
        remoteAsync.type = RemoteAsync.ADDUSERPHOTOS;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);

    }

    void start_progress_dialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    void stop_progress_dialog(){
        progressDialog.dismiss();
    }
}
