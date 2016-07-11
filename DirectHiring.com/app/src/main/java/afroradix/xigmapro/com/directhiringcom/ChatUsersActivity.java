package afroradix.xigmapro.com.directhiringcom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.ChatUsersAdapter;
import shared_pref.SharedStorage;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.ChatUsersBean;
import utilities.data_objects.DirectHiringModel;

public class ChatUsersActivity extends AppCompatActivity implements AsyncResponse {

    private ListView chat_list;

    DirectHiringModel dataModel =DirectHiringModel.getInstance();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_users);

        getSupportActionBar().setTitle("Chat Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chat_list = (ListView)findViewById(R.id.chat_list);

        chat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatUsersBean chatUsersBean = dataModel.chatUsersBeanArrayList.get(position);
                ChatListActivity.chat_user_id = chatUsersBean.getMember_id();
                ChatListActivity.chat_member_name = chatUsersBean.getName();
                ChatListActivity.chat_initiation_id = chatUsersBean.getChat_initiation_id();

                startActivity(new Intent(ChatUsersActivity.this, ChatListActivity.class));
            }
        });
       /* chat_list.setOnItemClickListener(new AdapterView.setOnItemClickListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        getChatUsers();
    }

    private void getChatUsers(){
        start_progress_dialog();

        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        String user_id= SharedStorage.getValue(getApplicationContext(), "UserId");

        arrayList.add(new org.apache.http.message.BasicNameValuePair("user_id", user_id));
        RemoteAsync remoteAsync = new RemoteAsync(Urls.get_Chat_Users);
        remoteAsync.type = RemoteAsync.GET_CHAT_USERS;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }

    @Override
    public void processFinish(String type, String output) {
        stop_progress_dialog();
        if (type.equals(RemoteAsync.GET_CHAT_USERS)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    JSONArray chatUsersArr = obj.getJSONArray("chat_users");

                    ArrayList<ChatUsersBean> chatUsersBeanArrayList=new ArrayList<ChatUsersBean>();
                    if (chatUsersArr.length()>0){
                        for (int i=0 ; i<chatUsersArr.length();i++){
                            JSONObject chat= chatUsersArr.getJSONObject(i);
                            ChatUsersBean chatUsersBean=new ChatUsersBean();

                            chatUsersBean.setMember_id(chat.getString("member_id"));
                            chatUsersBean.setChat_initiation_id(chat.getString("chat_initiation_id"));
                            chatUsersBean.setName(chat.getString("name"));

                            chatUsersBeanArrayList.add(chatUsersBean);
                        }
                    }

                    dataModel.chatUsersBeanArrayList=chatUsersBeanArrayList;

                    chat_list.setAdapter(new ChatUsersAdapter(dataModel.chatUsersBeanArrayList,getApplicationContext()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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


}
