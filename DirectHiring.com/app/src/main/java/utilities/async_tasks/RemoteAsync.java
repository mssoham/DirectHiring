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
    public static final String ALL_SERVICE = "allservice";
    public static final String ZIP_CODE = "zipcode";
    public static final String SERVICE_DETAILS = "service_details";
    public static final String PLACE_ORDER = "place_order";
    public static final String COUPON = "coupon";
    public static final String LOGIN = "login";
    public static final String UPDATE_PROFILE = "update_profile";
    public static final String ORDER_DETAILS = "order_details";
    public static final String CONFIG_SPINNER = "config";

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

        HttpConnection connection = new HttpConnection();
        string_JSON = connection.getPostRespoonse(url, pairs[0]);

        return string_JSON;



    }

    @Override
    protected void onPostExecute(String jsonString) {
        delegate.processFinish(type, jsonString);
    }

}
