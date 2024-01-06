package vut.fit.gja2023.systemmanager.errorhandling.exception;

public class UserNotFoundException extends BaseNotFoundException {

    public UserNotFoundException(String userName) {
        super(userName);
    }

    @Override
    public String getCode() {
        return super.getCode() + ".user";
    }
}
