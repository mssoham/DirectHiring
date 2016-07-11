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
import utilities.data_objects.HouseBean;

/**
 * Created by xyxz on 7/6/2016.
 */
public class HouseAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<HouseBean> houseBeanArrayList = new ArrayList<HouseBean>();
    public HouseAdapter(Activity activity, ArrayList<HouseBean> houseBeanArrayList) {
        this.activity = activity;
        this.houseBeanArrayList = houseBeanArrayList;

    }
    @Override
    public int getCount() {
        return houseBeanArrayList.size();
    }

    @Override
    public Object getItem(int location) {
        return houseBeanArrayList.get(location);
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

        HouseBean houseBean=houseBeanArrayList.get(position);
        type_txt.setText(houseBean.getHouse_value());
        return (convertView);
    }
}
