package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import afroradix.xigmapro.com.directhiringcom.R;
import custom_components.LikeHolder;
import utilities.async_tasks.ImageDownloaderTask;
import utilities.data_objects.LikeBean;
import utilities.data_objects.UserCriteriaBean;

/**
 * Created by Self-3 on 7/12/2016.
 */
public class UserCriteriaAdapter extends BaseAdapter {
    ArrayList<UserCriteriaBean> userCriteriaBeanArrayList = new ArrayList<UserCriteriaBean>();
    Context context;
    LayoutInflater inflater;

    private String img_url="http://xigmapro.website/dev4/directhiring/public/resource/site/images/users/";

    public UserCriteriaAdapter(ArrayList<UserCriteriaBean> userCriteriaBeanArrayList, Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.userCriteriaBeanArrayList = userCriteriaBeanArrayList;
    }

    @Override
    public int getCount() {
        return userCriteriaBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        UserCriteriaBean userCriteriaBean = userCriteriaBeanArrayList.get(position);

        if (convertView == null) {
            inflater = getLayoutInflater();
            holder = new Holder();
            convertView = inflater.inflate(R.layout.inflate_user_criteria, parent, false);
            holder.key = (TextView) convertView.findViewById(R.id.key);
            holder.value = (TextView) convertView.findViewById(R.id.value);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.key.setText(userCriteriaBean.getKey());
        holder.value.setText(userCriteriaBean.getValue());

        return convertView;
    }

    private LayoutInflater getLayoutInflater() {
        return inflater;
    }

    class Holder {

        ImageView user_img;
        TextView key, value;
    }

}
