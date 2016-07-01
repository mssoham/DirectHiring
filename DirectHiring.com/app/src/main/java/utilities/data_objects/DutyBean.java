package utilities.data_objects;

/**
 * Created by xyxz on 6/30/2016.
 */
public class DutyBean {
    public String getDuty_key() {
        return duty_key;
    }

    public void setDuty_key(String duty_key) {
        this.duty_key = duty_key;
    }

    public String getDuty_value() {
        return duty_value;
    }

    public void setDuty_value(String duty_value) {
        this.duty_value = duty_value;
    }

    private String duty_key="";
    private String duty_value="";
}
