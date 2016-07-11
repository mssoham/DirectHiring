package afroradix.xigmapro.com.directhiringcom;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import adapters.TypeSpinnerAdapter;
import utilities.data_objects.DirectHiringModel;

public class TypeSelectionSignUp extends AppCompatActivity {
    Spinner type;
    Button continue_btn;
    TypeSpinnerAdapter adapter;
    DirectHiringModel dataModel = DirectHiringModel.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_selection_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Welcome");
        type=(Spinner)findViewById(R.id.type);
        adapter = new TypeSpinnerAdapter(this, DirectHiringModel.getInstance().typeSpinnerBeanArrayList);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SignupFirstPage.register_type=String.valueOf(dataModel.getInstance().typeSpinnerBeanArrayList.get(position).getType_value());
                Log.e("registration---->",SignupFirstPage.register_type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        continue_btn=(Button)findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intMain3=new Intent(TypeSelectionSignUp.this,SignupFirstPage.class);
                startActivity(intMain3);
            }
        });

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
}
