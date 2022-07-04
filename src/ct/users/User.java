package ct.users;

public class User
{
    private final String userID;
    private final UserType type;
    private final UserInfo userInfo;

    public User(String userID, UserType type, UserInfo userInfo)
    {
        this.userID = userID;
        this.type = type;
        this.userInfo = userInfo;
    }

    public String getUserID() {
        return userID;
    }

    public UserType getUserType() {
        return type;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
