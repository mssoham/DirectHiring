package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import afroradix.xigmapro.com.directhiringcom.R;
import utilities.data_objects.ChatListBean;
import utilities.data_objects.ChatUsersBean;

/**
 * Created by Self-3 on 7/8/2016.
 */
public class ChatListAdapter extends BaseAdapter {
    ArrayList<ChatListBean> chatListBeanArrayList = new ArrayList<ChatListBean>();
    Context context;
    LayoutInflater inflater;

    public ChatListAdapter(ArrayList<ChatListBean> chatListBeanArrayList, Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.chatListBeanArrayList = chatListBeanArrayList;
    }

    @Override
    public int getCount() {
        return chatListBeanArrayList.size();
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
        ChatListBean chatListBean = chatListBeanArrayList.get(position);

        if (convertView == null) {
            inflater = getLayoutInflater();
            holder = new Holder();
            convertView = inflater.inflate(R.layout.inflate_chat_list, parent, false);
            holder.linear1 = (LinearLayout) convertView.findViewById(R.id.linear1);
            holder.linear2 = (LinearLayout) convertView.findViewById(R.id.linear2);
            holder.sender1 = (TextView) convertView.findViewById(R.id.sender1);
            holder.sender2 = (TextView) convertView.findViewById(R.id.sender2);
            holder.msg1 = (TextView) convertView.findViewById(R.id.msg1);
            holder.msg2 = (TextView) convertView.findViewById(R.id.msg2);
            holder.time1 = (TextView) convertView.findViewById(R.id.time1);
            holder.time2 = (TextView) convertView.findViewById(R.id.time2);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (chatListBean.getName().equals("You")){
            holder.linear1.setVisibility(View.GONE);
            holder.linear2.setVisibility(View.VISIBLE);

            holder.sender2.setText("You");
            holder.msg2.setText(chatListBean.getMsg());
            holder.time2.setText(chatListBean.getMsg_time());
        }else{
            holder.linear1.setVisibility(View.VISIBLE);
            holder.linear2.setVisibility(View.GONE);

            holder.sender1.setText(chatListBean.getName());
            holder.msg1.setText(chatListBean.getMsg());
            holder.time1.setText(chatListBean.getMsg_time());
        }


        return convertView;
    }

    private LayoutInflater getLayoutInflater() {
        return inflater;
    }

    class Holder {

        LinearLayout linear1,linear2;
        ImageView user_img;
        TextView sender1, sender2, msg1, msg2, time1, time2;
    }
}
