package ct.users;

public abstract class User
{
    private String userID;
    private UserInfo userInfo;

    public User(String userID, UserInfo userInfo)
    {
        this.userID = userID;
        this.userInfo = userInfo;
    }

    public String getUserID() {
        return userID;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
