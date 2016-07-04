package afroradix.xigmapro.com.directhiringcom;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;

import shared_pref.SharedStorage;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Urls;
import utilities.data_objects.DirectHiringModel;
import utilities.others.CToast;

public class SignUpSecondPage extends AppCompatActivity implements View.OnClickListener, AsyncResponse {
    private EditText user_name_reg, input_password_reg;
    private TextInputLayout input_layout_user_name_reg, input_layout_password_reg;

    String username, password,user_id;
    Button signin_btn_reg;
    ProgressDialog progressDialog;
    DirectHiringModel dataModel = DirectHiringModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second_page);
        input_layout_user_name_reg = (TextInputLayout) findViewById(R.id.input_layout_user_name_reg);
        input_layout_password_reg = (TextInputLayout) findViewById(R.id.input_layout_password_reg);
        user_name_reg = (EditText) findViewById(R.id.user_name_reg);
        input_password_reg = (EditText) findViewById(R.id.input_password_reg);
        user_name_reg.addTextChangedListener(new MyTextWatcher(user_name_reg));
        input_password_reg.addTextChangedListener(new MyTextWatcher(input_password_reg));
        signin_btn_reg = (Button) findViewById(R.id.signin_btn_reg);
        user_id = SharedStorage.getValue(getApplicationContext(), "UserId");
       /* signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CToast.show(getApplicationContext(),"Clicked on Signin");
                submitForm();
            }
        });*/
        signin_btn_reg.setOnClickListener(this);
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_btn_reg:
                submitForm();
                firstlogin();
                break;
        }
    }
    private void firstlogin(){
        start_progress_dialog();

        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new org.apache.http.message.BasicNameValuePair("last_insert_id", user_id));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("username", user_name_reg.getText().toString()));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("password", input_password_reg.getText().toString()));

        RemoteAsync remoteAsync = new RemoteAsync(Urls.registration_helper);
        remoteAsync.type = RemoteAsync.REGISTRATION_HELPER;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }

    @Override
    public void processFinish(String type, String output) {

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
        username=user_name_reg.getText().toString().trim();
        if (username.isEmpty()|| !isValidEmail(username)) {
            input_layout_user_name_reg.setError(getString(R.string.err_msg_name));
            requestFocus(user_name_reg);
            return false;
        } else {
            input_layout_user_name_reg.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        password=input_password_reg.getText().toString().trim();
        if (password.isEmpty()) {
            input_layout_password_reg.setError(getString(R.string.err_msg_password));
            requestFocus(input_password_reg);
            return false;
        } else {
            input_layout_password_reg.setErrorEnabled(false);
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