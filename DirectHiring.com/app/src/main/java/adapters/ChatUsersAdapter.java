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
import utilities.data_objects.ChatUsersBean;


/**
 * Created by Self-3 on 7/8/2016.
 */
public class ChatUsersAdapter extends BaseAdapter {
    ArrayList<ChatUsersBean> chatUsersBeanArrayList = new ArrayList<ChatUsersBean>();
    Context context;
    LayoutInflater inflater;

    public ChatUsersAdapter(ArrayList<ChatUsersBean> chatUsersBeanArrayList, Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.chatUsersBeanArrayList = chatUsersBeanArrayList;
    }

    @Override
    public int getCount() {
        return chatUsersBeanArrayList.size();
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
        ChatUsersBean chatUsersBean = chatUsersBeanArrayList.get(position);

        if (convertView == null) {
            inflater = getLayoutInflater();
            holder = new Holder();
            convertView = inflater.inflate(R.layout.inflate_chat_user, parent, false);
            holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            holder.user_img = (ImageView) convertView.findViewById(R.id.user_img);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.user_name.setText(chatUsersBean.getName());

        return convertView;
    }

    private LayoutInflater getLayoutInflater() {
        return inflater;
    }

    class Holder {

        ImageView user_img;
        TextView user_name;
    }
}
