package afroradix.xigmapro.com.directhiringcom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import utilities.others.CToast;

public class SeekbarTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekbar_test);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                CToast.show(getApplicationContext(),"Value >> "+btn1.getText().toString());
                break;
            case R.id.btn2:
                CToast.show(getApplicationContext(),"Value >> "+btn2.getText().toString());
                break;
        }
    }
}
