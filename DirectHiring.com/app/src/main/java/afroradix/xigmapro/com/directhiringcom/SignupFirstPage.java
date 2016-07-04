package afroradix.xigmapro.com.directhiringcom;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.CountryLoadSpinnerAdapter;
import adapters.DateOfBirthadapter;
import adapters.TypeSpinnerAdapter;
import shared_pref.SharedStorage;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.DirectHiringModel;
import utilities.data_objects.UserBean;
import utilities.others.CToast;

public class SignupFirstPage extends AppCompatActivity implements View.OnClickListener, AsyncResponse {
    private EditText user_first_name, input_last_name,input_email,confirm_email;
    private TextInputLayout input_layout_first_name, input_layout_last_name,input_layout_email,input_confirm_email;
    String firstname,lastname,date_of_birth,email,confirm_email1,country1,social_id,type,looking_for;
    Button sign_up;
    TextView signup_facebook;
    Spinner date,month,year,country;
    CountryLoadSpinnerAdapter adapter;
    DateOfBirthadapter dateadapter,monthadapter,yearadapter;
    ProgressDialog progressDialog;
    DirectHiringModel dataModel = DirectHiringModel.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_first_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Back");
        input_layout_first_name = (TextInputLayout) findViewById(R.id.input_layout_first_name);
        input_layout_last_name = (TextInputLayout) findViewById(R.id.input_layout_last_name);
        input_layout_email = (TextInputLayout) findViewById(R.id.input_layout_email);
        input_confirm_email = (TextInputLayout) findViewById(R.id.input_confirm_email);
        user_first_name = (EditText) findViewById(R.id.user_first_name);
        input_last_name = (EditText) findViewById(R.id.input_last_name);
        input_email = (EditText) findViewById(R.id.input_email);
        confirm_email = (EditText) findViewById(R.id.confirm_email);
        user_first_name.addTextChangedListener(new MyTextWatcher(user_first_name));
        input_last_name.addTextChangedListener(new MyTextWatcher(input_last_name));
        input_email.addTextChangedListener(new MyTextWatcher(input_email));
        confirm_email.addTextChangedListener(new MyTextWatcher(confirm_email));
        sign_up=(Button)findViewById(R.id.sign_up);
        signup_facebook=(TextView)findViewById(R.id.signup_facebook);
        sign_up.setOnClickListener(this);
        signup_facebook.setOnClickListener(this);
        date=(Spinner)findViewById(R.id.date);
        month=(Spinner)findViewById(R.id.month);
        year=(Spinner)findViewById(R.id.year);
        country=(Spinner)findViewById(R.id.country);
        adapter = new CountryLoadSpinnerAdapter(this, DirectHiringModel.getInstance().countryLoadBeanArrayList);
        country.setAdapter(adapter);
        dateadapter = new DateOfBirthadapter(this, DirectHiringModel.getInstance().dateArrayList);
        date.setAdapter(dateadapter);
        monthadapter = new DateOfBirthadapter(this, DirectHiringModel.getInstance().monthArrayList);
        month.setAdapter(monthadapter);
        yearadapter = new DateOfBirthadapter(this, DirectHiringModel.getInstance().yearArrayList);
        year.setAdapter(yearadapter);
        date_of_birth=date.getSelectedItem().toString().trim()+'-'+month.getSelectedItem().toString().trim()+'-'+year.getSelectedItem().toString().trim();
        country1=country.getSelectedItem().toString().trim();
        social_id="0";
        if(TypeSelectionSignUp.register_type.equals("Helper")){
            type="Helper";
            looking_for="Family";
        }else{
            type="Family";
            looking_for="Helper";
        }

    }

    @Override
    public void onClick(View v) {
        if(v==sign_up){
            submitForm();
            if(country1.equals("Select Country") && email.equals(confirm_email1)){
                signup();

            }else{
                CToast.show(getApplicationContext(),"Enter valid Country name!");
            }
        }
        if(v==signup_facebook){

        }

    }
    private void submitForm() {
        if (!validateFirstName()) {
            return;
        }
        if (!validateLastName()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }

        if (!validateConfirmEmail()) {
            return;
        }
        CToast.show(getApplicationContext(), "Successfully signup");
    }

    @Override
    public void processFinish(String type, String output) {
        if (type.equals(RemoteAsync.REGISTRATION)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    //startActivity(new Intent(ServiceDetailsActivity.this,OrderSuccessfulActivity.class));
                    JSONObject userObj=obj.getJSONObject("users");
                    UserBean userBean=new UserBean();
                    userBean.setUser_id(userObj.getString("id"));
                    userBean.setSocial_id(userObj.getString("social_id"));
                    userBean.setFlag(userObj.getString("flag"));
                    userBean.setName(userObj.getString("name"));
                    userBean.setEmail(userObj.getString("email"));
                    userBean.setDate_of_birth(userObj.getString("date_of_birth"));
                    userBean.setLocation(userObj.getString("location"));
                    userBean.setLooking_for(userObj.getString("looking_for"));
                    userBean.setType(userObj.getString("type"));
                    userBean.setStatus(userObj.getString("status"));
                    userBean.setWallet(userObj.getString("wallet"));
                    userBean.setRemember_token(userObj.getString("remember_token"));
                    userBean.setCreated_at(userObj.getString("created_at"));
                    userBean.setUpdated_at(userObj.getString("updated_at"));

                    dataModel.user=userBean;
                    SharedStorage.setValue(getApplicationContext(), "UserId", userObj.getString("id"));

                    //ShowAlertDialog.showAlertDialog(getApplicationContext(),"Profile updated successfully");
                    CToast.show(getApplicationContext(),"Profile created successfully");
                }else{
                    CToast.show(getApplicationContext(),"Failed to create profile");
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
                case R.id.user_first_name:
                    validateFirstName();
                    break;
                case R.id.input_last_name:
                    validateLastName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.confirm_email:
                    validateConfirmEmail();
                    break;
            }
        }
    }
    private boolean validateFirstName() {
        firstname=user_first_name.getText().toString().trim();
        if (firstname.isEmpty()) {
            input_layout_first_name.setError(getString(R.string.err_msg_name));
            requestFocus(input_layout_first_name);
            return false;
        } else {
            input_layout_first_name.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLastName() {
        lastname=input_last_name.getText().toString().trim();
        if (lastname.isEmpty()) {
            input_layout_last_name.setError(getString(R.string.err_msg_password));
            requestFocus(input_layout_last_name);
            return false;
        } else {
            input_layout_last_name.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateEmail() {
        email=input_email.getText().toString().trim();
        if (email.isEmpty()|| !isValidEmail(email)) {
            input_layout_email.setError(getString(R.string.err_msg_password));
            requestFocus(input_layout_email);
            return false;
        } else {
            input_layout_email.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateConfirmEmail() {
        confirm_email1=confirm_email.getText().toString().trim();
        if (confirm_email1.isEmpty()|| !isValidEmail(confirm_email1)) {
            input_confirm_email.setError(getString(R.string.err_msg_password));
            requestFocus(input_confirm_email);
            return false;
        } else {
            input_confirm_email.setErrorEnabled(false);
        }

        return true;
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

    private static boolean isValidEmail(String username) {
        return !TextUtils.isEmpty(username) && android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private void signup(){
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new org.apache.http.message.BasicNameValuePair("social_id", social_id));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("first_name",user_first_name.getText().toString().trim() ));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("last_name", input_last_name.getText().toString().trim()));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("date_of_birth", date_of_birth));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("location", country1));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("email", input_email.getText().toString().trim()));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("type", type));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("looking_for", looking_for));
        //Urls.urlkey=url_key;
        RemoteAsync remoteAsync = new RemoteAsync(Urls.registration);
        remoteAsync.type = RemoteAsync.REGISTRATION;
        remoteAsync.delegate=this;
        remoteAsync.execute(arrayList);
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

}

