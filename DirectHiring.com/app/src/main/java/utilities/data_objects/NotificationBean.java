package utilities.data_objects;

import custom_components.LikeHolder;
import custom_components.NotificationHolder;

/**
 * Created by xyxz on 7/14/2016.
 */
public class NotificationBean {
    private String name="";
    private String image="";
    private String age="";
    private String location="";
    private String fromid="";
    private String tag="0";
    public NotificationHolder holder = new NotificationHolder();

    public NotificationBean(){

    }

    public NotificationBean(String name, String image, String age, String location){
        this.name=name;
        this.image=image;
        this.age=age;
        this.location=location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
