package vut.fit.gja2023.systemmanager.errorhandling.exception;

public class GroupNotFoundException extends BaseNotFoundException {

    public GroupNotFoundException(String... args) {
        super(args);
    }

    public GroupNotFoundException(String groupName) {
        super(groupName);
    }

    @Override
    public String getCode() {
        return super.getCode() + ".group";
    }

}
