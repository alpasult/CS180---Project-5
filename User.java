/**
 * 
 * A Simple User super class, Customer and Seller are children of the User
 * Tracks login and password
 * 
 * @author Alpamys Sultanbek
 * @version 1.0
 */
public class User {
    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
