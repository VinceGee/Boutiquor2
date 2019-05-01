package zw.co.vokers.vinceg.boutiquor.models;

/**
 * Created by Vince G on 2/3/2019
 */

public class User {
    private String uniqueId;
    private String fullname;
    private String role;
    private String username;
    private String mobile;
    private String email;
    private String password;
    private String salt;
    private String address;
    private String created;

    public User(String uniqueId, String fullname, String role, String username, String mobile, String email, String password, String salt, String address, String created) {
        this.uniqueId = uniqueId;
        this.fullname = fullname;
        this.role = role;
        this.username = username;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.address = address;
        this.created = created;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
