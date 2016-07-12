package afroradix.xigmapro.com.directhiringcom;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import adapters.UserCriteriaAdapter;
import afroradix.xigmapro.com.directhiringcom.fragments.PremiumMemberDialogFragment;
import custom_components.RoundedImageViewWhiteBorder;
import shared_pref.SharedStorage;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.ImageDownloaderTask;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.DashboardUserBean;
import utilities.data_objects.DashboardUserImageBean;
import utilities.data_objects.DirectHiringModel;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncResponse, View.OnClickListener,
        PremiumMemberDialogFragment.OnFragmentInteractionListener{

    private RoundedImageViewWhiteBorder img1,img2,img3,imageView;
    private ImageView imgTickCross;
    private TextView textView1,description;
    private ListView looking_for_list;
    private RelativeLayout rel_cross;
    private LinearLayout like, skip;
    private ViewPager pager;
    private String img_url="http://xigmapro.website/dev4/directhiring/public/resource/site/images/users/";
    private int user_pos=0;

    ProgressDialog progressDialog;
    DirectHiringModel dataModel=DirectHiringModel.getInstance();
    CustomPagerAdapter customPagerAdapter;

    private static final String FORMAT = "%02d:%02d:%02d";

    int seconds , minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Latest Matches");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        img1 = (RoundedImageViewWhiteBorder)findViewById(R.id.img1);
        img2 = (RoundedImageViewWhiteBorder)findViewById(R.id.img2);
        img3 = (RoundedImageViewWhiteBorder)findViewById(R.id.img3);
        imgTickCross = (ImageView)findViewById(R.id.imgTickCross);

        rel_cross = (RelativeLayout)findViewById(R.id.rel_cross);

        like = (LinearLayout)findViewById(R.id.like);
        skip = (LinearLayout)findViewById(R.id.skip);

        textView1 = (TextView)findViewById(R.id.textView1);
        description = (TextView)findViewById(R.id.description);

        looking_for_list = (ListView)findViewById(R.id.looking_for_list);
        looking_for_list.setFocusable(false);

        Log.e("Criteria>>", dataModel.userBean.getUserCriteriaBeanArrayList().toString());

        looking_for_list.setAdapter(new UserCriteriaAdapter(dataModel.userBean.getUserCriteriaBeanArrayList(), getApplicationContext()));


        if (user_pos==0){
            rel_cross.setVisibility(View.GONE);
            img2.setAlpha(0.3f);
            img3.setAlpha(0.3f);
        }else{
            rel_cross.setVisibility(View.VISIBLE);
            img2.setAlpha(1.0f);
            img3.setAlpha(0.3f);

            if (dataModel.dashboardUserBeanArrayList.get(user_pos-1).is_liked==1){
                imgTickCross.setImageResource(R.drawable.ic_done_white_24dp);
            }else if (dataModel.dashboardUserBeanArrayList.get(user_pos-1).is_skipped==1){
                imgTickCross.setImageResource(R.drawable.ic_clear_white_24dp);
            }
        }

        View header = navigationView.getHeaderView(0);
        imageView = (RoundedImageViewWhiteBorder)header.findViewById(R.id.imageView);

        Log.e("Img>>",img_url + "/" + dataModel.userBean.getImage());
        if (!dataModel.userBean.getImage().equals("")) {
            if (imageView != null) {
                new ImageDownloaderTask(imageView).execute(img_url + "/" + dataModel.userBean.getImage());
            }
        }

        pager = (ViewPager)findViewById(R.id.pager);

        like.setOnClickListener(this);
        skip.setOnClickListener(this);

        fetchDashboardData();
    }

    private void fetchDashboardData(){

        start_progress_dialog();
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        String user_id= SharedStorage.getValue(getApplicationContext(),"UserId");
        arrayList.add(new org.apache.http.message.BasicNameValuePair("id", user_id));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("looking_for", dataModel.userBean.getType()));

        RemoteAsync remoteAsync = new RemoteAsync(Urls.dashBoard_Data);
        remoteAsync.type = RemoteAsync.DASHBOARD_DATA;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            Intent intent=new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_FROM_BACKGROUND);
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id==R.id.message){
            startActivity(new Intent(DashboardActivity.this,ChatUsersActivity.class));
        }

        if (id==R.id.refer_friend){
            startActivity(new Intent(DashboardActivity.this,ReferFriendActivity.class));
        }

        if (id==R.id.my_likes){
            startActivity(new Intent(DashboardActivity.this,WhoILikeActivity.class));
        }

        if (id==R.id.visitors){
            startActivity(new Intent(DashboardActivity.this,VisitorsActivity.class));
        }

        if (id==R.id.sign_out){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Logout");
            alertDialogBuilder.setMessage("Are you sure,You want to logout");

            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    SharedStorage.setValue(getApplicationContext(),"UserId","");
                    dataModel.search_user=0;
                    startActivity(new Intent(DashboardActivity.this,ExistingUserSignin.class));
                }
            });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        if (type.equals(RemoteAsync.DASHBOARD_DATA)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    JSONArray dataArr=obj.getJSONArray("message");
                    ArrayList<DashboardUserBean> dashboardUserBeanArrayList=new ArrayList<DashboardUserBean>();

                    if (dataArr.length()>0){
                        for (int i=0;i<dataArr.length();i++){
                            JSONObject dataObj=dataArr.getJSONObject(i);
                            DashboardUserBean dashboardUserBean=new DashboardUserBean();

                            JSONObject profileObj = dataObj.getJSONObject("profile");

                            dashboardUserBean.setId(profileObj.getString("id"));
                            dashboardUserBean.setFlag(profileObj.getString("flag"));
                            dashboardUserBean.setName(profileObj.getString("name"));
                            dashboardUserBean.setEmail(profileObj.getString("email"));
                            dashboardUserBean.setUsername(profileObj.getString("username"));
                            dashboardUserBean.setDescription(profileObj.getString("description"));
                            dashboardUserBean.setImage(profileObj.getString("image"));
                            dashboardUserBean.setDate_of_birth(profileObj.getString("date_of_birth"));
                            dashboardUserBean.setLocation(profileObj.getString("location"));
                            dashboardUserBean.setStatus(profileObj.getString("status"));

                            JSONArray profileImgArr = dataObj.getJSONArray("image");
                            ArrayList<DashboardUserImageBean> dashboardUserImageBeanArrayList=new ArrayList<DashboardUserImageBean>();

                            if (profileImgArr.length()>0){
                                for (int j=0;j<profileImgArr.length();j++){
                                    JSONObject imgObj= profileImgArr.getJSONObject(j);
                                    DashboardUserImageBean dashboardUserImageBean=new DashboardUserImageBean();

                                    dashboardUserImageBean.setUser_id(imgObj.getString("user_id"));
                                    dashboardUserImageBean.setImage(imgObj.getString("image"));

                                    dashboardUserImageBeanArrayList.add(dashboardUserImageBean);
                                }
                            }

                            dashboardUserBean.setDashboardUserImageBeanArrayList(dashboardUserImageBeanArrayList);
                            dashboardUserBeanArrayList.add(dashboardUserBean);
                        }
                    }

                    dataModel.dashboardUserBeanArrayList=dashboardUserBeanArrayList;

                    description.setText(dataModel.dashboardUserBeanArrayList.get(user_pos).getDescription());
                    if (user_pos==0){
                        if (img1 != null) {
                            new ImageDownloaderTask(img1).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(user_pos).getImage());
                        }
                        if (img2 != null) {
                            new ImageDownloaderTask(img2).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(user_pos+1).getImage());
                        }
                        if (img3 != null) {
                            new ImageDownloaderTask(img3).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(user_pos+2).getImage());
                        }
                    }else{
                        if (img1 != null) {
                            new ImageDownloaderTask(img1).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(user_pos-1).getImage());
                        }
                        if (img2 != null) {
                            new ImageDownloaderTask(img2).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(user_pos).getImage());
                        }
                        if (img3 != null) {
                            new ImageDownloaderTask(img3).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(user_pos+1).getImage());
                        }
                    }


                    customPagerAdapter=new CustomPagerAdapter(getApplicationContext(),dataModel.dashboardUserBeanArrayList.get(user_pos).getDashboardUserImageBeanArrayList());
                    pager.setAdapter(customPagerAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (type.equals(RemoteAsync.LIKE_MEMBER)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    dataModel.dashboardUserBeanArrayList.get(user_pos).is_liked=1;
                    user_pos+=1;
                    manageDashImagePositions();
                    dataModel.search_user+=1;

                    if (dataModel.search_user>=3) {
                        setTimer();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (type.equals(RemoteAsync.DISLIKE_MEMBER)){
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    dataModel.dashboardUserBeanArrayList.get(user_pos).is_skipped = 1;
                    user_pos += 1;
                    manageDashImagePositions();
                    dataModel.search_user += 1;

                    if (dataModel.search_user >= 3) {
                        setTimer();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void manageDashImagePositions(){
        if (user_pos==0){
            if (img1 != null) {
                new ImageDownloaderTask(img1).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(user_pos).getImage());
            }
            if (img2 != null) {
                new ImageDownloaderTask(img2).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(user_pos+1).getImage());
            }
            if (img3 != null) {
                new ImageDownloaderTask(img3).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(user_pos+2).getImage());
            }

            description.setText(dataModel.dashboardUserBeanArrayList.get(user_pos).getDescription());
        }else{
            if (img1 != null) {
                new ImageDownloaderTask(img1).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(user_pos-1).getImage());
            }
            if (img2 != null) {
                new ImageDownloaderTask(img2).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(user_pos).getImage());
            }
            if (img3 != null) {
                new ImageDownloaderTask(img3).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(user_pos+1).getImage());
            }

            description.setText(dataModel.dashboardUserBeanArrayList.get(user_pos).getDescription());
        }

        customPagerAdapter=new CustomPagerAdapter(getApplicationContext(),dataModel.dashboardUserBeanArrayList.get(user_pos).getDashboardUserImageBeanArrayList());
        pager.setAdapter(customPagerAdapter);

        if (user_pos==0){
            rel_cross.setVisibility(View.GONE);
            img2.setAlpha(0.3f);
            img3.setAlpha(0.3f);
        }else{
            rel_cross.setVisibility(View.VISIBLE);
            img2.setAlpha(1.0f);
            img3.setAlpha(0.3f);

            if (dataModel.dashboardUserBeanArrayList.get(user_pos-1).is_liked==1){
                imgTickCross.setImageResource(R.drawable.ic_done_white_24dp);
            }else if (dataModel.dashboardUserBeanArrayList.get(user_pos-1).is_skipped==1){
                imgTickCross.setImageResource(R.drawable.ic_clear_white_24dp);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.like:
                if (dataModel.search_user>=3) {
                    FragmentManager fm = getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                }else{
                    likeMember();
                }

                break;
            case R.id.skip:
                if (dataModel.search_user>=3) {
                    FragmentManager fm = getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                }else {
                   skipMember();
                }
                break;
        }
    }

    private void likeMember(){
        start_progress_dialog();
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        String user_id= SharedStorage.getValue(getApplicationContext(),"UserId");
        arrayList.add(new org.apache.http.message.BasicNameValuePair("user_id", user_id));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("member_id", dataModel.dashboardUserBeanArrayList.get(user_pos).getId()));

        RemoteAsync remoteAsync = new RemoteAsync(Urls.like_Member);
        remoteAsync.type = RemoteAsync.LIKE_MEMBER;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }

    private void skipMember(){
        start_progress_dialog();
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        String user_id= SharedStorage.getValue(getApplicationContext(),"UserId");
        arrayList.add(new org.apache.http.message.BasicNameValuePair("user_id", user_id));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("member_id", dataModel.dashboardUserBeanArrayList.get(user_pos).getId()));

        RemoteAsync remoteAsync = new RemoteAsync(Urls.dislike_Member);
        remoteAsync.type = RemoteAsync.DISLIKE_MEMBER;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }


    private void setTimer(){
        new CountDownTimer(86400000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                textView1.setText("Get next 3 swipes within: "+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                textView1.setText("done!");
            }
        }.start();


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<DashboardUserImageBean> dashboardUserImageBeanArrayList = new ArrayList<DashboardUserImageBean>();

        public CustomPagerAdapter(Context context, ArrayList<DashboardUserImageBean> dashboardUserImageBeanArrayList) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.dashboardUserImageBeanArrayList=dashboardUserImageBeanArrayList;
        }

        @Override
        public int getCount() {
            return dashboardUserImageBeanArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            DashboardUserImageBean dashboardUserImageBean=dashboardUserImageBeanArrayList.get(position);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            //imageView.setImageResource(mResources[position]);
            if (imageView != null) {
                new ImageDownloaderTask(imageView).execute(img_url+"/"+dashboardUserImageBean.getImage());
            }

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
