package afroradix.xigmapro.com.directhiringcom;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import custom_components.RoundedImageViewWhiteBorder;
import utilities.async_tasks.ImageDownloaderTask;
import utilities.data_objects.DirectHiringModel;

public class MYProfile extends AppCompatActivity {
    private RoundedImageViewWhiteBorder img_my_prfl;
    private ImageView imgchange;
    private TextView name_user,location,prfldetails_fields;
    private ListView list_criteria;
    private Button details_edit,edit_location,edit_about;
    private String img_url="http://xigmapro.website/dev4/directhiring/public/resource/site/images/users/";
    DirectHiringModel dataModel=DirectHiringModel.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        img_my_prfl=(RoundedImageViewWhiteBorder)findViewById(R.id.img_my_prfl);
        imgchange=(ImageView)findViewById(R.id.imgchange);
        name_user=(TextView)findViewById(R.id.name_user);
        location=(TextView)findViewById(R.id.location);
        prfldetails_fields=(TextView)findViewById(R.id.prfldetails_fields);
        list_criteria=(ListView)findViewById(R.id.list_criteria);
        details_edit=(Button)findViewById(R.id.details_edit);
        edit_location=(Button)findViewById(R.id.edit_location);
        edit_about=(Button)findViewById(R.id.edit_about);
        Log.e("Img>>", img_url + "/" + dataModel.userBean.getImage());
        if (!dataModel.userBean.getImage().equals("")) {
            if (img_my_prfl != null) {
                new ImageDownloaderTask(img_my_prfl).execute(img_url + "/" + dataModel.userBean.getImage());
            }
        }
        name_user.setText(dataModel.userBean.getName());
        location.setText(dataModel.userBean.getLocation());
    }

}
