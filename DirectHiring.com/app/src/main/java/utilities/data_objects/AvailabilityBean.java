package utilities.data_objects;

/**
 * Created by xyxz on 6/30/2016.
 */
public class AvailabilityBean {
    public String getAvailabilty_value() {
        return availabilty_value;
    }

    public void setAvailabilty_value(String availabilty_value) {
        this.availabilty_value = availabilty_value;
    }

    public String getAvailabilty_key() {
        return availabilty_key;
    }

    public void setAvailabilty_key(String availabilty_key) {
        this.availabilty_key = availabilty_key;
    }

    private String availabilty_key="";
    private String availabilty_value="";
}
