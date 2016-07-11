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
import utilities.async_tasks.ImageDownloaderTask;
import utilities.data_objects.ChatUsersBean;
import utilities.data_objects.LikeBean;

/**
 * Created by Self-3 on 7/11/2016.
 */
public class LikelistAdapter extends BaseAdapter {
    ArrayList<LikeBean> likeBeanArrayList = new ArrayList<LikeBean>();
    Context context;
    LayoutInflater inflater;

    private String img_url="http://xigmapro.website/dev4/directhiring/public/resource/site/images/users/";

    public LikelistAdapter(ArrayList<LikeBean> likeBeanArrayList, Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.likeBeanArrayList = likeBeanArrayList;
    }

    @Override
    public int getCount() {
        return likeBeanArrayList.size();
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
        LikeBean likeBean = likeBeanArrayList.get(position);

        if (convertView == null) {
            inflater = getLayoutInflater();
            holder = new Holder();
            convertView = inflater.inflate(R.layout.inflate_like, parent, false);
            holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            holder.user_img = (ImageView) convertView.findViewById(R.id.user_img);
            holder.user_info = (TextView) convertView.findViewById(R.id.user_info);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.user_name.setText(likeBean.getName());
        holder.user_info.setText(likeBean.getAge()+" - "+likeBean.getLocation());
        if (holder.user_img != null) {
            new ImageDownloaderTask(holder.user_img).execute(img_url+"/"+likeBean.getImage());
        }

        return convertView;
    }

    private LayoutInflater getLayoutInflater() {
        return inflater;
    }

    class Holder {

        ImageView user_img;
        TextView user_name, user_info;
    }

}
