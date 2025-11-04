package Model;

public class User {
    private int user_id;
    private String user_name;
    private String user_phone;
    private String user_username;
    private String user_password;

    public User(int user_id, String user_name, String user_phone, String user_username, String user_password) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.user_username = user_username;
        this.user_password = user_password;
    }

    public User() {}

    // Getters and Setters
    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getUserName() { return user_name; }
    public void setUserName(String user_name) { this.user_name = user_name; }

    public String getUserPhone() { return user_phone; }
    public void setUserPhone(String user_phone) { this.user_phone = user_phone; }

    public String getUserUsername() { return user_username; }
    public void setUserUsername(String user_username) { this.user_username = user_username; }

    public String getUserPassword() { return user_password; }
    public void setUserPassword(String user_password) { this.user_password = user_password; }
}
