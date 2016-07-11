package afroradix.xigmapro.com.directhiringcom;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
        implements NavigationView.OnNavigationItemSelectedListener, AsyncResponse {

    private RoundedImageViewWhiteBorder img1,img2,img3,imageView;
    private ViewPager pager;
    private String img_url="http://xigmapro.website/dev4/directhiring/public/resource/site/images/users/";

    ProgressDialog progressDialog;
    DirectHiringModel dataModel=DirectHiringModel.getInstance();
    CustomPagerAdapter customPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        View header = navigationView.getHeaderView(0);
        imageView = (RoundedImageViewWhiteBorder)header.findViewById(R.id.imageView);

        if (imageView != null) {
            new ImageDownloaderTask(imageView).execute(img_url+"/"+dataModel.userBean.getImage());
        }

        pager = (ViewPager)findViewById(R.id.pager);

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
            super.onBackPressed();
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
        if (id == R.id.action_settings) {
            return true;
        }

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

                    if (img1 != null) {
                        new ImageDownloaderTask(img1).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(0).getImage());
                    }
                    if (img2 != null) {
                        new ImageDownloaderTask(img2).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(1).getImage());
                    }
                    if (img3 != null) {
                        new ImageDownloaderTask(img3).execute(img_url+"/"+dataModel.dashboardUserBeanArrayList.get(2).getImage());
                    }

                    customPagerAdapter=new CustomPagerAdapter(getApplicationContext(),dataModel.dashboardUserBeanArrayList.get(1).getDashboardUserImageBeanArrayList());
                    pager.setAdapter(customPagerAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
