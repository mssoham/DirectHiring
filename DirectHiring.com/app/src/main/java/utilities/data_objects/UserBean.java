package utilities.data_objects;

import java.util.ArrayList;

/**
 * Created by Self-3 on 7/1/2016.
 */
public class UserBean {
    private String user_id="";
    private String social_id="";
    private String flag="";
    private String name="";
    private String email="";
    private String username="";
    private String image="";
    private String description="";
    private String date_of_birth="";
    private String location="";
    private String looking_for="";
    private String type="";
    private String status="";
    private String wallet="";
    private String remember_token="";
    private String created_at="";
    private String updated_at="";
    private ArrayList<UserCriteriaBean> userCriteriaBeanArrayList=new ArrayList<UserCriteriaBean>();
    ArrayList<UserPhotosLoadBean> userPhotosLoadBeanArrayList=new ArrayList<UserPhotosLoadBean>();

    public ArrayList<UserPhotosLoadBean> getUserPhotosLoadBeanArrayList() {
        return userPhotosLoadBeanArrayList;
    }

    public void setUserPhotosLoadBeanArrayList(ArrayList<UserPhotosLoadBean> userPhotosLoadBeanArrayList) {
        this.userPhotosLoadBeanArrayList = userPhotosLoadBeanArrayList;
    }



    public ArrayList<UserCriteriaBean> getUserCriteriaBeanArrayList() {
        return userCriteriaBeanArrayList;
    }

    public void setUserCriteriaBeanArrayList(ArrayList<UserCriteriaBean> userCriteriaBeanArrayList) {
        this.userCriteriaBeanArrayList = userCriteriaBeanArrayList;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLooking_for() {
        return looking_for;
    }

    public void setLooking_for(String looking_for) {
        this.looking_for = looking_for;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }
}
