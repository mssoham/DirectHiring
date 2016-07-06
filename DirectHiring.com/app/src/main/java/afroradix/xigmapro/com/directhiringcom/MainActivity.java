package afroradix.xigmapro.com.directhiringcom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

public class MainActivity extends Activity implements AsyncResponse {
    ProgressDialog progressDialog;
    DirectHiringModel dataModel = DirectHiringModel.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread welcomeThread = new Thread(){
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(10000);  //Delay of 10 seconds
                } catch (Exception e) {

                } finally {
                    Intent i = new Intent(MainActivity.this,
                            ImageSliderScreen.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
        getSpinnerElement();
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
                    country1.setCountry_key("Select Country");
                    country1.setCountryvalue("Select Country");
                    countryLoadBeanArrayList.add(country1);
                    dataModel.countryLoadBeanArrayList=countryLoadBeanArrayList;
                    if(countryarr.length()>0){
                        for(int i=0;i<countryarr.length();i++){
                            CountryLoadBean country=new CountryLoadBean();
                            JSONObject countrynames=countryarr.getJSONObject(i);
                            country.setCountry_key(countrynames.getString("key"));
                            country.setCountryvalue(countrynames.getString("value"));
                            countryLoadBeanArrayList.add(country);
                        }
                        dataModel.countryLoadBeanArrayList=countryLoadBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for country found!");
                    }
                    NationalityBean nationality1=new NationalityBean();
                    nationality1.setNationality_value("Select Nationality");
                    nationality1.setNationality_key("Select Nationality");
                    nationalityBeanArrayList.add(nationality1);
                    dataModel.nationalityBeanArrayList=nationalityBeanArrayList;
                    if(nationalityarr.length()>0){
                        for(int i=0;i<nationalityarr.length();i++){
                            NationalityBean nationality=new NationalityBean();
                            JSONObject nationalitynames=nationalityarr.getJSONObject(i);
                            nationality.setNationality_key(nationalitynames.getString("key"));
                            nationality.setNationality_value(nationalitynames.getString("value"));
                            nationalityBeanArrayList.add(nationality);
                        }
                        dataModel.nationalityBeanArrayList=nationalityBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for nationality found!");
                    }
                    AvailabilityBean availability1=new AvailabilityBean();
                    availability1.setAvailabilty_value("Select Job Availibility");
                    availability1.setAvailabilty_key("Select Job Availibility");
                    availabilityBeanArrayList.add(availability1);
                    dataModel.availabilityBeanArrayList=availabilityBeanArrayList;
                    if(availabilityarr.length()>0){
                        for(int i=0;i<availabilityarr.length();i++){
                            AvailabilityBean availability=new AvailabilityBean();
                            JSONObject availabiltiy=availabilityarr.getJSONObject(i);
                            availability.setAvailabilty_key(availabiltiy.getString("key"));
                            availability.setAvailabilty_value(availabiltiy.getString("value"));
                            availabilityBeanArrayList.add(availability);
                        }
                        dataModel.availabilityBeanArrayList=availabilityBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for availability found!");
                    }
                    if(employeearr.length()>0){
                        for(int i=0;i<employeearr.length();i++){
                            EmployeeBean employee=new EmployeeBean();
                            JSONObject employer=employeearr.getJSONObject(i);
                            employee.setEmployee_key(employer.getString("key"));
                            employee.setEmployee_value(employer.getString("value"));
                            employeeBeanArrayList.add(employee);
                        }
                        dataModel.employeeBeanArrayList=employeeBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for employee found!");
                    }
                    if(housearr.length()>0){
                        for(int i=0;i<housearr.length();i++){
                            HouseBean house=new HouseBean();
                            JSONObject housenames=housearr.getJSONObject(i);
                            house.setHouse_key(housenames.getString("key"));
                            house.setHouse_value(housenames.getString("value"));
                            houseBeanArrayList.add(house);
                        }
                        dataModel.houseBeanArrayList=houseBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for house found!");
                    }
                    ExperienceBean exp1=new ExperienceBean();
                    exp1.setExp_value("Select Experience Range");
                    exp1.setExp_key("Select Experience Range");
                    experienceBeanArrayList.add(exp1);
                    dataModel.experienceBeanArrayList=experienceBeanArrayList;
                    if(experience_rangearr.length()>0){
                        for(int i=0;i<experience_rangearr.length();i++){
                            ExperienceBean exp=new ExperienceBean();
                            JSONObject expnames=experience_rangearr.getJSONObject(i);
                            exp.setExp_key(expnames.getString("key"));
                            exp.setExp_value(expnames.getString("value"));
                            experienceBeanArrayList.add(exp);
                        }
                        dataModel.experienceBeanArrayList=experienceBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for experience found!");
                    }
                    DutyBean duty1=new DutyBean();
                    duty1.setDuty_value("Select Main Duty");
                    duty1.setDuty_key("Select Main Duty");
                    dutyBeanArrayList.add(duty1);
                    dataModel.dutyBeanArrayList=dutyBeanArrayList;
                    if(main_dutyarr.length()>0){
                        for(int i=0;i<main_dutyarr.length();i++){
                            DutyBean duty=new DutyBean();
                            JSONObject dutynames=main_dutyarr.getJSONObject(i);
                            duty.setDuty_key(dutynames.getString("key"));
                            duty.setDuty_value(dutynames.getString("value"));
                            dutyBeanArrayList.add(duty);
                        }
                        dataModel.dutyBeanArrayList=dutyBeanArrayList;
                    }else{
                        CToast.show(getApplicationContext(),"no Data for duty found!");
                    }

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
