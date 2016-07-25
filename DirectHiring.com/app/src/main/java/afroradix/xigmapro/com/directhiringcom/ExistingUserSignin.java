package afroradix.xigmapro.com.directhiringcom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.view.MenuItem;

import org.apache.http.NameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import shared_pref.SharedStorage;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.DashboardUserBean;
import utilities.data_objects.DashboardUserImageBean;
import utilities.data_objects.DirectHiringModel;
import utilities.data_objects.UserBean;
import utilities.data_objects.UserCriteriaBean;
import utilities.data_objects.UserPhotosLoadBean;
import utilities.others.CToast;

public class ExistingUserSignin extends AppCompatActivity implements View.OnClickListener, AsyncResponse {
    private EditText user_name, input_password;
    private TextInputLayout input_layout_user_name, input_layout_password;

    String username,password,email_id="",social_id="";
    Button signin_btn;
    TextView signin_facebook;
    ProgressDialog progressDialog;
    DirectHiringModel dataModel = DirectHiringModel.getInstance();
    String first_name,last_name,email="",facebook_id="",module="";
    String uuid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_user_signin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Welcome");
        input_layout_user_name = (TextInputLayout) findViewById(R.id.input_layout_user_name);
        input_layout_password = (TextInputLayout) findViewById(R.id.input_layout_password);
        user_name = (EditText) findViewById(R.id.user_name);
        input_password = (EditText) findViewById(R.id.input_password);
        user_name.addTextChangedListener(new MyTextWatcher(user_name));
        input_password.addTextChangedListener(new MyTextWatcher(input_password));
        signin_btn=(Button)findViewById(R.id.signin_btn);
        signin_facebook = (TextView)findViewById(R.id.signin_facebook);

