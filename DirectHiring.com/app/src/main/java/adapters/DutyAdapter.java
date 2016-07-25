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
import utilities.data_objects.CountryLoadBean;
import utilities.data_objects.DutyBean;

/**
 * Created by xyxz on 7/5/2016.
 */
public class DutyAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<DutyBean> dutyBeanArrayList = new ArrayList<DutyBean>();
    public DutyAdapter(Activity activity, ArrayList<DutyBean> dutyBeanArrayList) {
        this.activity = activity;
        this.dutyBeanArrayList = dutyBeanArrayList;

    }
    @Override
    public int getCount() {
        return dutyBeanArrayList.size();
    }

    @Override
    public Object getItem(int location) {
        return dutyBeanArrayList.get(location);
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

        DutyBean dutyBean=dutyBeanArrayList.get(position);
        type_txt.setText(dutyBean.getValue());
        type_txt.setSingleLine(false);
        return (convertView);
    }
}
