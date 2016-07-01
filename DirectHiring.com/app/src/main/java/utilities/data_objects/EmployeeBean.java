package utilities.data_objects;

/**
 * Created by xyxz on 6/30/2016.
 */
public class EmployeeBean {
    public String getEmployee_key() {
        return employee_key;
    }

    public void setEmployee_key(String employee_key) {
        this.employee_key = employee_key;
    }

    public String getEmployee_value() {
        return employee_value;
    }

    public void setEmployee_value(String employee_value) {
        this.employee_value = employee_value;
    }

    private String employee_key="";
    private String employee_value="";
}
