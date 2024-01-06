package vut.fit.gja2023.systemmanager.errorhandling.exception;

public class UserAlreadyExistsException extends BaseConflictException {

    public UserAlreadyExistsException(String userName) {
        super(userName);
    }

    @Override
    public String getCode() {
        return super.getCode() + ".user";
    }
}
