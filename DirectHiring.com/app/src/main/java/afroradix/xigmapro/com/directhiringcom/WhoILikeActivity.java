package afroradix.xigmapro.com.directhiringcom;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import adapters.LikelistAdapter;
import utilities.data_objects.LikeBean;

public class WhoILikeActivity extends AppCompatActivity {

    private ListView like_list;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_ilike);

        getSupportActionBar().setTitle("Like");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        like_list = (ListView)findViewById(R.id.like_list);

        ArrayList<LikeBean> likeBeanArrayList = new ArrayList<LikeBean>();

        likeBeanArrayList.add(new LikeBean("Jon Snow","Tina.jpg","40","ICELAND"));
        likeBeanArrayList.add(new LikeBean("Leo Messi","thumb10.jpg","69","ARGENTINA"));

        like_list.setAdapter(new LikelistAdapter(likeBeanArrayList,getApplicationContext()));
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
