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
    //public UserBean user;

    public ArrayList<CountryLoadBean> countryLoadBeanArrayList = new ArrayList<CountryLoadBean>();
    public ArrayList<NationalityBean> nationalityBeanArrayList = new ArrayList<NationalityBean>();
    public ArrayList<EmployeeBean> employeeBeanArrayList = new ArrayList<EmployeeBean>();
    public ArrayList<ExperienceBean> experienceBeanArrayList = new ArrayList<ExperienceBean>();
    public ArrayList<HouseBean> houseBeanArrayList = new ArrayList<HouseBean>();
    public ArrayList<DutyBean> dutyBeanArrayList = new ArrayList<DutyBean>();
    public ArrayList<AvailabilityBean> availabilityBeanArrayList = new ArrayList<AvailabilityBean>();
    public ArrayList<TypeSpinnerBean> typeSpinnerBeanArrayList = new ArrayList<TypeSpinnerBean>();
    public ArrayList<DateBean> dateArrayList = new ArrayList<DateBean>();
    public ArrayList<DateBean> yearArrayList = new ArrayList<DateBean>();
    public ArrayList<DateBean> monthArrayList = new ArrayList<DateBean>();

    public UserBean userBean;
    public ArrayList<DashboardUserBean> dashboardUserBeanArrayList=new ArrayList<DashboardUserBean>();
    public ArrayList<ChatUsersBean> chatUsersBeanArrayList=new ArrayList<ChatUsersBean>();
    public ArrayList<ChatListBean> chatListBeanArrayList = new ArrayList<ChatListBean>();

    public int search_user=0;

}
