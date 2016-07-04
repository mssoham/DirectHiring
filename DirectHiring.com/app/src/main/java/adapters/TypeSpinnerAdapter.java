package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;

import afroradix.xigmapro.com.directhiringcom.R;
import utilities.data_objects.TypeSpinnerBean;

/**
 * Created by xyxz on 7/1/2016.
 */
public class TypeSpinnerAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<TypeSpinnerBean> typeSpinnerBeanArrayList = new ArrayList<TypeSpinnerBean>();

    /*private ItemFilter mFilter = new ItemFilter();*/
    public TypeSpinnerAdapter(Activity activity, ArrayList<TypeSpinnerBean> typeSpinnerBeanArrayList) {
        this.activity = activity;
        this.typeSpinnerBeanArrayList = typeSpinnerBeanArrayList;

    }
    @Override
    public int getCount() {
        return typeSpinnerBeanArrayList.size();
    }

    @Override
    public Object getItem(int location) {
        return typeSpinnerBeanArrayList.get(location);
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

        TypeSpinnerBean typeSpinnerBean=typeSpinnerBeanArrayList.get(position);
        type_txt.setText(typeSpinnerBean.getType_value());

        return (convertView);
    }
}
