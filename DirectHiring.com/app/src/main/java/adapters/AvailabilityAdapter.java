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
import utilities.data_objects.CountryLoadBean;

/**
 * Created by xyxz on 7/5/2016.
 */
public class AvailabilityAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<AvailabilityBean> availabilityBeanArrayList = new ArrayList<AvailabilityBean>();
    public AvailabilityAdapter(Activity activity, ArrayList<AvailabilityBean> availabilityBeanArrayList) {
        this.activity = activity;
        this.availabilityBeanArrayList = availabilityBeanArrayList;

    }
    @Override
    public int getCount() {
        return availabilityBeanArrayList.size();
    }

    @Override
    public Object getItem(int location) {
        return availabilityBeanArrayList.get(location);
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
            convertView = inflater.inflate(R.layout.inflate_type, null);
        TextView type_txt=(TextView)convertView.findViewById(R.id.type_txt);

        AvailabilityBean availabilityBean=availabilityBeanArrayList.get(position);
        type_txt.setText(availabilityBean.getAvailabilty_value());
        return (convertView);
    }
}
