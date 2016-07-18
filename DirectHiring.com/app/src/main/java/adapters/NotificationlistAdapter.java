package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import afroradix.xigmapro.com.directhiringcom.R;
import custom_components.LikeHolder;
import custom_components.NotificationHolder;
import utilities.async_tasks.ImageDownloaderTask;
import utilities.data_objects.LikeBean;
import utilities.data_objects.NotificationBean;

/**
 * Created by xyxz on 7/14/2016.
 */
public class NotificationlistAdapter extends BaseAdapter {
    ArrayList<NotificationBean> notificationBeanArrayList = new ArrayList<NotificationBean>();
    Context context;
    LayoutInflater inflater;

    private String img_url="http://xigmapro.website/dev4/directhiring/public/resource/site/images/users/";

    public NotificationlistAdapter(ArrayList<NotificationBean> notificationBeanArrayList, Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.notificationBeanArrayList = notificationBeanArrayList;
    }

    @Override
    public int getCount() {
        return notificationBeanArrayList.size();
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
        //Holder holder = null;
        NotificationBean notificationBean = notificationBeanArrayList.get(position);

        if (convertView == null) {
            inflater = getLayoutInflater();
            //holder = new Holder();
            convertView = inflater.inflate(R.layout.inflate_notification_layout, parent, false);
            notificationBean.holder.user_name_noti = (TextView) convertView.findViewById(R.id.user_name_noti);
            notificationBean.holder.user_img_noti = (ImageView) convertView.findViewById(R.id.user_img_noti);
            notificationBean.holder.user_info_location = (TextView) convertView.findViewById(R.id.user_info_location);
            notificationBean.holder.accept = (Button) convertView.findViewById(R.id.accept);
            notificationBean.holder.decline = (Button) convertView.findViewById(R.id.decline);

            convertView.setTag(notificationBean.holder);
        } else {
            notificationBean.holder = (NotificationHolder) convertView.getTag();
        }

        notificationBean.holder.user_name_noti.setText(notificationBean.getName());
        notificationBean.holder.user_info_location.setText(notificationBean.getAge()+" - "+notificationBean.getLocation());
        if (notificationBean.holder.user_img_noti != null) {
            new ImageDownloaderTask(notificationBean.holder.user_img_noti).execute(img_url+"/"+notificationBean.getImage());
        }

        return convertView;
    }

    private LayoutInflater getLayoutInflater() {
        return inflater;
    }

    /*class Holder {

        ImageView user_img;
        TextView user_name, user_info;
    }*/

}
