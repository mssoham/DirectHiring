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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.AvailabilityAdapter;
import adapters.CountryLoadSpinnerAdapter;
import adapters.DutyAdapter;
import adapters.ExpRangeAdapter;
import adapters.NationalityAdapter;
import afroradix.xigmapro.com.directhiringcom.fragments.PremiumMemberDialogFragment;
import custom_components.RangeSeekBar;
import shared_pref.SharedStorage;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.DirectHiringModel;
import utilities.data_objects.NationalityBean;
import utilities.data_objects.UserBean;
import utilities.others.CToast;

public class CriteriaType extends AppCompatActivity implements View.OnClickListener, AsyncResponse,PremiumMemberDialogFragment.OnFragmentInteractionListener {
   private CheckBox nationality_check,main_duty_check,availibility_check,exp_range_check,sal_range_check,
            age_range_check,day_of_range_check;
    private Spinner nationality,main_duty,availibility,exp_range,criteria;
    DirectHiringModel dataModel = DirectHiringModel.getInstance();
    NationalityAdapter nationality_adapter;
    AvailabilityAdapter availability_adapter;
    DutyAdapter duty_adapter;
    ExpRangeAdapter exprange_adapter;
    ProgressDialog progressDialog;
    String sal_range_seekValuemin,sal_range_seekValuemax,age_seekValuemin,age_seekValuemax,day_of_range_seekValuemin,
            day_of_range_seekValuemax,nationality1,main_duty1,availibility1,exp_range1,isPaid,user_id;
    RangeSeekBar day_of_range_seekbar,age_range_seekbar,sal_range_seekbar;
    int check = 0;
    UserBean user=new UserBean();
    Button continue_btn_last;
    public static String user_status="";
    JSONObject criteriaObj=new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criteria_type);
        nationality_check=(CheckBox)findViewById(R.id.nationality_check);
        main_duty_check=(CheckBox)findViewById(R.id.main_duty_check);
        availibility_check=(CheckBox)findViewById(R.id.availibility_check);
        exp_range_check=(CheckBox)findViewById(R.id.exp_range_check);
        sal_range_check=(CheckBox)findViewById(R.id.sal_range_check);
        age_range_check=(CheckBox)findViewById(R.id.age_range_check);
        day_of_range_check=(CheckBox)findViewById(R.id.day_of_range_check);
        nationality=(Spinner)findViewById(R.id.nationality);
        main_duty=(Spinner)findViewById(R.id.main_duty);
        day_of_range_seekbar=(RangeSeekBar)findViewById(R.id.day_of_range_seekbar);
        age_range_seekbar=(RangeSeekBar)findViewById(R.id.age_range_seekbar);
        sal_range_seekbar=(RangeSeekBar)findViewById(R.id.sal_range_seekbar);
        availibility=(Spinner)findViewById(R.id.availibility);
        exp_range=(Spinner)findViewById(R.id.exp_range);
        Log.e("status",user_status);
        nationality_adapter = new NationalityAdapter(this, DirectHiringModel.getInstance().nationalityBeanArrayList);
        availability_adapter = new AvailabilityAdapter(this, DirectHiringModel.getInstance().availabilityBeanArrayList);
        duty_adapter= new DutyAdapter(this, DirectHiringModel.getInstance().dutyBeanArrayList);
        exprange_adapter=new ExpRangeAdapter(this, DirectHiringModel.getInstance().experienceBeanArrayList);
        user_id = SharedStorage.getValue(getApplicationContext(), "UserId");
        nationality.setAdapter(nationality_adapter);
        main_duty.setAdapter(duty_adapter);
        availibility.setAdapter(availability_adapter);
        exp_range.setAdapter(exprange_adapter);
        nationality_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    nationality.setVisibility(View.VISIBLE);
                    check++;
                } else {
                    nationality.setVisibility(View.GONE);
                    criteriaObj.remove("nationality");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                }
                if (user_status.equals("normal")&&check > 2) {
                        CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                        Log.e("check count---->", String.valueOf(check));
                        nationality_check.setChecked(false);
                        nationality.setVisibility(View.GONE);
                        criteriaObj.remove("nationality");
                        Log.e("remove--->", String.valueOf(criteriaObj));
                        check--;
                    FragmentManager fm = getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                    }
                }
        });
        main_duty_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    main_duty.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    main_duty.setVisibility(View.GONE);
                    criteriaObj.remove("duty");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (user_status.equals("normal")&&check > 2) {
                        CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                        Log.e("check count---->", String.valueOf(check));
                        main_duty_check.setChecked(false);
                        main_duty.setVisibility(View.GONE);
                        criteriaObj.remove("duty");
                        Log.e("remove--->", String.valueOf(criteriaObj));
                        check--;
                    FragmentManager fm = getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                    }
                }
        });
        availibility_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    availibility.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    availibility.setVisibility(View.GONE);
                    criteriaObj.remove("avaliability");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (user_status.equals("normal")&&check > 2) {
                        CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                        Log.e("check count---->", String.valueOf(check));
                        availibility_check.setChecked(false);
                        availibility.setVisibility(View.GONE);
                        criteriaObj.remove("avaliability");
                        Log.e("remove--->", String.valueOf(criteriaObj));
                        check--;
                    FragmentManager fm = getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                    }
                }
        });
        exp_range_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    exp_range.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    exp_range.setVisibility(View.GONE);
                    criteriaObj.remove("experience_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (user_status.equals("normal")&&check > 2) {

                        CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                        Log.e("check count---->", String.valueOf(check));
                        exp_range_check.setChecked(false);
                        exp_range.setVisibility(View.GONE);
                        criteriaObj.remove("experience_range");
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
                if (user_status.equals("normal")&&check > 2) {

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
        age_range_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    age_range_seekbar.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    /*rel_age.setVisibility(View.GONE);*/
                    age_range_seekbar.setVisibility(View.GONE);
                    criteriaObj.remove("age_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (user_status.equals("normal")&&check > 2) {
                        CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                        Log.e("check count---->", String.valueOf(check));
                        age_range_check.setChecked(false);
                    /*rel_age.setVisibility(View.GONE);*/
                        age_range_seekbar.setVisibility(View.GONE);
                        criteriaObj.remove("age_range");
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
                if (user_status.equals("normal")&& check > 2) {
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
        age_range_seekbar.setRangeValues(23, 50);
        age_range_seekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Log.e("value", minValue + "  " + maxValue);
                /*age_seekValuemin.setText("Min"+minValue);
                age_seekValuemax.setText("Max"+maxValue);*/
                age_seekValuemin=minValue.toString().trim();
                age_seekValuemax=maxValue.toString().trim();
                String age=age_seekValuemin+":"+age_seekValuemax;
                try {
                    criteriaObj.put("age_range",age);
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
                day_of_range_seekValuemin=minValue.toString().trim();
                day_of_range_seekValuemax=maxValue.toString().trim();
                String day_of=day_of_range_seekValuemin+":"+day_of_range_seekValuemax;
                try {
                    criteriaObj.put("day_range",day_of);
                    Log.e("add--->", String.valueOf(criteriaObj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        nationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nationality1 = dataModel.getInstance().nationalityBeanArrayList.get(position).getNationality_key();
                try {
                    criteriaObj.put("nationality",nationality1);
                    Log.e("add--->", String.valueOf(criteriaObj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        main_duty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                main_duty1 = dataModel.getInstance().dutyBeanArrayList.get(position).getDuty_key();
                try {
                    criteriaObj.put("duty",main_duty1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("add--->", String.valueOf(criteriaObj));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        availibility.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                availibility1 = dataModel.getInstance().availabilityBeanArrayList.get(position).getAvailabilty_key();
                try {
                    criteriaObj.put("avaliability",availibility1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        exp_range.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exp_range1 = dataModel.getInstance().experienceBeanArrayList.get(position).getExp_key();
                try {
                    criteriaObj.put("experience_range",exp_range);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        continue_btn_last=(Button)findViewById(R.id.continue_btn_last);
        continue_btn_last.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /*try {
            JSONObject json = new JSONObject("{\"salary_range\":\"0\",\"msg\":\"HTTP_ACCEPTED\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        } */
        criteria();
    }
    private void criteria(){
        if (user_status.equals("normal")){
            isPaid="yes";
        }else{
            isPaid="no";
        }
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new org.apache.http.message.BasicNameValuePair("criteria", criteriaObj.toString()));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("type", "family"));
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
                    startActivity(new Intent(CriteriaType.this, UploadImage.class));
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
