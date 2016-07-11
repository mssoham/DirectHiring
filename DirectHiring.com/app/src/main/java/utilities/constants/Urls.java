package utilities.constants;


public class Urls
{

    public static String urlkey="";
    private static final String url="http://xigmapro.website/dev4/directhiring/Services/service.php?action=";
    private static final String url1="http://xigmapro.website/dev4/directhiring/Services/config.php?action=";
    private static final String url2="http://xigmapro.website/dev4/directhiring/Services/chat_service.php?action=";

    public static final String config_spinner = url1+"config";
    public static final String login = url+"login";
    public static final String registration = url+"registration";
    public static final String registration_helper = url+"registration_helper";
    public static final String self = url+"self";
    public static final String dashBoard_Data = url+"dashBoardData";

    /* Services for chat start */
    public static final String get_Chat_Users = url2+"getChatUsers";
    public static final String get_Chat_Messages = url2+"getChatMessages";
    public static final String send_Messages = url2+"sendMessages";
    /* Services for chat end */

    public static final String criteria = url+"criteria";

}
