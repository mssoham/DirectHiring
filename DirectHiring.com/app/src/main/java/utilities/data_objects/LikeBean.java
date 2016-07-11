package utilities.data_objects;

/**
 * Created by Self-3 on 7/11/2016.
 */
public class LikeBean {
    private String name="";
    private String image="";
    private String age="";
    private String location="";

    public LikeBean(){

    }

    public LikeBean(String name, String image, String age, String location){
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
}
