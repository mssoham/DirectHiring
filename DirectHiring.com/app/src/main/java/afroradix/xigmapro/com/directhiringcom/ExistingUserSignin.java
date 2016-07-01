package afroradix.xigmapro.com.directhiringcom;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.view.MenuItem;

import org.apache.http.util.TextUtils;

import utilities.others.CToast;

public class ExistingUserSignin extends AppCompatActivity {
    private EditText user_name, input_password;
    private TextInputLayout input_layout_user_name, input_layout_password;
    String username,password;
    Button signin_btn;
    TextView signin_facebook;

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
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CToast.show(getApplicationContext(),"Clicked on Signin");
                submitForm();
            }
        });
    }
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        CToast.show(getApplicationContext(),"Successfully signin");
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
}
