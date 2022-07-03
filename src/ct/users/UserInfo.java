package ct.users;

public class UserInfo
{
    private final String firstName, mi, lastName, address, city, state, zipCode;

    public UserInfo(String firstName, String mi, String lastName, String address, String city, String state, String zipCode)
    {
        this.firstName = firstName;
        this.mi = mi;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getMi() {
        return mi;
    }
    public String getLastName() {
        return lastName;
    }
    public String getAddress() {
        return address;
    }
    public String getCity() {
        return city;
    }
    public String getState() {
        return state;
    }
    public String getZipCode() {
        return zipCode;
    }
}
