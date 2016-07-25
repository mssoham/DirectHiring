package adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import afroradix.xigmapro.com.directhiringcom.NotificationActivity;
import afroradix.xigmapro.com.directhiringcom.R;
import afroradix.xigmapro.com.directhiringcom.SignUpSecondPage;
import custom_components.LikeHolder;
import custom_components.NotificationHolder;
import shared_pref.SharedStorage;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.ImageDownloaderTask;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.LikeBean;
import utilities.data_objects.NotificationBean;
import utilities.others.CToast;

/**
 * Created by xyxz on 7/14/2016.
 */
public class NotificationlistAdapter extends BaseAdapter implements AsyncResponse, View.OnClickListener {
    ArrayList<NotificationBean> notificationBeanArrayList = new ArrayList<NotificationBean>();
    Context context;
    LayoutInflater inflater;
    String fromid,user_id;
    int pos=0;
    ProgressDialog progressDialog;

    private String img_url="http://xigmapro.website/dev4/directhiring/public/resource/site/images/users/";

    public NotificationlistAdapter(ArrayList<NotificationBean> notificationBeanArrayList, Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.notificationBeanArrayList = notificationBeanArrayList;
    }

    @Override
    public int getCount() {
        return notificationBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Holder holder = null;
        NotificationBean notificationBean = notificationBeanArrayList.get(position);

        if (convertView == null) {
            inflater = getLayoutInflater();
            //holder = new Holder();
            convertView = inflater.inflate(R.layout.inflate_notification_layout, parent, false);
            notificationBean.holder.user_name_noti = (TextView) convertView.findViewById(R.id.user_name_noti);
            notificationBean.holder.user_img_noti = (ImageView) convertView.findViewById(R.id.user_img_noti);
            notificationBean.holder.user_info_location = (TextView) convertView.findViewById(R.id.user_info_location);
            notificationBean.holder.accept = (Button) convertView.findViewById(R.id.accept);
            notificationBean.holder.decline = (Button) convertView.findViewById(R.id.decline);

            convertView.setTag(notificationBean.holder);
        } else {
            notificationBean.holder = (NotificationHolder) convertView.getTag();
        }

        notificationBean.holder.user_name_noti.setText(notificationBean.getName());
        notificationBean.holder.accept.setTag(position);
        notificationBean.holder.decline.setTag(position);
        notificationBean.holder.user_info_location.setText(notificationBean.getAge()+" - "+notificationBean.getLocation());
        if (notificationBean.holder.user_img_noti != null) {
            new ImageDownloaderTask(notificationBean.holder.user_img_noti).execute(img_url+"/"+notificationBean.getImage());
        }
        notificationBean.holder.accept.setOnClickListener(this);
        notificationBean.holder.decline.setOnClickListener(this);

        return convertView;
    }

    private LayoutInflater getLayoutInflater() {
        return inflater;
    }

    /*class Holder {

        ImageView user_img;
        TextView user_name, user_info;
    }*/
    private void accept() {
        start_progress_dialog();

        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new org.apache.http.message.BasicNameValuePair("user_id",fromid));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("from_id", user_id));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("status", "1"));

        RemoteAsync remoteAsync = new RemoteAsync(Urls.friendRequest);
        remoteAsync.type = RemoteAsync.FRIENDREQUEST;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }

    @Override
    public void processFinish(String type, String output) {
        stop_progress_dialog();
        Log.e("Output---->", output);
        if (type.equals(RemoteAsync.FRIENDREQUEST)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    String message=obj.getString("message");
                    CToast.show(context,message);
                    Intent intent=new Intent(context,NotificationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void decline() {
        start_progress_dialog();

        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new org.apache.http.message.BasicNameValuePair("user_id", fromid));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("from_id", user_id));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("status", "0"));

        RemoteAsync remoteAsync = new RemoteAsync(Urls.friendRequest);
        remoteAsync.type = RemoteAsync.FRIENDREQUEST;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }
    void start_progress_dialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setIndeterminate(true);
        /*progressDialog.setCancelable(false);*/
        /*progressDialog.show();*/
    }

    void stop_progress_dialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        pos = (int) v.getTag();
        Log.e("Button position---->", String.valueOf(pos));
        NotificationBean notificationBean = notificationBeanArrayList.get(pos);
        switch (v.getId()) {

            case R.id.accept:
                user_id = SharedStorage.getValue(context, "UserId");
                fromid=notificationBean.getFromid();
                accept();
                break;
            case R.id.decline:
                user_id = SharedStorage.getValue(context, "UserId");
                fromid=notificationBean.getFromid();
                decline();
                break;
        }
    }
}
