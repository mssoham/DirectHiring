package afroradix.xigmapro.com.directhiringcom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import adapters.AvailabilityAdapter;
import adapters.UserCriteriaAdapter;
import afroradix.xigmapro.com.directhiringcom.fragments.AboutFragment;
import afroradix.xigmapro.com.directhiringcom.fragments.CriteriaFamilyFragment;
import afroradix.xigmapro.com.directhiringcom.fragments.CriteriaFragment;
import afroradix.xigmapro.com.directhiringcom.fragments.ImageChangeFragment;
import afroradix.xigmapro.com.directhiringcom.fragments.PremiumMemberDialogFragment;
import custom_components.RoundedImageViewWhiteBorder;
import utilities.async_tasks.ImageDownloaderTask;
import utilities.data_objects.DirectHiringModel;
import utilities.data_objects.UserBean;
import utilities.others.CToast;

public class MYProfile extends AppCompatActivity implements View.OnClickListener,ImageChangeFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener,CriteriaFragment.OnFragmentInteractionListener, CriteriaFamilyFragment.OnFragmentInteractionListener,
        PremiumMemberDialogFragment.OnFragmentInteractionListener{
    private RoundedImageViewWhiteBorder img_my_prfl;
    private ImageView imgchange;
    private TextView name_user,location,prfldetails_fields,type_txt;
    private ListView list_criteria;
    private LinearLayout details_edit,edit_about;
    private String img_url="http://xigmapro.website/dev4/directhiring/public/resource/site/images/users/";
    DirectHiringModel dataModel=DirectHiringModel.getInstance();
    UserCriteriaAdapter userCriteriaAdapter;
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
        details_edit=(LinearLayout)findViewById(R.id.details_edit);
        type_txt=(TextView)findViewById(R.id.type_txt);
        type_txt.setText(dataModel.userBean.getType()+" "+getString(R.string.profile_and_requirement));
        edit_about=(LinearLayout)findViewById(R.id.edit_about);
        Log.e("Img>>", img_url + "/" + dataModel.userBean.getImage());
        if (!dataModel.userBean.getImage().equals("")) {
            if (img_my_prfl != null) {
                new ImageDownloaderTask(img_my_prfl).execute(img_url + "/" + dataModel.userBean.getImage());
            }
        }
        name_user.setText(dataModel.userBean.getName());
        location.setText(dataModel.userBean.getLocation());
        prfldetails_fields.setText(dataModel.userBean.getDescription());
        userCriteriaAdapter = new UserCriteriaAdapter(DirectHiringModel.getInstance().userBean.getUserCriteriaBeanArrayList(),getApplicationContext());
        list_criteria.setAdapter(userCriteriaAdapter);
        list_criteria.setFocusable(false);
        imgchange.setOnClickListener(this);
        details_edit.setOnClickListener(this);
        edit_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==imgchange){
            Intent intent=new Intent(MYProfile.this,ImagechangeaddActivity.class);
            startActivity(intent);
        }
        if(v==edit_about){
            CToast.show(getApplicationContext(), "about clicked");
            FragmentManager fm = getSupportFragmentManager();
            AboutFragment dFragment = new AboutFragment();
            // Show DialogFragment
            dFragment.show(fm, "Dialog Fragment");
        }
        if(v==details_edit){
            CToast.show(getApplicationContext(),"details clicked");
            if(dataModel.userBean.getType().equals("helper")) {
                FragmentManager fm = getSupportFragmentManager();
                CriteriaFamilyFragment dFragment = new CriteriaFamilyFragment();
                // Show DialogFragment
                dFragment.show(fm, "Dialog Fragment");
            }else{
                FragmentManager fm = getSupportFragmentManager();
                CriteriaFragment dFragment = new CriteriaFragment();
                // Show DialogFragment
                dFragment.show(fm, "Dialog Fragment");
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MYProfile.this, DashboardActivity.class));
    }
}
