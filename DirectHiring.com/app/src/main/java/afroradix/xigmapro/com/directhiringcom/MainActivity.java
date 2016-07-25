package afroradix.xigmapro.com.directhiringcom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.AvailabilityBean;
import utilities.data_objects.CountryLoadBean;
import utilities.data_objects.DateBean;
import utilities.data_objects.DutyBean;
import utilities.data_objects.EmployeeBean;
import utilities.data_objects.ExperienceBean;
import utilities.data_objects.HouseBean;
import utilities.data_objects.NationalityBean;
import utilities.data_objects.DirectHiringModel;
import utilities.data_objects.TypeSpinnerBean;
import utilities.others.CToast;
import utilities.others.ConnectionStatus;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends Activity implements AsyncResponse {
    ProgressDialog progressDialog;
    DirectHiringModel dataModel = DirectHiringModel.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!ConnectionStatus.checkConnectionStatus(getApplicationContext())){
            LinearLayout no_Connection=(LinearLayout)findViewById(R.id.no_Connection);
            no_Connection.setVisibility(View.VISIBLE);
            Animation bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
            no_Connection.startAnimation(bottomUp);
            no_Connection.setVisibility(View.VISIBLE);
        }else {
            getSpinnerElement();
        }

        //TypeSpinnerBean type=new TypeSpinnerBean();
        ArrayList<TypeSpinnerBean> typeSpinnerBeanArrayList=new ArrayList<TypeSpinnerBean>();
        dataModel.typeSpinnerBeanArrayList.add(new TypeSpinnerBean("Helper","Helper"));
        dataModel.typeSpinnerBeanArrayList.add(new TypeSpinnerBean("Family","Family"));
        for(int i=1;i<=31;i++){
            dataModel.dateArrayList.add(new DateBean((i)));
        }
        for(int j=1;j<=12;j++){
            dataModel.monthArrayList.add(new DateBean(j));
        }
        for (int k=1940;k<=2000;k++){
            dataModel.yearArrayList.add(new DateBean(k));
        }

    }
    private void getSpinnerElement(){
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        //Urls.urlkey=url_key;
        RemoteAsync remoteAsync = new RemoteAsync(Urls.config_spinner);
        remoteAsync.type = RemoteAsync.CONFIG_SPINNER;
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
        if (type.equals(RemoteAsync.CONFIG_SPINNER)){
            try{
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());
                if (obj.getString("status").equals(Constants.SUCCESS)){
                    ArrayList<CountryLoadBean> countryLoadBeanArrayList=new ArrayList<CountryLoadBean>();
                    ArrayList<NationalityBean> nationalityBeanArrayList=new ArrayList<NationalityBean>();
                    ArrayList<AvailabilityBean> availabilityBeanArrayList=new ArrayList<AvailabilityBean>();
                    ArrayList<EmployeeBean> employeeBeanArrayList=new ArrayList<EmployeeBean>();
                    ArrayList<HouseBean> houseBeanArrayList=new ArrayList<HouseBean>();
                    ArrayList<ExperienceBean> experienceBeanArrayList=new ArrayList<ExperienceBean>();
                    ArrayList<DutyBean> dutyBeanArrayList=new ArrayList<DutyBean>();
                    JSONObject configobj = obj.getJSONObject("config");
                    JSONArray countryarr= configobj.getJSONArray("country");
                    JSONArray nationalityarr=configobj.getJSONArray("nationality");
                    JSONArray availabilityarr=configobj.getJSONArray("avaliability");
                    JSONArray employeearr=configobj.getJSONArray("employee");
                    JSONArray housearr=configobj.getJSONArray("house");
                    JSONArray experience_rangearr=configobj.getJSONArray("experience_range");
                    JSONArray main_dutyarr=configobj.getJSONArray("main_duty");
                    CountryLoadBean country1=new CountryLoadBean();
                    country1.setKey("Select Country");
                    country1.setValue("Select Country");
                    countryLoadBeanArrayList.add(country1);
                    dataModel.countryLoadBeanArrayList=countryLoadBeanArrayList;
                    if(countryarr.length()>0){
                        for(int i=0;i<countryarr.length();i++){
                            CountryLoadBean country=new CountryLoadBean();
                            JSONObject countrynames=countryarr.getJSONObject(i);
                            country.setKey(countrynames.getString("key"));
                            country.setValue(countrynames.getString("value"));
                            countryLoadBeanArrayList.add(country);
                        }
                        dataModel.countryLoadBeanArrayList=countryLoadBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for country found!");
                    }
                    NationalityBean nationality1=new NationalityBean();
                    nationality1.setKey("Select Nationality");
                    nationality1.setValue("Select Nationality");
                    nationalityBeanArrayList.add(nationality1);
                    dataModel.nationalityBeanArrayList=nationalityBeanArrayList;
                    if(nationalityarr.length()>0){
                        for(int i=0;i<nationalityarr.length();i++){
                            NationalityBean nationality=new NationalityBean();
                            JSONObject nationalitynames=nationalityarr.getJSONObject(i);
                            nationality.setKey(nationalitynames.getString("key"));
                            nationality.setValue(nationalitynames.getString("value"));
                            nationalityBeanArrayList.add(nationality);
                        }
                        dataModel.nationalityBeanArrayList=nationalityBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for nationality found!");
                    }
                    AvailabilityBean availability1=new AvailabilityBean();
                    availability1.setValue("Select Job Availibility");
                    availability1.setKey("Select Job Availibility");
                    availabilityBeanArrayList.add(availability1);
                    dataModel.availabilityBeanArrayList=availabilityBeanArrayList;
                    if(availabilityarr.length()>0){
                        for(int i=0;i<availabilityarr.length();i++){
                            AvailabilityBean availability=new AvailabilityBean();
                            JSONObject availabiltiy=availabilityarr.getJSONObject(i);
                            availability.setKey(availabiltiy.getString("key"));
                            availability.setValue(availabiltiy.getString("value"));
                            availabilityBeanArrayList.add(availability);
                        }
                        dataModel.availabilityBeanArrayList=availabilityBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for availability found!");
                    }
                    EmployeeBean employee1=new EmployeeBean();
                    employee1.setKey("Select Employer Type");
                    employee1.setValue("Select Employer Type");
                    employeeBeanArrayList.add(employee1);
                    dataModel.employeeBeanArrayList=employeeBeanArrayList;
                    if(employeearr.length()>0){
                        for(int i=0;i<employeearr.length();i++){
                            EmployeeBean employee=new EmployeeBean();
                            JSONObject employer=employeearr.getJSONObject(i);
                            employee.setKey(employer.getString("key"));
                            employee.setValue(employer.getString("value"));
                            employeeBeanArrayList.add(employee);
                        }
                        dataModel.employeeBeanArrayList=employeeBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for employee found!");
                    }
                    HouseBean house1=new HouseBean();
                    house1.setKey("Select Job House");
                    house1.setValue("Select Job House");
                    houseBeanArrayList.add(house1);
                    dataModel.houseBeanArrayList=houseBeanArrayList;
                    if(housearr.length()>0){
                        for(int i=0;i<housearr.length();i++){
                            HouseBean house=new HouseBean();
                            JSONObject housenames=housearr.getJSONObject(i);
                            house.setKey(housenames.getString("key"));
                            house.setValue(housenames.getString("value"));
                            houseBeanArrayList.add(house);
                        }
                        dataModel.houseBeanArrayList=houseBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for house found!");
                    }
                    ExperienceBean exp1=new ExperienceBean();
                    exp1.setValue("Select Experience Range");
                    exp1.setKey("Select Experience Range");
                    experienceBeanArrayList.add(exp1);
                    dataModel.experienceBeanArrayList=experienceBeanArrayList;
                    if(experience_rangearr.length()>0){
                        for(int i=0;i<experience_rangearr.length();i++){
                            ExperienceBean exp=new ExperienceBean();
                            JSONObject expnames=experience_rangearr.getJSONObject(i);
                            exp.setKey(expnames.getString("key"));
                            exp.setValue(expnames.getString("value"));
                            experienceBeanArrayList.add(exp);
                        }
                        dataModel.experienceBeanArrayList=experienceBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for experience found!");
                    }
                    DutyBean duty1=new DutyBean();
                    duty1.setValue("Select Main Duty");
                    duty1.setKey("Select Main Duty");
                    dutyBeanArrayList.add(duty1);
                    dataModel.dutyBeanArrayList=dutyBeanArrayList;
                    if(main_dutyarr.length()>0){
                        for(int i=0;i<main_dutyarr.length();i++){
                            DutyBean duty=new DutyBean();
                            JSONObject dutynames=main_dutyarr.getJSONObject(i);
                            duty.setKey(dutynames.getString("key"));
                            duty.setValue(dutynames.getString("value"));
                            dutyBeanArrayList.add(duty);
                        }
                        dataModel.dutyBeanArrayList=dutyBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for duty found!");
                    }
                    Intent i = new Intent(MainActivity.this,
                            ImageSliderScreen.class);
                    startActivity(i);
                    finish();

                }else if(obj.getString("status").equals("100")){
                    AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
                    alerBuilder.setMessage("Error in connecting Server please press try again to load the application again!!!!");
                    alerBuilder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getSpinnerElement();

                        }
                    });

                    AlertDialog alertDialog = alerBuilder.create();
                    alertDialog.show();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
