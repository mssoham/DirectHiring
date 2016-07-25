package afroradix.xigmapro.com.directhiringcom.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import adapters.AvailabilityAdapter;
import adapters.DutyAdapter;
import adapters.ExpRangeAdapter;
import adapters.NationalityAdapter;
import afroradix.xigmapro.com.directhiringcom.MYProfile;
import afroradix.xigmapro.com.directhiringcom.R;
import afroradix.xigmapro.com.directhiringcom.UploadImage;
import custom_components.RangeSeekBar;
import shared_pref.SharedStorage;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.DirectHiringModel;
import utilities.data_objects.UserBean;
import utilities.data_objects.UserCriteriaBean;
import utilities.others.CToast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CriteriaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CriteriaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CriteriaFragment extends android.support.v4.app.DialogFragment implements PremiumMemberDialogFragment.OnFragmentInteractionListener, View.OnClickListener, AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CheckBox nationality_check_edit,main_duty_check_edit,availibility_check_edit,exp_range_check_edit,sal_range_check_edit,
            age_range_check_edit,day_of_range_check_edit;
    private Spinner nationality_edit,main_duty_edit,availibility_edit,exp_range_edit,criteria;
    DirectHiringModel dataModel = DirectHiringModel.getInstance();
    NationalityAdapter nationality_adapter;
    AvailabilityAdapter availability_adapter;
    DutyAdapter duty_adapter;
    ExpRangeAdapter exprange_adapter;
    ProgressDialog progressDialog;
    String sal_range_seekValuemin,sal_range_seekValuemax,age_seekValuemin,age_seekValuemax,day_of_range_seekValuemin,
            day_of_range_seekValuemax,nationality1,main_duty1,availibility1,exp_range1,isPaid,user_id,criteriaRange;
    RangeSeekBar sal_range_seekbar_edit,age_range_seekbar_edit,day_of_range_seekbar_edit;
    int check = 0;
    UserBean user=new UserBean();
    int first,second;
    Button continue_btn_last_edit;
    public static String user_status="";
    JSONObject criteriaObj=new JSONObject();

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CriteriaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CriteriaFragment newInstance(String param1, String param2) {
        CriteriaFragment fragment = new CriteriaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CriteriaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_criteria, container, false);
        getDialog().setTitle("Edit Criteria");
        nationality_check_edit=(CheckBox)view.findViewById(R.id.nationality_check_edit);
        main_duty_check_edit=(CheckBox)view.findViewById(R.id.main_duty_check_edit);
        availibility_check_edit=(CheckBox)view.findViewById(R.id.availibility_check_edit);
        exp_range_check_edit=(CheckBox)view.findViewById(R.id.exp_range_check_edit);
        sal_range_check_edit=(CheckBox)view.findViewById(R.id.sal_range_check_edit);
        age_range_check_edit=(CheckBox)view.findViewById(R.id.age_range_check_edit);
        day_of_range_check_edit=(CheckBox)view.findViewById(R.id.day_of_range_check_edit);
        nationality_edit=(Spinner)view.findViewById(R.id.nationality_edit);
        main_duty_edit=(Spinner)view.findViewById(R.id.main_duty_edit);
        day_of_range_seekbar_edit=(RangeSeekBar)view.findViewById(R.id.day_of_range_seekbar_edit);
        age_range_seekbar_edit=(RangeSeekBar)view.findViewById(R.id.age_range_seekbar_edit);
        sal_range_seekbar_edit=(RangeSeekBar)view.findViewById(R.id.sal_range_seekbar_edit);
        availibility_edit=(Spinner)view.findViewById(R.id.availibility_edit);
        exp_range_edit=(Spinner)view.findViewById(R.id.exp_range_edit);
        user_status=dataModel.userBean.getStatus();
        Log.e("status", user_status);
        nationality_adapter = new NationalityAdapter(getActivity(), DirectHiringModel.getInstance().nationalityBeanArrayList);
        availability_adapter = new AvailabilityAdapter(getActivity(), DirectHiringModel.getInstance().availabilityBeanArrayList);
        duty_adapter= new DutyAdapter(getActivity(), DirectHiringModel.getInstance().dutyBeanArrayList);
        exprange_adapter=new ExpRangeAdapter(getActivity(), DirectHiringModel.getInstance().experienceBeanArrayList);
        user_id = SharedStorage.getValue(getActivity(), "UserId");
        nationality_edit.setAdapter(nationality_adapter);
        main_duty_edit.setAdapter(duty_adapter);
        availibility_edit.setAdapter(availability_adapter);
        exp_range_edit.setAdapter(exprange_adapter);
        criteriaselection("Nationality", nationality_check_edit, null, nationality_edit);
        criteriaselection("Day off Range", day_of_range_check_edit,day_of_range_seekbar_edit,null);
        criteriaselection("Salary Range", sal_range_check_edit,sal_range_seekbar_edit,null);
        criteriaselection("Age Range", age_range_check_edit,age_range_seekbar_edit,null);
        criteriaselection("Main Duty", main_duty_check_edit,null,main_duty_edit);
        criteriaselection("Job Avaliability",availibility_check_edit,null,availibility_edit);
        criteriaselection("Experience Range",exp_range_check_edit,null,exp_range_edit);
        nationality_check_edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    nationality_edit.setVisibility(View.VISIBLE);
                    check++;
                } else {
                    nationality_edit.setVisibility(View.GONE);
                    criteriaObj.remove("nationality");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                }
                if (user_status.equals("normal") && check > 2) {
                    CToast.show(getActivity().getApplicationContext(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    nationality_check_edit.setChecked(false);
                    nationality_edit.setVisibility(View.GONE);
                    criteriaObj.remove("nationality");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                }
            }
        });
        main_duty_check_edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    main_duty_edit.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    main_duty_edit.setVisibility(View.GONE);
                    criteriaObj.remove("duty");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (user_status.equals("normal")&&check > 2) {
                    CToast.show(getActivity(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    main_duty_check_edit.setChecked(false);
                    main_duty_edit.setVisibility(View.GONE);
                    criteriaObj.remove("duty");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                }
            }
        });
        availibility_check_edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    availibility_edit.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    availibility_edit.setVisibility(View.GONE);
                    criteriaObj.remove("avaliability");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (user_status.equals("normal")&&check > 2) {
                    CToast.show(getActivity(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    availibility_check_edit.setChecked(false);
                    availibility_edit.setVisibility(View.GONE);
                    criteriaObj.remove("avaliability");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                }
            }
        });
        exp_range_check_edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    exp_range_edit.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    exp_range_edit.setVisibility(View.GONE);
                    criteriaObj.remove("experience_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (user_status.equals("normal")&&check > 2) {

                    CToast.show(getActivity(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    exp_range_check_edit.setChecked(false);
                    exp_range_edit.setVisibility(View.GONE);
                    criteriaObj.remove("experience_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                }
            }
        });
        sal_range_check_edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sal_range_seekbar_edit.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    /*rel_sal_range.setVisibility(View.GONE);*/
                    sal_range_seekbar_edit.setVisibility(View.GONE);
                    criteriaObj.remove("salary_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (user_status.equals("normal")&&check > 2) {

                    CToast.show(getActivity(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    sal_range_check_edit.setChecked(false);
                    /*rel_sal_range.setVisibility(View.GONE);*/
                    sal_range_seekbar_edit.setVisibility(View.GONE);
                    criteriaObj.remove("salary_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                }
            }
        });
        age_range_check_edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    age_range_seekbar_edit.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    /*rel_age.setVisibility(View.GONE);*/
                    age_range_seekbar_edit.setVisibility(View.GONE);
                    criteriaObj.remove("age_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (user_status.equals("normal")&&check > 2) {
                    CToast.show(getActivity(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    age_range_check_edit.setChecked(false);
                    /*rel_age.setVisibility(View.GONE);*/
                    age_range_seekbar_edit.setVisibility(View.GONE);
                    criteriaObj.remove("age_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                }
            }
        });
        day_of_range_check_edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    day_of_range_seekbar_edit.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    /*rel_day_of_range.setVisibility(View.GONE);*/
                    day_of_range_seekbar_edit.setVisibility(View.GONE);
                    criteriaObj.remove("day_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (user_status.equals("normal")&& check > 2) {
                    CToast.show(getActivity(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    day_of_range_check_edit.setChecked(false);
                    /*rel_day_of_range.setVisibility(View.GONE);*/
                    day_of_range_seekbar_edit.setVisibility(View.GONE);
                    criteriaObj.remove("day_range");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                    // Show DialogFragment
                    dFragment.show(fm, "Dialog Fragment");
                }
            }
        });
        sal_range_seekbar_edit.setRangeValues(500, 1000);
        sal_range_seekbar_edit.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Log.e("value", minValue + "  " + maxValue);
                /*sal_range_seekValuemin.setText("Min "+minValue);
                sal_range_seekValuemax.setText("Max"+maxValue);*/
                sal_range_seekValuemin=minValue.toString().trim();
                sal_range_seekValuemax=maxValue.toString().trim();
                String salary=sal_range_seekValuemin+","+sal_range_seekValuemax;
                try {
                    criteriaObj.put("salary_range",salary);
                    Log.e("add--->", String.valueOf(criteriaObj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        age_range_seekbar_edit.setRangeValues(23, 50);
        age_range_seekbar_edit.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Log.e("value", minValue + "  " + maxValue);
                /*age_seekValuemin.setText("Min"+minValue);
                age_seekValuemax.setText("Max"+maxValue);*/
                age_seekValuemin=minValue.toString().trim();
                age_seekValuemax=maxValue.toString().trim();
                String age=age_seekValuemin+","+age_seekValuemax;
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
        day_of_range_seekbar_edit.setRangeValues(0, 4);
        day_of_range_seekbar_edit.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Log.e("value", minValue + "  " + maxValue);
                /*day_of_range_seekValuemin.setText("Min"+minValue);
                day_of_range_seekValuemax.setText("Max"+maxValue);*/
                day_of_range_seekValuemin=minValue.toString().trim();
                day_of_range_seekValuemax=maxValue.toString().trim();
                String day_of=day_of_range_seekValuemin+","+day_of_range_seekValuemax;
                try {
                    criteriaObj.put("day_range",day_of);
                    Log.e("add--->", String.valueOf(criteriaObj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        nationality_edit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nationality1 = dataModel.getInstance().nationalityBeanArrayList.get(position).getKey();
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
        main_duty_edit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                main_duty1 = dataModel.getInstance().dutyBeanArrayList.get(position).getKey();
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
        availibility_edit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                availibility1 = dataModel.getInstance().availabilityBeanArrayList.get(position).getKey();
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
        exp_range_edit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exp_range1 = dataModel.getInstance().experienceBeanArrayList.get(position).getKey();
                try {
                    criteriaObj.put("experience_range",exp_range1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        continue_btn_last_edit=(Button)view.findViewById(R.id.continue_btn_last_edit);
        continue_btn_last_edit.setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        if(v==continue_btn_last_edit){
            criteria();
        }

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

    @Override
    public void processFinish(String type, String output) {
        Log.e("output-->",output);
        if (type.equals(RemoteAsync.CRITERIA)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    //startActivity(new Intent(ServiceDetailsActivity.this,OrderSuccessfulActivity.class));
                    JSONArray criteriaArr = obj.getJSONArray("criterias");
                    UserBean userBean=new UserBean();
                    ArrayList<UserCriteriaBean> userCriteriaBeans=new ArrayList<UserCriteriaBean>();
                    if (criteriaArr.length()>0){
                        for (int i = 0; i<criteriaArr.length();i++){
                            UserCriteriaBean userCriteriaBean=new UserCriteriaBean();
                            JSONObject userC = criteriaArr.getJSONObject(i);

                            userCriteriaBean.setKey(userC.getString("criteria"));
                            userCriteriaBean.setValue(userC.getString("criteria_details"));

                            userCriteriaBeans.add(userCriteriaBean);
                        }
                    }

                    dataModel.userBean.setUserCriteriaBeanArrayList(userCriteriaBeans);
                    Intent intent=new Intent(getActivity(),MYProfile.class);
                    startActivity(intent);
                }else{
                    CToast.show(getActivity(),"Failed to select criteria!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    private void criteriaselection(String key,CheckBox checkBox,RangeSeekBar rangeSeekBar,Spinner spinner){
        Log.e("CheckBox---->", String.valueOf(checkBox));
        Log.e("rangeSeekBar---->", String.valueOf(rangeSeekBar));
        Log.e("spinner---->", String.valueOf(spinner));
        for(int i=0;i<dataModel.userBean.getUserCriteriaBeanArrayList().size();i++){
            if(dataModel.userBean.getUserCriteriaBeanArrayList().get(i).getKey().equals(key)){
                Log.e("key--->", key);
                checkBox.setChecked(true);
                check++;
                if(!(rangeSeekBar ==null)){
                    Log.e("rangeSeekBar---->", String.valueOf(rangeSeekBar));
                    rangeSeekBar.setVisibility(View.VISIBLE);
                    criteriaRange=dataModel.userBean.getUserCriteriaBeanArrayList().get(i).getValue();
                    Log.e("range value--->",criteriaRange);
                    StringTokenizer tokens = new StringTokenizer(criteriaRange, ",");
                    first = Integer.parseInt(tokens.nextToken());
                    second = Integer.parseInt(tokens.nextToken());
                    Log.e("min--->", String.valueOf(first));
                    Log.e("max--->", String.valueOf(second));
                    selectRangeValue(rangeSeekBar, first, second);
                }
                if(!(spinner ==null)){
                    Log.e("spinner---->", String.valueOf(spinner));
                    spinner.setVisibility(View.VISIBLE);
                    criteriaRange=dataModel.userBean.getUserCriteriaBeanArrayList().get(i).getValue();
                    Log.e("spinner value--->",criteriaRange);
                    selectValue(spinner,key,criteriaRange);
                }
                Log.e("Check count", String.valueOf(check));
                Log.e("CheckBox Checked---->", String.valueOf(checkBox));
            }
        }

    }
    private void selectValue(final Spinner spinner,String key, String value) {
        Log.e("spinner selection value", value);
        Log.e("key value",key);
        Log.e("spinner object", String.valueOf(spinner));
        if(key.equals("Main Duty")){
            for (int i = 0; i < dataModel.dutyBeanArrayList.size(); i++) {
                if (dataModel.dutyBeanArrayList.get(i).getKey().equals(value)) {
                    Toast.makeText(getActivity(), "selected position" + i, Toast.LENGTH_LONG).show();
                    final int finalI = i;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            spinner.setSelection(finalI);
                        }
                    }, 100);

                    break;
                }
            }
        }else if(key.equals("Nationality")) {
            for (int i = 0; i < dataModel.nationalityBeanArrayList.size(); i++) {
                if (dataModel.nationalityBeanArrayList.get(i).getKey().equals(value)) {
                    Toast.makeText(getActivity(), "selected position" + i, Toast.LENGTH_LONG).show();
                    final int finalI = i;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            spinner.setSelection(finalI);
                        }
                    }, 100);
                    break;
                }
            }
        }else if(key.equals("Job Avaliability")){
            for (int i = 0; i < dataModel.availabilityBeanArrayList.size(); i++) {
                if (dataModel.availabilityBeanArrayList.get(i).getKey().equals(value)) {
                    Toast.makeText(getActivity(), "selected position" + i, Toast.LENGTH_LONG).show();
                    final int finalI = i;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            spinner.setSelection(finalI);
                        }
                    }, 100);
                    break;
                }
            }
        } else if(key.equals("Experience Range")){
            for (int i = 0; i < dataModel.experienceBeanArrayList.size(); i++) {
                if (dataModel.experienceBeanArrayList.get(i).getKey().equals(value)) {
                    Toast.makeText(getActivity(), "selected position" + i, Toast.LENGTH_LONG).show();
                    final int finalI = i;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            spinner.setSelection(finalI);
                        }
                    }, 100);
                    break;
                }
            }
        }
    }
    private void selectRangeValue(final RangeSeekBar rangeSeekBar, final Integer min, final Integer max){

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rangeSeekBar.setSelectedMinValue(min);
                rangeSeekBar.setSelectedMaxValue(max);
            }
        }, 100);


    }
}
