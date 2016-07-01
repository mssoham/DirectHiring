package shared_pref;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedStorage
{
	static SharedPreferences preference;

	private static String prefData="Directhiring";

    public static String UserId= "UserId";

    public static void setValue(Context context,String key,String data){
        preference = context.getSharedPreferences(prefData, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key,data);
        editor.commit();
    }

    public static String getValue(Context context,String key){
        preference = context.getSharedPreferences(prefData, Context.MODE_PRIVATE);
        String id = preference.getString(key,"");
        return id;
    }

    public static void resetValue(Context context){
        preference = context.getSharedPreferences(prefData, Context.MODE_PRIVATE);
        preference.edit().clear().commit();
    }

}