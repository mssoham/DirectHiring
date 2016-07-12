package afroradix.xigmapro.com.directhiringcom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.AvailabilityAdapter;
import adapters.DutyAdapter;
import adapters.EmployerTypeAdapter;
import adapters.ExpRangeAdapter;
import adapters.HouseAdapter;
import adapters.NationalityAdapter;
import afroradix.xigmapro.com.directhiringcom.fragments.PremiumMemberDialogFragment;
import custom_components.RangeSeekBar;
import shared_pref.SharedStorage;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.DirectHiringModel;
import utilities.data_objects.UserBean;
import utilities.others.CToast;

public class CriteriaFamilyType extends AppCompatActivity implements View.OnClickListener, AsyncResponse,PremiumMemberDialogFragment.OnFragmentInteractionListener {
    private CheckBox employer_type_check,job_availability_check,house_type_check,sal_range_check,
            family_member_check,day_of_range_check;
    private Spinner employer_type,job_availability,house_type;
    RangeSeekBar day_of_range_seekbar,family_member_seekbar,sal_range_seekbar;
    HouseAdapter house_Adapter;
    EmployerTypeAdapter employerTyp_eAdapter;
    DirectHiringModel dataModel = DirectHiringModel.getInstance();
    AvailabilityAdapter availability_adapter;
    int check = 0;
    Button continue_btn_last_family;
    UserBean user=new UserBean();
    public static String user_status1="";
    String sal_range_seekValuemin,sal_range_seekValuemax,family_seekValuemin,family_seekValuemax,day_of_range_seekValuemin,
            day_of_range_seekValuemax,employer_type1,job_availability1,house_type1,isPaid,user_id;
    JSONObject criteriaObj=new JSONObject();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criteria_family_type);
        employer_type_check=(CheckBox)findViewById(R.id.employer_type_check);
        job_availability_check=(CheckBox)findViewById(R.id.job_availability_check);
        house_type_check=(CheckBox)findViewById(R.id.house_type_check);
        sal_range_check=(CheckBox)findViewById(R.id.sal_range_check);
        family_member_check=(CheckBox)findViewById(R.id.family_member_check);
        day_of_range_check=(CheckBox)findViewById(R.id.day_of_range_check);
        employer_type=(Spinner)findViewById(R.id.employer_type);
        job_availability=(Spinner)findViewById(R.id.job_availability);
        house_type=(Spinner)findViewById(R.id.house_type);
        user_id = SharedStorage.getValue(getApplicationContext(), "UserId");
        day_of_range_seekbar=(RangeSeekBar)findViewById(R.id.day_of_range_seekbar);
        family_member_seekbar=(RangeSeekBar)findViewById(R.id.family_member_seekbar);
        sal_range_seekbar=(RangeSeekBar)findViewById(R.id.sal_range_seekbar);
        house_Adapter = new HouseAdapter(this, DirectHiringModel.getInstance().houseBeanArrayList);
        availability_adapter = new AvailabilityAdapter(this, DirectHiringModel.getInstance().availabilityBeanArrayList);
        employerTyp_eAdapter= new EmployerTypeAdapter(this, DirectHiringModel.getInstance().employeeBeanArrayList);
        house_type.setAdapter(house_Adapter);
        employer_type.setAdapter(employerTyp_eAdapter);
        job_availability.setAdapter(availability_adapter);
        employer_type_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    employer_type.setVisibility(View.VISIBLE);
                    check++;
                } else {
                    employer_type.setVisibility(View.GONE);
                    criteriaObj.remove("employee");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                }
                if (user.getStatus().equals("normal")) {
                    if (user_status1.equals("normal")&&check > 2) {
                        CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                        Log.e("check count---->", String.valueOf(check));
                        employer_type_check.setChecked(false);
                        employer_type.setVisibility(View.GONE);
                        criteriaObj.remove("employee");
                        Log.e("remove--->", String.valueOf(criteriaObj));
                        check--;
                        FragmentManager fm = getSupportFragmentManager();
                        PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                        // Show DialogFragment
                        dFragment.show(fm, "Dialog Fragment");
                    }
                }
            }
        });
        job_availability_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    job_availability.setVisibility(View.VISIBLE);
                    check++;
                } else {
                    job_availability.setVisibility(View.GONE);
                    criteriaObj.remove("avaliability");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                }
                if (user.getStatus().equals("normal")) {
                    if (user_status1.equals("normal")&&check > 2) {
                        CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                        Log.e("check count---->", String.valueOf(check));
                        job_availability_check.setChecked(false);
                        job_availability.setVisibility(View.GONE);
                        criteriaObj.remove("avaliability");
                        Log.e("remove--->", String.valueOf(criteriaObj));
                        check--;
                        FragmentManager fm = getSupportFragmentManager();
                        PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                        // Show DialogFragment
                        dFragment.show(fm, "Dialog Fragment");
                    }
                }
            }
        });
        house_type_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    house_type.setVisibility(View.VISIBLE);
                    check++;
                } else {
                    house_type.setVisibility(View.GONE);
                    criteriaObj.remove("house");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                }
                    if (user_status1.equals("normal")&&check > 2) {
                        CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                        Log.e("check count---->", String.valueOf(check));
                        house_type_check.setChecked(false);
                        house_type.setVisibility(View.GONE);
                        criteriaObj.remove("house");
                        Log.e("remove--->", String.valueOf(criteriaObj));
                        check--;
                        FragmentManager fm = getSupportFragmentManager();
                        PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                        // Show DialogFragment
                        dFragment.show(fm, "Dialog Fragment");
                    }
                }
        });
        sal_range_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sal_range_seekbar.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    /*rel_sal_range.setVisibility(View.GONE);*/
                    sal_range_seekbar.setVisibility(View.GONE);
                    criteriaObj.remove("salary_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (user_status1.equals("normal")&&check > 2) {
                    CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    sal_range_check.setChecked(false);
                    /*rel_sal_range.setVisibility(View.GONE);*/
                    sal_range_seekbar.setVisibility(View.GONE);
                    criteriaObj.remove("salary_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    FragmentManager fm = getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                }
            }
        });
        day_of_range_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    day_of_range_seekbar.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    /*rel_day_of_range.setVisibility(View.GONE);*/
                    day_of_range_seekbar.setVisibility(View.GONE);
                    criteriaObj.remove("day_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (user_status1.equals("normal")&&check > 2) {
                    CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    day_of_range_check.setChecked(false);
                    /*rel_day_of_range.setVisibility(View.GONE);*/
                    day_of_range_seekbar.setVisibility(View.GONE);
                    criteriaObj.remove("day_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    FragmentManager fm = getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                }
            }
        });
        family_member_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    family_member_seekbar.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    /*rel_age.setVisibility(View.GONE);*/
                    family_member_seekbar.setVisibility(View.GONE);
                    criteriaObj.remove("family_member");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (user_status1.equals("normal")&&check > 2) {
                    CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    family_member_check.setChecked(false);
                    /*rel_age.setVisibility(View.GONE);*/
                    family_member_seekbar.setVisibility(View.GONE);
                    criteriaObj.remove("family_member");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    FragmentManager fm = getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                }
            }
        });
        sal_range_seekbar.setRangeValues(500, 1000);
        sal_range_seekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Log.e("value", minValue + "  " + maxValue);
                /*sal_range_seekValuemin.setText("Min "+minValue);
                sal_range_seekValuemax.setText("Max"+maxValue);*/
                sal_range_seekValuemin=minValue.toString().trim();
                sal_range_seekValuemax=maxValue.toString().trim();
                String salary=sal_range_seekValuemin+":"+sal_range_seekValuemax;
                try {
                    criteriaObj.put("salary_range",salary);
                    Log.e("add--->", String.valueOf(criteriaObj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        family_member_seekbar.setRangeValues(2, 10);
        family_member_seekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Log.e("value", minValue + "  " + maxValue);
                /*age_seekValuemin.setText("Min"+minValue);
                age_seekValuemax.setText("Max"+maxValue);*/
                family_seekValuemin=minValue.toString().trim();
                family_seekValuemax=maxValue.toString().trim();
                String age=family_seekValuemin+":"+family_seekValuemax;
                try {
                    criteriaObj.put("family_member",age);
                    Log.e("add--->", String.valueOf(criteriaObj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        /*day_of_range_seekbar.setNotifyWhileDragging(true);*/
        /*day_of_range_seekbar.setAdapter(new DemoRangeAdapter());*/
        day_of_range_seekbar.setRangeValues(0, 4);
        day_of_range_seekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Log.e("value", minValue + "  " + maxValue);
                /*day_of_range_seekValuemin.setText("Min"+minValue);
                day_of_range_seekValuemax.setText("Max"+maxValue);*/
                day_of_range_seekValuemin = minValue.toString().trim();
                day_of_range_seekValuemax = maxValue.toString().trim();
                String day_of = day_of_range_seekValuemin + ":" + day_of_range_seekValuemax;
                try {
                    criteriaObj.put("day_range", day_of);
                    Log.e("add--->", String.valueOf(criteriaObj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        employer_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                employer_type1 = dataModel.getInstance().employeeBeanArrayList.get(position).getEmployee_value();
                try {
                    criteriaObj.put("employee", employer_type1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        job_availability.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                job_availability1 = dataModel.getInstance().availabilityBeanArrayList.get(position).getAvailabilty_value();
                try {
                    criteriaObj.put("avaliability", job_availability1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        house_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                house_type1=dataModel.getInstance().houseBeanArrayList.get(position).getHouse_value();
                try {
                    criteriaObj.put("house", house_type1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        continue_btn_last_family=(Button)findViewById(R.id.continue_btn_last_family);
        continue_btn_last_family.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==continue_btn_last_family){
            criteriaFamily();
        }
    }
    private void criteriaFamily(){
        if (user_status1.equals("normal")){
            isPaid="yes";
        }else{
            isPaid="no";
        }
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new org.apache.http.message.BasicNameValuePair("criteria", criteriaObj.toString()));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("type", "helper"));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("last_insert_id", user_id));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("is_paid", isPaid));

        //Urls.urlkey=url_key;
        RemoteAsync remoteAsync = new RemoteAsync(Urls.criteria);
        remoteAsync.type = RemoteAsync.CRITERIA;
        remoteAsync.delegate=this;
        remoteAsync.execute(arrayList);
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
        Log.e("output-->",output);
        if (type.equals(RemoteAsync.CRITERIA)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    //startActivity(new Intent(ServiceDetailsActivity.this,OrderSuccessfulActivity.class));
                    startActivity(new Intent(CriteriaFamilyType.this, UploadImage.class));
                }else{
                    CToast.show(getApplicationContext(),"Failed to select criteria!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
