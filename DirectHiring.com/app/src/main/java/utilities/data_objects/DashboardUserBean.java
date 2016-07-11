package utilities.data_objects;

import java.util.ArrayList;

/**
 * Created by Self-3 on 7/6/2016.
 */
public class DashboardUserBean {
    private String id="";
    private String flag="";
    private String name="";
    private String email="";
    private String image="";
    private String username="";
    private String description="";
    private String date_of_birth="";
    private String location="";
    private String status="";
    private ArrayList<DashboardUserImageBean> dashboardUserImageBeanArrayList=new ArrayList<DashboardUserImageBean>();

    public ArrayList<DashboardUserImageBean> getDashboardUserImageBeanArrayList() {
        return dashboardUserImageBeanArrayList;
    }

    public void setDashboardUserImageBeanArrayList(ArrayList<DashboardUserImageBean> dashboardUserImageBeanArrayList) {
        this.dashboardUserImageBeanArrayList = dashboardUserImageBeanArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
