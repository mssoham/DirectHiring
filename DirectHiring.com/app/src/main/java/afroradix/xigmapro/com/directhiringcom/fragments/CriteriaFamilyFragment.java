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
import adapters.EmployerTypeAdapter;
import adapters.HouseAdapter;
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
 * {@link CriteriaFamilyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CriteriaFamilyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CriteriaFamilyFragment extends android.support.v4.app.DialogFragment implements PremiumMemberDialogFragment.OnFragmentInteractionListener, View.OnClickListener, AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CheckBox employer_type_check,job_availability_check,house_type_check,sal_range_check,
            family_member_check,day_of_range_check;
    private Spinner employer_type,job_availability,house_type;
    private RangeSeekBar day_of_range_seekbar,family_member_seekbar,sal_range_seekbar;
    HouseAdapter house_Adapter;
    EmployerTypeAdapter employerTyp_eAdapter;
    DirectHiringModel dataModel = DirectHiringModel.getInstance();
    AvailabilityAdapter availability_adapter;
    int check = 0;
    Button continue_btn_last_family;
    UserBean user=new UserBean();
    public static String user_status1="";
    String sal_range_seekValuemin,sal_range_seekValuemax,family_seekValuemin,family_seekValuemax,day_of_range_seekValuemin,
            day_of_range_seekValuemax,employer_type1,job_availability1,house_type1,isPaid,user_id,criteriaRange;
    int first,second;
    JSONObject criteriaObj=new JSONObject();
    ProgressDialog progressDialog;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CriteriaFamilyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CriteriaFamilyFragment newInstance(String param1, String param2) {
        CriteriaFamilyFragment fragment = new CriteriaFamilyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CriteriaFamilyFragment() {
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
        View view= inflater.inflate(R.layout.fragment_criteria_family, container, false);
        getDialog().setTitle("Edit Criteria");
        employer_type_check=(CheckBox)view.findViewById(R.id.employer_type_check);
        job_availability_check=(CheckBox)view.findViewById(R.id.job_availability_check);
        house_type_check=(CheckBox)view.findViewById(R.id.house_type_check);
        sal_range_check=(CheckBox)view.findViewById(R.id.sal_range_check);
        family_member_check=(CheckBox)view.findViewById(R.id.family_member_check);
        day_of_range_check=(CheckBox)view.findViewById(R.id.day_of_range_check);
        employer_type=(Spinner)view.findViewById(R.id.employer_type);
        job_availability=(Spinner)view.findViewById(R.id.job_availability);
        house_type=(Spinner)view.findViewById(R.id.house_type);
        user_id = SharedStorage.getValue(getActivity(), "UserId");
        user_status1=dataModel.userBean.getStatus();
        Log.e("Status", user_status1);
        day_of_range_seekbar=(RangeSeekBar)view.findViewById(R.id.day_of_range_seekbar);
        family_member_seekbar=(RangeSeekBar)view.findViewById(R.id.family_member_seekbar);
        sal_range_seekbar=(RangeSeekBar)view.findViewById(R.id.sal_range_seekbar);
        criteriaselection("Job Avaliability", job_availability_check,null,job_availability);
        criteriaselection("Day off Range", day_of_range_check,day_of_range_seekbar,null);
        criteriaselection("Salary Range", sal_range_check,sal_range_seekbar,null);
        criteriaselection("Employer Type", employer_type_check,null,employer_type);
        criteriaselection("House Type", house_type_check,null,house_type);
        criteriaselection("Family Member",family_member_check,family_member_seekbar,null);
        house_Adapter = new HouseAdapter(getActivity(), DirectHiringModel.getInstance().houseBeanArrayList);
        availability_adapter = new AvailabilityAdapter(getActivity(), DirectHiringModel.getInstance().availabilityBeanArrayList);
        employerTyp_eAdapter= new EmployerTypeAdapter(getActivity(), DirectHiringModel.getInstance().employeeBeanArrayList);
        house_type.setAdapter(house_Adapter);
        employer_type.setAdapter(employerTyp_eAdapter);
        job_availability.setAdapter(availability_adapter);
        employer_type_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    employer_type.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    employer_type.setVisibility(View.GONE);
                    criteriaObj.remove("employee");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                }
                    if (user_status1.equals("normal")&&check > 2) {
                        CToast.show(getActivity(), "you need to pay to check more criteria!");
                        Log.e("check count---->", String.valueOf(check));
                        employer_type_check.setChecked(false);
                        employer_type.setVisibility(View.GONE);
                        criteriaObj.remove("employee");
                        Log.e("remove--->", String.valueOf(criteriaObj));
                        check--;
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        PremiumMemberDialogFragment dFragment = new PremiumMemberDialogFragment();
                        // Show DialogFragment
                        dFragment.show(fm, "Dialog Fragment");
                }
            }
        });
        job_availability_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    job_availability.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    job_availability.setVisibility(View.GONE);
                    criteriaObj.remove("avaliability");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                }

                    if (user_status1.equals("normal")&&check > 2) {
                        CToast.show(getActivity(), "you need to pay to check more criteria!");
                        Log.e("check count---->", String.valueOf(check));
                        job_availability_check.setChecked(false);
                        job_availability.setVisibility(View.GONE);
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
        house_type_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    house_type.setVisibility(View.VISIBLE);
                    check++;
                    Log.e("check count---->", String.valueOf(check));
                } else {
                    house_type.setVisibility(View.GONE);
                    criteriaObj.remove("house");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                }
                if (user_status1.equals("normal")&&check > 2) {
                    CToast.show(getActivity(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    house_type_check.setChecked(false);
                    house_type.setVisibility(View.GONE);
                    criteriaObj.remove("house");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    FragmentManager fm = getActivity().getSupportFragmentManager();
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
                    CToast.show(getActivity(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    sal_range_check.setChecked(false);
                    /*rel_sal_range.setVisibility(View.GONE);*/
                    sal_range_seekbar.setVisibility(View.GONE);
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
                    CToast.show(getActivity(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    day_of_range_check.setChecked(false);
                    /*rel_day_of_range.setVisibility(View.GONE);*/
                    day_of_range_seekbar.setVisibility(View.GONE);
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
                    CToast.show(getActivity(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    family_member_check.setChecked(false);
                    /*rel_age.setVisibility(View.GONE);*/
                    family_member_seekbar.setVisibility(View.GONE);
                    criteriaObj.remove("family_member");
                    Log.e("remove--->", String.valueOf(criteriaObj));
                    check--;
                    FragmentManager fm = getActivity().getSupportFragmentManager();
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
                String salary=sal_range_seekValuemin+","+sal_range_seekValuemax;
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
                String age=family_seekValuemin+","+family_seekValuemax;
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
                String day_of = day_of_range_seekValuemin + "," + day_of_range_seekValuemax;
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
                employer_type1 = dataModel.getInstance().employeeBeanArrayList.get(position).getKey();
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
                job_availability1 = dataModel.getInstance().availabilityBeanArrayList.get(position).getKey();
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
                house_type1=dataModel.getInstance().houseBeanArrayList.get(position).getKey();
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
        continue_btn_last_family=(Button)view.findViewById(R.id.continue_btn_last_family);
        continue_btn_last_family.setOnClickListener(this);
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
                    first = Integer.parseInt(tokens.nextToken());// this will contain "Fruit"
                    second = Integer.parseInt(tokens.nextToken());
                    Log.e("min--->", String.valueOf(first));
                    Log.e("max--->", String.valueOf(second));
                    selectRangeValue(rangeSeekBar, first, second);
                }
                if(!(spinner ==null)){
                    Log.e("spinner---->", String.valueOf(spinner));
                    spinner.setVisibility(View.VISIBLE);
                    criteriaRange=dataModel.userBean.getUserCriteriaBeanArrayList().get(i).getValue();
                    Log.e("range value--->", criteriaRange);
                    selectValue(spinner,key,criteriaRange);
                }
                Log.e("Check count", String.valueOf(check));
                Log.e("CheckBox Checked---->", String.valueOf(checkBox));
            }
        }

    }
    private void selectValue(final Spinner spinner,String key, String value) {
        Log.e("spinner selection value",value);
        Log.e("spinner object", String.valueOf(spinner));
        Log.e("key value",key);
        if(key.equals("Job Avaliability")){
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
        }else if(key.equals("Employer Type")){
            for (int i = 0; i < dataModel.employeeBeanArrayList.size(); i++) {
                /*Log.e("Spinner Key---->",dataModel.employeeBeanArrayList.get(i).getKey());
                Log.e("value---->",dataModel.employeeBeanArrayList.get(i).getValue());*/
                if (dataModel.employeeBeanArrayList.get(i).getKey().equals(value)) {
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
        }else if(key.equals("House Type")){
            for (int i = 0; i < dataModel.houseBeanArrayList.size(); i++) {
                if (dataModel.houseBeanArrayList.get(i).getKey().equals(value)) {
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
