package utilities.data_objects;

/**
 * Created by xyxz on 7/1/2016.
 */
public class DateBean {
    private int type_value=0;
    public int getType_value() {
        return type_value;
    }

    public void setType_value(int type_value) {
        this.type_value = type_value;
    }



    public DateBean(int type_value){
        this.type_value=type_value;
    }
}
