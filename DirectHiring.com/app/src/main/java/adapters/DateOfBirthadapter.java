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
import utilities.data_objects.DateBean;

/**
 * Created by xyxz on 7/1/2016.
 */
public class DateOfBirthadapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<DateBean> dateBeanArrayList = new ArrayList<DateBean>();


    /*private ItemFilter mFilter = new ItemFilter();*/
    public DateOfBirthadapter(Activity activity, ArrayList<DateBean> dateBeanArrayList) {
        this.activity = activity;
        this.dateBeanArrayList = dateBeanArrayList;

    }
    @Override
    public int getCount() {
        return dateBeanArrayList.size();
    }

    @Override
    public Object getItem(int location) {
        return dateBeanArrayList.get(location);
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
            convertView = inflater.inflate(R.layout.inflate_date_of_birth, null);
        TextView date_of_birth=(TextView)convertView.findViewById(R.id.date_of_birth);

        DateBean DateBean=dateBeanArrayList.get(position);
        date_of_birth.setText(String.valueOf(DateBean.getType_value()));

        return (convertView);
    }
}
