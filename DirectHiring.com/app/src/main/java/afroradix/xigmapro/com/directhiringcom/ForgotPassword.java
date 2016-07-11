package afroradix.xigmapro.com.directhiringcom;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONObject;

import java.util.ArrayList;

import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.DirectHiringModel;
import utilities.others.CToast;

public class ForgotPassword extends AppCompatActivity implements AsyncResponse {

    private Button Reset_password;
    private EditText user_email;
    String user_email1;
    private TextInputLayout input_layout_user_email;
    ProgressDialog progressDialog;
    DirectHiringModel dataModel = DirectHiringModel.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        input_layout_user_email = (TextInputLayout) findViewById(R.id.input_layout_user_email);
        user_email = (EditText) findViewById(R.id.user_email);
        user_email.addTextChangedListener(new MyTextWatcher(user_email));
        Reset_password=(Button)findViewById(R.id.Reset_password);
        Reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    @Override
    public void processFinish(String type, String output) {
        stop_progress_dialog();
        if (type.equals(RemoteAsync.FORGOTPASSWORD)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    AlertDialog.Builder alerBuilder = new AlertDialog.Builder(ForgotPassword.this);
                    alerBuilder.setMessage("Check your mail you will get your password there. Click Continue to proceed");
                    alerBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intMain = new Intent(ForgotPassword.this, ExistingUserSignin.class);
                            startActivity(intMain);
                        }
                    });

                    AlertDialog alertDialog = alerBuilder.create();
                    alertDialog.show();
                }else{
                    CToast.show(getApplicationContext(),"checkyour email id properly use correct emailid!");
                }
            }catch (Exception e){
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
                case R.id.user_email:
                    validateName();
                    break;
            }
        }
    }
    private boolean validateName() {
        user_email1=user_email.getText().toString().trim();
        if (user_email1.isEmpty()|| !isValidEmail(user_email1)) {
            input_layout_user_email.setError(getString(R.string.err_msg_email));
            requestFocus(user_email);
            return false;
        } else {
            input_layout_user_email.setErrorEnabled(false);
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
    private void submitForm() {
        if (!validateName()) {
            return;
        }
        forgotpassword();
    }
    private void forgotpassword(){
        start_progress_dialog();

        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new org.apache.http.message.BasicNameValuePair("email", user_email.getText().toString()));

        RemoteAsync remoteAsync = new RemoteAsync(Urls.forgetPassword);
        remoteAsync.type = RemoteAsync.FORGOTPASSWORD;
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
}
