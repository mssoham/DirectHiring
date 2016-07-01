package utilities.data_objects;

import java.util.ArrayList;

/**
 * Created by Self-3 on 6/27/2016.
 */
public class DirectHiringModel {
    private static DirectHiringModel ourInstance = new DirectHiringModel();

    public static DirectHiringModel getInstance() {
        return ourInstance;
    }
    private DirectHiringModel() {

    }

    public CountryLoadBean countryBean;

    public ArrayList<CountryLoadBean> countryLoadBeanArrayList = new ArrayList<CountryLoadBean>();
    public ArrayList<NationalityBean> nationalityBeanArrayList = new ArrayList<NationalityBean>();
    public ArrayList<EmployeeBean> employeeBeanArrayList = new ArrayList<EmployeeBean>();
    public ArrayList<ExperienceBean> experienceBeanArrayList = new ArrayList<ExperienceBean>();
    public ArrayList<HouseBean> houseBeanArrayList = new ArrayList<HouseBean>();
    public ArrayList<DutyBean> dutyBeanArrayList = new ArrayList<DutyBean>();
    public ArrayList<AvailabilityBean> availabilityBeanArrayList = new ArrayList<AvailabilityBean>();

}
