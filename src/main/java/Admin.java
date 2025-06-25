public class Admin extends User {
    public Admin(String username, String phoneNumber, String email, String password, String userType) {
        super(username, phoneNumber, email, password, userType);
    }

    @Override
    public boolean isCustomer() {
        return false;
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public boolean isStudent() {
        return false;
    }

    @Override
    public String toString() {
        return username + "," + phoneNumber + "," + email + "," + password;
    }
}