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
import utilities.data_objects.NationalityBean;

/**
 * Created by xyxz on 7/5/2016.
 */
public class NationalityAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<NationalityBean> nationalityBeanArrayList = new ArrayList<NationalityBean>();
    public NationalityAdapter(Activity activity, ArrayList<NationalityBean> nationalityBeanArrayList) {
        this.activity = activity;
        this.nationalityBeanArrayList = nationalityBeanArrayList;

    }
    @Override
    public int getCount() {
        return nationalityBeanArrayList.size();
    }

    @Override
    public Object getItem(int location) {
        return nationalityBeanArrayList.get(location);
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

        NationalityBean nationalityBean=nationalityBeanArrayList.get(position);
        type_txt.setText(nationalityBean.getNationality_value());
        return (convertView);
    }
}
