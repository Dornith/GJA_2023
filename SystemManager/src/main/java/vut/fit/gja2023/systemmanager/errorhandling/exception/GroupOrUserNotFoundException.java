package vut.fit.gja2023.systemmanager.errorhandling.exception;

public class GroupOrUserNotFoundException extends BaseNotFoundException {

    @Override
    public String getCode() {
        return super.getCode() + ".groupOrUser";
    }
}
