package afroradix.xigmapro.com.directhiringcom;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.ChatListAdapter;
import adapters.ChatUsersAdapter;
import shared_pref.SharedStorage;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.ChatListBean;
import utilities.data_objects.ChatUsersBean;
import utilities.data_objects.DirectHiringModel;

public class ChatListActivity extends AppCompatActivity implements AsyncResponse, View.OnClickListener {

    private ListView chat_list;
    private EditText msg;
    private LinearLayout btnMsg;

    public static String chat_user_id="";
    public static String chat_member_name="";
    public static String chat_initiation_id="";

    ProgressDialog progressDialog;
    DirectHiringModel dataModel = DirectHiringModel.getInstance();
    ChatListAdapter chatListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        getSupportActionBar().setTitle(chat_member_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chat_list = (ListView)findViewById(R.id.chat_list);
        msg = (EditText)findViewById(R.id.msg);
        btnMsg = (LinearLayout)findViewById(R.id.btnMsg);


        getChatList();

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //CALL ANY METHOD OR ANY URL OR FUNCTION or any view
                                getChatList();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };t.start();

        btnMsg.setOnClickListener(this);
    }

    private void sendMessage(){
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        String user_id= SharedStorage.getValue(getApplicationContext(), "UserId");

        arrayList.add(new org.apache.http.message.BasicNameValuePair("sender_id", user_id));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("receiver_id", chat_user_id));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("msg", msg.getText().toString()));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("chat_initiation_id", chat_initiation_id));
        RemoteAsync remoteAsync = new RemoteAsync(Urls.send_Messages);
        remoteAsync.type = RemoteAsync.SEND_MESSAGES;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }

    private void getChatList(){
        //start_progress_dialog();

        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        String user_id= SharedStorage.getValue(getApplicationContext(), "UserId");

        arrayList.add(new org.apache.http.message.BasicNameValuePair("user_id", user_id));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("chat_initiation_id", chat_initiation_id));
        RemoteAsync remoteAsync = new RemoteAsync(Urls.get_Chat_Messages);
        remoteAsync.type = RemoteAsync.GET_CHAT_MESSAGES;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void start_progress_dialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    void stop_progress_dialog(){
        progressDialog.dismiss();
    }

    @Override
    public void processFinish(String type, String output) {
        //stop_progress_dialog();
        if (type.equals(RemoteAsync.GET_CHAT_MESSAGES)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    JSONArray chatUsersArr = obj.getJSONArray("chat_msgs");

                    ArrayList<ChatListBean> chatListBeanArrayList=new ArrayList<ChatListBean>();
                    if (chatUsersArr.length()>0){
                        for (int i=0 ; i<chatUsersArr.length();i++){
                            JSONObject chat= chatUsersArr.getJSONObject(i);
                            ChatListBean chatListBean=new ChatListBean();

                            chatListBean.setName(chat.getString("name"));
                            chatListBean.setMsg(chat.getString("msg"));
                            chatListBean.setMsg_time(chat.getString("msg_time"));

                            chatListBeanArrayList.add(chatListBean);
                        }
                    }

                    //dataModel.chatListBeanArrayList=chatListBeanArrayList;

                    chatListAdapter = new ChatListAdapter(chatListBeanArrayList, getApplicationContext());
                    chat_list.setAdapter(chatListAdapter);
                    chat_list.setSelection(chatListAdapter.getCount());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else  if (type.equals(RemoteAsync.SEND_MESSAGES)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    msg.setText("");
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    getChatList();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        sendMessage();
    }
}
