package utilities.data_objects;

import java.util.ArrayList;

/**
 * Created by xyxz on 7/22/2016.
 */
public class UserPhotosLoadBean {
    private String id="";
    private String image="";
    private ArrayList<UserPhotosLoadBean> userPhotosLoadBeanArrayList=new ArrayList<UserPhotosLoadBean>();
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<UserPhotosLoadBean> getUserPhotosLoadBeanArrayList() {
        return userPhotosLoadBeanArrayList;
    }

    public void setUserPhotosLoadBeanArrayList(ArrayList<UserPhotosLoadBean> userPhotosLoadBeanArrayList) {
        this.userPhotosLoadBeanArrayList = userPhotosLoadBeanArrayList;
    }
}
