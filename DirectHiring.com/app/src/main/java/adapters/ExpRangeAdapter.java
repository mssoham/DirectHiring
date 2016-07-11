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
import utilities.data_objects.ExperienceBean;

/**
 * Created by xyxz on 7/5/2016.
 */
public class ExpRangeAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<ExperienceBean> experienceBeanArrayList = new ArrayList<ExperienceBean>();
    public ExpRangeAdapter(Activity activity, ArrayList<ExperienceBean> experienceBeanArrayList) {
        this.activity = activity;
        this.experienceBeanArrayList = experienceBeanArrayList;

    }
    @Override
    public int getCount() {
        return experienceBeanArrayList.size();
    }

    @Override
    public Object getItem(int location) {
        return experienceBeanArrayList.get(location);
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

        ExperienceBean experienceBean=experienceBeanArrayList.get(position);
        type_txt.setText(experienceBean.getExp_value());
        return (convertView);
    }
}
