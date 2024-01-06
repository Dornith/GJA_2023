package vut.fit.gja2023.systemmanager.errorhandling.exception;

public class GroupUserNotFoundException extends GroupNotFoundException {

    public GroupUserNotFoundException(String userName, String groupName) {
        super(userName, groupName);
    }

    @Override
    public String getCode() {
        return super.getCode() + ".user";
    }
}
