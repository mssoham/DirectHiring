package afroradix.xigmapro.com.directhiringcom.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import afroradix.xigmapro.com.directhiringcom.R;
import custom_components.RoundedImageViewWhiteBorder;
import utilities.async_tasks.ImageDownloaderTask;
import utilities.data_objects.DashboardUserImageBean;
import utilities.data_objects.DirectHiringModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageChangeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageChangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageChangeFragment extends android.support.v4.app.DialogFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String img_url="http://xigmapro.website/dev4/directhiring/public/resource/site/images/users/";
    private RoundedImageViewWhiteBorder dp_pd_edit;
    private ViewPager pager_edit;
    private ImageButton upld_edit;
    CustomPagerAdapter customPagerAdapter;
    DirectHiringModel dataModel=DirectHiringModel.getInstance();

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageChangeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageChangeFragment newInstance(String param1, String param2) {
        ImageChangeFragment fragment = new ImageChangeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageChangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_image_change, container, false);
        getDialog().setTitle("Change Image");
        dp_pd_edit=(RoundedImageViewWhiteBorder)view.findViewById(R.id.dp_pd_edit);
        pager_edit=(ViewPager)view.findViewById(R.id.pager_edit);
        /*customPagerAdapter=new CustomPagerAdapter(getActivity(),dataModel.dashboardUserBeanArrayList.getDashboardUserImageBeanArrayList());*/

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<DashboardUserImageBean> dashboardUserImageBeanArrayList = new ArrayList<DashboardUserImageBean>();

        public CustomPagerAdapter(Context context, ArrayList<DashboardUserImageBean> dashboardUserImageBeanArrayList) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.dashboardUserImageBeanArrayList=dashboardUserImageBeanArrayList;
        }

        @Override
        public int getCount() {
            return dashboardUserImageBeanArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_myprfl_item, container, false);

            DashboardUserImageBean dashboardUserImageBean=dashboardUserImageBeanArrayList.get(position);

            ImageView imageView_edit = (ImageView) itemView.findViewById(R.id.imageView_edit);
            ImageView add_image = (ImageView) itemView.findViewById(R.id.add_image);
            if(position==dashboardUserImageBeanArrayList.size()+1){
                imageView_edit.setVisibility(View.GONE);
                add_image.setVisibility(View.VISIBLE);
            }
            //imageView.setImageResource(mResources[position]);
            if (imageView_edit != null) {
                new ImageDownloaderTask(imageView_edit).execute(img_url+"/"+dashboardUserImageBean.getImage());
            }

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

}
