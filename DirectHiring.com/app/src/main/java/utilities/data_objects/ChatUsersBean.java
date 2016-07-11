package utilities.data_objects;

/**
 * Created by Self-3 on 7/8/2016.
 */
public class ChatUsersBean {
    private String member_id="";
    private String chat_initiation_id="";
    private String name="";
    private String image="";

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getChat_initiation_id() {
        return chat_initiation_id;
    }

    public void setChat_initiation_id(String chat_initiation_id) {
        this.chat_initiation_id = chat_initiation_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
