package utilities.async_tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

public class RemoteAsync extends AsyncTask<ArrayList<NameValuePair>, Void, String> {
    private String string_JSON;

    /**
     * Creating an instance of the {@link AsyncResponse} interface to receive
     * {@code processFinish()}
     */
    public AsyncResponse delegate = null;
    private String url;
    public String type = "";


    // Constants
    public static final String CONFIG_SPINNER = "config";
    public static final String LOGIN = "login";
    public static final String REGISTRATION = "registration";
    public static final String REGISTRATION_HELPER = "registration_helper";
    public static final String IMAGE_UPLOAD = "self";
    public static final String DASHBOARD_DATA = "dashBoardData";

    /* Services for chat start */
    public static final String GET_CHAT_USERS = "getChatUsers";
    public static final String GET_CHAT_MESSAGES = "getChatMessages";
    public static final String SEND_MESSAGES = "sendMessages";
    public static final String FORGOTPASSWORD = "forgetPassword";
    /* Services for chat end */

    public static final String CRITERIA = "criteria";

    public static final String LIKE_MEMBER = "likeMember";
    public static final String DISLIKE_MEMBER = "dislikeMember";
    public static final String LIKES_MEMBER_LIST = "likesMemberList";
    public static final String VISITORS = "visitors";
    public static final String NOTIFICATION = "notification";
    public static final String FRIENDREQUEST = "friendRequest";
    public static final String CHANGEPROFILEIMAGE ="changeProfileImage";
    public static final String CHANGEDESCRIPTION ="changeDescription";
    public static final String ADDUSERPHOTOS ="addUserPhotos";

    /**
     * Pass the Url of the web service as a String
     * <p/>
     * url
     */
    public RemoteAsync(String url) {
        Log.e("SERVICE URL # ", url);
        this.url = url;
    }

    @Override
    protected String doInBackground(@SuppressWarnings("unchecked") ArrayList<NameValuePair>... pairs) {

       /* HttpConnection connection = new HttpConnection();
        string_JSON = connection.getPostRespoonse(url, pairs[0]);*/

        if(type == IMAGE_UPLOAD || type == CHANGEPROFILEIMAGE||type == ADDUSERPHOTOS){
            MultiPartFileUpload multiPart_fileUpload=new MultiPartFileUpload();
            return multiPart_fileUpload.makeServiceCall(url, pairs[0]);
        } else{
            HttpConnection connection = new HttpConnection();
            string_JSON = connection.getPostRespoonse(url, pairs[0]);
        }

        return string_JSON;



    }

    @Override
    protected void onPostExecute(String jsonString) {
        delegate.processFinish(type, jsonString);
    }

}
