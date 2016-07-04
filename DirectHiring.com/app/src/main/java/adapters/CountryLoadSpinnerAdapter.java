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

/**
 * Created by xyxz on 7/1/2016.
 */
public class CountryLoadSpinnerAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<CountryLoadBean> countryLoadBeanArrayList = new ArrayList<CountryLoadBean>();
    public CountryLoadSpinnerAdapter(Activity activity, ArrayList<CountryLoadBean> countryLoadBeanArrayList) {
        this.activity = activity;
        this.countryLoadBeanArrayList = countryLoadBeanArrayList;

    }
    @Override
    public int getCount() {
        return countryLoadBeanArrayList.size();
    }

    @Override
    public Object getItem(int location) {
        return countryLoadBeanArrayList.get(location);
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

        CountryLoadBean countryLoadBean=countryLoadBeanArrayList.get(position);
        type_txt.setText(countryLoadBean.getCountryvalue());

        return (convertView);
    }
}
