package afroradix.xigmapro.com.directhiringcom;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import adapters.AvailabilityAdapter;
import adapters.CountryLoadSpinnerAdapter;
import adapters.DutyAdapter;
import adapters.ExpRangeAdapter;
import adapters.NationalityAdapter;
import utilities.data_objects.DirectHiringModel;
import utilities.data_objects.NationalityBean;
import utilities.data_objects.UserBean;
import utilities.others.CToast;

public class CriteriaType extends Activity {
   private CheckBox nationality_check,main_duty_check,availibility_check,exp_range_check,sal_range_check,
            age_range_check,day_of_range_check;
    private Spinner nationality,main_duty,availibility,exp_range;
    DirectHiringModel dataModel = DirectHiringModel.getInstance();
    NationalityAdapter nationality_adapter;
    AvailabilityAdapter availability_adapter;
    DutyAdapter duty_adapter;
    ExpRangeAdapter exprange_adapter;
    int check = 0;
    UserBean user=new UserBean();
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
        availibility=(Spinner)findViewById(R.id.availibility);
        exp_range=(Spinner)findViewById(R.id.exp_range);
        nationality_adapter = new NationalityAdapter(this, DirectHiringModel.getInstance().nationalityBeanArrayList);
        availability_adapter = new AvailabilityAdapter(this, DirectHiringModel.getInstance().availabilityBeanArrayList);
        duty_adapter= new DutyAdapter(this, DirectHiringModel.getInstance().dutyBeanArrayList);
        exprange_adapter=new ExpRangeAdapter(this, DirectHiringModel.getInstance().experienceBeanArrayList);
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
                    check--;
                }
                if (user.getStatus().equals("normal")) {
                    if (check > 2) {
                        CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                        Log.e("check count---->", String.valueOf(check));
                        nationality_check.setChecked(false);
                        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(CriteriaType.this);
                        alerBuilder.setMessage("You need to pay to add more criteria. Click Continue to proceed");
                        alerBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intMain = new Intent(CriteriaType.this, PaypalWebView.class);
                                startActivity(intMain);
                            }
                        });

                        AlertDialog alertDialog = alerBuilder.create();
                        alertDialog.show();
                    }
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
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (check > 2) {
                    CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    main_duty_check.setChecked(false);
                    AlertDialog.Builder alerBuilder = new AlertDialog.Builder(CriteriaType.this);
                    alerBuilder.setMessage("You need to pay to add more criteria. Click Continue to proceed");
                    alerBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intMain = new Intent(CriteriaType.this, PaypalWebView.class);
                            startActivity(intMain);
                        }
                    });

                    AlertDialog alertDialog = alerBuilder.create();
                    alertDialog.show();
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
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if (check > 2) {
                    CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    availibility_check.setChecked(false);
                    AlertDialog.Builder alerBuilder = new AlertDialog.Builder(CriteriaType.this);
                    alerBuilder.setMessage("You need to pay to add more criteria. Click Continue to proceed");
                    alerBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intMain = new Intent(CriteriaType.this, PaypalWebView.class);
                            startActivity(intMain);
                        }
                    });

                    AlertDialog alertDialog = alerBuilder.create();
                    alertDialog.show();
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
                    check--;
                    Log.e("check count---->", String.valueOf(check));
                }
                if(check>2) {
                    CToast.show(getApplicationContext(), "you need to pay to check more criteria!");
                    Log.e("check count---->", String.valueOf(check));
                    exp_range_check.setChecked(false);
                    AlertDialog.Builder alerBuilder = new AlertDialog.Builder(CriteriaType.this);
                    alerBuilder.setMessage("You need to pay to add more criteria. Click Continue to proceed");
                    alerBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intMain = new Intent(CriteriaType.this, PaypalWebView.class);
                            startActivity(intMain);
                        }
                    });

                    AlertDialog alertDialog = alerBuilder.create();
                    alertDialog.show();
                }
            }

        });
    }
}
