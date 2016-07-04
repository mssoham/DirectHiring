package utilities.data_objects;

/**
 * Created by xyxz on 7/1/2016.
 */
public class TypeSpinnerBean {
    private String type_key="";
    private String type_value="";

    public TypeSpinnerBean(String type_key, String type_value){
        this.type_key=type_key;
        this.type_value=type_value;
    }

    public String getType_key() {
        return type_key;
    }

    public void setType_key(String type_key) {
        this.type_key = type_key;
    }

    public String getType_value() {
        return type_value;
    }

    public void setType_value(String type_value) {
        this.type_value = type_value;
    }


}
