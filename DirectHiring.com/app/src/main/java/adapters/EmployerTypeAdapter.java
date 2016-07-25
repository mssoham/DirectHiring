package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import afroradix.xigmapro.com.directhiringcom.R;
import utilities.data_objects.AvailabilityBean;
import utilities.data_objects.EmployeeBean;

/**
 * Created by xyxz on 7/6/2016.
 */
public class EmployerTypeAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<EmployeeBean> employeeBeanArrayList = new ArrayList<EmployeeBean>();
    public EmployerTypeAdapter(Activity activity, ArrayList<EmployeeBean> employeeBeanArrayList) {
        this.activity = activity;
        this.employeeBeanArrayList = employeeBeanArrayList;

    }
    @Override
    public int getCount() {
        return employeeBeanArrayList.size();
    }

    @Override
    public Object getItem(int location) {
        return employeeBeanArrayList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.inflate_type1, null);
        TextView type_txt=(TextView)convertView.findViewById(R.id.type_txt);

        EmployeeBean employeeBean=employeeBeanArrayList.get(position);
        type_txt.setText(employeeBean.getValue());
        return (convertView);
    }
}
