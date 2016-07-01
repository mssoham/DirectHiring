package utilities.others;

import android.app.Activity;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

import utilities.constants.Constants;


public class CommonParams
{
	public static ArrayList<NameValuePair> getListNameValuePair(Activity activity)
	{
       /* String device_id = new PushRegId(
                activity.getApplicationContext()).getRegistrationId(activity
                .getApplicationContext());*/
        //Log.e("DEVICE ID ### ", device_id);

		ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

		//arrayList.add(new org.apache.http.message.BasicNameValuePair("user_id", SharedStorage.getValue(activity, SharedStorage.UserId)));
		arrayList.add(new org.apache.http.message.BasicNameValuePair("device_type", Constants.ANDROID));
		//arrayList.add(new org.apache.http.message.BasicNameValuePair("device_id", device_id));
		
		return arrayList;
	}
}
