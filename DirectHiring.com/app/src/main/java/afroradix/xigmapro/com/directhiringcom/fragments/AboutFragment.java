package afroradix.xigmapro.com.directhiringcom.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import afroradix.xigmapro.com.directhiringcom.MYProfile;
import afroradix.xigmapro.com.directhiringcom.R;
import shared_pref.SharedStorage;
import utilities.async_tasks.AsyncResponse;
import utilities.async_tasks.RemoteAsync;
import utilities.constants.Constants;
import utilities.constants.Urls;
import utilities.data_objects.DirectHiringModel;
import utilities.others.CToast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener, AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText description_edit;
    ProgressDialog progressDialog;
    DirectHiringModel dataModel=DirectHiringModel.getInstance();
    private TextInputLayout input_layout_description_edit;
    Button submit_edit;
    String description;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AboutFragment() {
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
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        getDialog().setTitle("Edit Description");
        description_edit=(EditText)view.findViewById(R.id.description_edit);
        input_layout_description_edit=(TextInputLayout)view.findViewById(R.id.input_layout_description_edit);
        description_edit.setText(dataModel.userBean.getDescription());
        submit_edit=(Button)view.findViewById(R.id.submit_edit);
        submit_edit.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if(v==submit_edit){
            description=description_edit.getText().toString().trim();
            changedescription();
        }
    }

    @Override
    public void processFinish(String type, String output) {
        Log.e("Output----->",output);
        stop_progress_dialog();
        if (type.equals(RemoteAsync.CHANGEDESCRIPTION)) {
            try {
                JSONObject obj = new JSONObject(output);
                Log.e("Response-->", obj.toString());

                if (obj.getString("status").equals(Constants.SUCCESS)) {
                    CToast.show(getActivity(), "success");
                    dataModel.userBean.setDescription(description);
                    Intent intent=new Intent(getActivity(),MYProfile.class);
                    startActivity(intent);
                }else{
                    CToast.show(getActivity(),"failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    private void changedescription(){
        start_progress_dialog();

        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        String user_id = SharedStorage.getValue(getActivity(), "UserId");
        arrayList.add(new org.apache.http.message.BasicNameValuePair("user_id", user_id));
        arrayList.add(new org.apache.http.message.BasicNameValuePair("description", description_edit.getText().toString().trim()));

        RemoteAsync remoteAsync = new RemoteAsync(Urls.changeDescription);
        remoteAsync.type = RemoteAsync.CHANGEDESCRIPTION;
        remoteAsync.delegate = this;
        remoteAsync.execute(arrayList);
    }
    void start_progress_dialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    void stop_progress_dialog(){
        progressDialog.dismiss();
    }


}
