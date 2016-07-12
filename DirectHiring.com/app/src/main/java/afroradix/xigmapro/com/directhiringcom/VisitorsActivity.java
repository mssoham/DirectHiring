package afroradix.xigmapro.com.directhiringcom;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.LikelistAdapter;
import shared_pref.SharedStorage;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.LikeBean;

public class VisitorsActivity extends AppCompatActivity implements AsyncResponse {

    private ListView visitor_list;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors);

        getSupportActionBar().setTitle("Visitors");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        visitor_list = (ListView)findViewById(R.id.visitor_list);

        visitors();
    }

    private void visitors(){
        start_progress_dialog();
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        String user_id= SharedStorage.getValue(getApplicationContext(), "UserId");
        arrayList.add(new org.apache.http.message.BasicNameValuePair("user_id", user_id));

        RemoteAsync remoteAsync = new RemoteAsync(Urls.visitors);
        remoteAsync.type = RemoteAsync.VISITORS;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void start_progress_dialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    void stop_progress_dialog(){
        progressDialog.dismiss();
    }

    @Override
    public void processFinish(String type, String output) {
        stop_progress_dialog();
        if (type.equals(RemoteAsync.VISITORS)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    JSONArray likeMembersArr = obj.getJSONArray("visitor_details");

                    ArrayList<LikeBean> likeBeanArrayList = new ArrayList<LikeBean>();

                    if (likeMembersArr.length()>0){
                        for (int i=0;i<likeMembersArr.length();i++){
                            LikeBean likeBean = new LikeBean();
                            JSONObject like = likeMembersArr.getJSONObject(i);

                            likeBean.setName(like.getString("name"));
                            likeBean.setAge(like.getString("age"));
                            likeBean.setImage(like.getString("image"));
                            likeBean.setLocation(like.getString("location"));
                            likeBeanArrayList.add(likeBean);
                        }
                    }

                    visitor_list.setAdapter(new LikelistAdapter(likeBeanArrayList,getApplicationContext()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