        input_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (input_password.getRight() - input_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        startActivity(new Intent(ExistingUserSignin.this, ForgotPassword.class));
                        return true;
                    }
                }
                return false;
            }
        });
       /* signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CToast.show(getApplicationContext(),"Clicked on Signin");
                submitForm();
            }
        });*/
        signin_btn.setOnClickListener(this);
        signin_facebook.setOnClickListener(this);


        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        uuid = tManager.getDeviceId();
        Log.e("uuid>>", uuid);
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        CToast.show(getApplicationContext(), "Successfully signin");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signin_btn:
                login();
                break;
            case R.id.signin_facebook:
                module="facebook";
                Intent fbIntent=new Intent(ExistingUserSignin.this,SocialLoginActivity.class);
                fbIntent.putExtra("LOGIN_TYPE",module);

                startActivityForResult(fbIntent,440);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 440) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra("result");
                SetUserDetails(result);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void SetUserDetails(ArrayList<String> msg){
        first_name=msg.get(0).toString();
        last_name=msg.get(1).toString();
        email=(msg.get(2).toString());
        facebook_id=(msg.get(3).toString());

        //joinSocial();
        login();
    }

    private void login(){
       // if (user_name.getText().toString().equals("") || input_password.getText().toString().equals("")){
            start_progress_dialog();

            ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

            arrayList.add(new org.apache.http.message.BasicNameValuePair("username", user_name.getText().toString()));
            arrayList.add(new org.apache.http.message.BasicNameValuePair("password", input_password.getText().toString()));
            arrayList.add(new org.apache.http.message.BasicNameValuePair("ip", uuid));
            arrayList.add(new org.apache.http.message.BasicNameValuePair("social_id", facebook_id));
            arrayList.add(new org.apache.http.message.BasicNameValuePair("email", email));

            RemoteAsync remoteAsync = new RemoteAsync(Urls.login);
            remoteAsync.type = RemoteAsync.LOGIN;
            remoteAsync.delegate = this;
            remoteAsync.execute(arrayList);
        /*}else{
            CToast.show(getApplicationContext(),"Mandetory fields should not be blank");
        }*/
    }

    @Override
    public void processFinish(String type, String output) {
        stop_progress_dialog();
        email="";
        facebook_id="";
        if (type.equals(RemoteAsync.LOGIN)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    JSONObject detailsObj=obj.getJSONObject("details");
                    JSONObject userObj=detailsObj.getJSONObject("users");
                    JSONArray criteriaArr = detailsObj.getJSONArray("criteria");

                    UserBean userBean=new UserBean();

                    userBean.setUser_id(userObj.getString("id"));
                    userBean.setSocial_id(userObj.getString("social_id"));
                    userBean.setFlag(userObj.getString("flag"));
                    userBean.setName(userObj.getString("name"));
                    userBean.setEmail(userObj.getString("email"));
                    userBean.setUsername(userObj.getString("username"));
                    userBean.setImage(userObj.getString("image"));
                    userBean.setDescription(userObj.getString("description"));
                    userBean.setDate_of_birth(userObj.getString("date_of_birth"));
                    userBean.setLocation(userObj.getString("location"));
                    userBean.setLooking_for(userObj.getString("looking_for"));
                    userBean.setType(userObj.getString("type"));
                    userBean.setStatus(userObj.getString("status"));
                    userBean.setRemember_token(userObj.getString("remember_token"));
                    /*userBean.setWallet(userObj.getString("wallet"));*/
                    JSONArray user_photos=detailsObj.getJSONArray("photos");
                    UserPhotosLoadBean userPhotosLoadBean=new UserPhotosLoadBean();
                    ArrayList<UserPhotosLoadBean> userPhotosLoadBeanArrayList=new ArrayList<UserPhotosLoadBean>();
                    if (user_photos.length()>0){
                        for (int j=0;j<user_photos.length();j++){
                            Log.e("loop count","count");
                            JSONObject imgObj= user_photos.getJSONObject(j);
                            UserPhotosLoadBean usersPhotosLoadBean=new UserPhotosLoadBean();

                            usersPhotosLoadBean.setId(imgObj.getString("id"));
                            usersPhotosLoadBean.setImage(imgObj.getString("img"));

                            userPhotosLoadBeanArrayList.add(usersPhotosLoadBean);
                        }
                    }
                    userPhotosLoadBean.setUserPhotosLoadBeanArrayList(userPhotosLoadBeanArrayList);
                    userBean.setUserPhotosLoadBeanArrayList(userPhotosLoadBeanArrayList);
                    ArrayList<UserCriteriaBean> userCriteriaBeans=new ArrayList<UserCriteriaBean>();
                    if (criteriaArr.length()>0){
                        for (int i = 0; i<criteriaArr.length();i++){
                            UserCriteriaBean userCriteriaBean=new UserCriteriaBean();
                            JSONObject userC = criteriaArr.getJSONObject(i);

                            userCriteriaBean.setKey(userC.getString("key"));
                            userCriteriaBean.setValue(userC.getString("value"));

                            userCriteriaBeans.add(userCriteriaBean);
                        }
                    }

                    userBean.setUserCriteriaBeanArrayList(userCriteriaBeans);

                    dataModel.userBean=userBean;
                    SharedStorage.setValue(getApplicationContext(),"UserId",userObj.getString("id"));

                    startActivity(new Intent(ExistingUserSignin.this, DashboardActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.user_name:
                    validateName();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }
    private boolean validateName() {
        username=user_name.getText().toString().trim();
        if (username.isEmpty()|| !isValidEmail(username)) {
            input_layout_user_name.setError(getString(R.string.err_msg_name));
            requestFocus(user_name);
            return false;
        } else {
            input_layout_user_name.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        password=input_password.getText().toString().trim();
        if (password.isEmpty()) {
            input_layout_password.setError(getString(R.string.err_msg_password));
            requestFocus(input_password);
            return false;
        } else {
            input_layout_password.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String username) {
        return !TextUtils.isEmpty(username) && android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
    public void onBackPressed() {
        Intent intent=new Intent(ExistingUserSignin.this,ImageSliderScreen.class);
        startActivity(intent);
    }
}
