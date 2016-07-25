package afroradix.xigmapro.com.directhiringcom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.LikelistAdapter;
import adapters.NotificationlistAdapter;
import shared_pref.SharedStorage;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.LikeBean;
import utilities.data_objects.NationalityBean;
import utilities.data_objects.NotificationBean;

public class NotificationActivity extends AppCompatActivity implements AsyncResponse {

    private ListView list_notication;
    String user_id;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getNotificationList();
        user_id = SharedStorage.getValue(getApplicationContext(), "UserId");
        list_notication = (ListView) findViewById(R.id.list_notication);


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void getNotificationList() {
        start_progress_dialog();

        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new org.apache.http.message.BasicNameValuePair("user_id", "1"));

        RemoteAsync remoteAsync = new RemoteAsync(Urls.notification);
        remoteAsync.type = RemoteAsync.NOTIFICATION;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }

    void start_progress_dialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    void stop_progress_dialog() {
        progressDialog.dismiss();
    }

    @Override
    public void processFinish(String type, String output) {
        stop_progress_dialog();
        Log.e("Output---->",output);
        if (type.equals(RemoteAsync.NOTIFICATION)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    JSONArray likeMembersArr = obj.getJSONArray("notification");

                    ArrayList<NotificationBean> notificationBeanArrayList = new ArrayList<NotificationBean>();

                    if (likeMembersArr.length()>0){
                        for (int i=0;i<likeMembersArr.length();i++){
                            NotificationBean notificationBean = new NotificationBean();
                            JSONObject like = likeMembersArr.getJSONObject(i);

                            notificationBean.setName(like.getString("name"));
                            notificationBean.setAge(like.getString("age"));
                            notificationBean.setImage(like.getString("image"));
                            notificationBean.setLocation(like.getString("location"));
                            notificationBean.setFromid(like.getString("id"));
                            notificationBeanArrayList.add(notificationBean);
                        }
                    }

                    list_notication.setAdapter(new NotificationlistAdapter(notificationBeanArrayList,getApplicationContext()));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NotificationActivity.this, DashboardActivity.class));
    }
}