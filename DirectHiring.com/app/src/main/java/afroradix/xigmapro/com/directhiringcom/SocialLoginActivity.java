package afroradix.xigmapro.com.directhiringcom;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class SocialLoginActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // for facebook
    CallbackManager callbackManager;
    String first_name,last_name,email,id;
    //for google +
    private boolean mSignInClicked=true;

    private GoogleApiClient mGoogleApiClient;

    private ConnectionResult mConnectionResult;
    public static final int REQUEST_CODE_RESOLVE_ERR = 1005;
    ArrayList<String> arrayList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_social);
        String login_type=getIntent().getExtras().getString("LOGIN_TYPE");
        //..............................for Google+........................................

        Plus.PlusOptions options = Plus.PlusOptions.builder().build();
        mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, options)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        /**
         * .....................Signup using facebook...........................................
         */

        callbackManager = CallbackManager.Factory.create();

        //loginButton.setReadPermissions("public_profile");
        //loginButton.setReadPermissions(Arrays.asList("email"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        String accessToken = loginResult.getAccessToken().getToken();
                        String userId = loginResult.getAccessToken().getUserId();

                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                try {

                                    id = jsonObject.getString("id");
                                    first_name = jsonObject.getString("first_name");
                                    last_name = jsonObject.getString("last_name");
                                    email = jsonObject.getString("email");
                                    arrayList.add(first_name);
                                    arrayList.add(last_name);
                                    arrayList.add(email);
                                    arrayList.add(id);
                                    LoginManager.getInstance().logOut();
                                    Intent returnIntent = new Intent();
                                    returnIntent.putStringArrayListExtra("result", arrayList);
                                    setResult(RESULT_OK, returnIntent);
                                    finish();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,email,name");
                        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
                        graphRequest.setParameters(parameters);
                        graphRequest.executeAsync();
                        // progres Animation apply
                        // App code
                        return;
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", getResources().getString(R.string.error_msg));
                        setResult(-2, returnIntent);
                        finish();
                        return;

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", getResources().getString(R.string.error_msg));
                        setResult(-2, returnIntent);
                        finish();
                        return;
                    }
                });
        if(login_type.equals("facebook")) {

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        }
        if(login_type.equals("google")){
            mGoogleApiClient.connect();
        }
    }
    //.............................. Google plus..................
    public void onStart() {
        super.onStart();
        // mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    //.............................Google Plus........................
//......................Google Plus................................................
    private void getUserDetails() {
        if (mGoogleApiClient.isConnected()) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            if (currentPerson == null) {

            } else {

                Person.Name name = currentPerson.getName();
                first_name= name.getGivenName();
                last_name=name.getFamilyName();
                email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                arrayList.add(first_name);
                arrayList.add(last_name);
                arrayList.add(email);
                arrayList.add("");
                Intent returnIntent = new Intent();
                returnIntent.putStringArrayListExtra("result", arrayList);
                setResult(RESULT_OK,returnIntent);
                finish();


            }
        }
    }
    private void logOut() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);

            mGoogleApiClient.disconnect();

            setResult(Activity.RESULT_OK);

        }
    }
    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        try {
            if (mConnectionResult != null && mConnectionResult.hasResolution()) {
                mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            }
        }  catch (IntentSender.SendIntentException e) {
            mConnectionResult = null;
            mGoogleApiClient.connect();
        }
    }



    @Override
    public void onConnected(Bundle bundle) {

        mSignInClicked = false;
        getUserDetails();

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Save the intent so that we can start an activity when the user clicks
        // the sign-in button.
        mConnectionResult = result;
        if (mSignInClicked) {
            resolveSignInError() ;
        }

    }

//.............................Google Plus...............................






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR) {
            if (resultCode == Activity.RESULT_OK) {
                if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();

                }
            }
        }
        else
            callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
