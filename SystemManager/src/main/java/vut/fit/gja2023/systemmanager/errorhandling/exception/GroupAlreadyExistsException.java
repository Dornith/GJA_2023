package vut.fit.gja2023.systemmanager.errorhandling.exception;

public class GroupAlreadyExistsException extends BaseConflictException {

    public GroupAlreadyExistsException(String groupName) {
        super(groupName);
    }

    @Override
    public String getCode() {
        return super.getCode() + ".group";
    }
}
