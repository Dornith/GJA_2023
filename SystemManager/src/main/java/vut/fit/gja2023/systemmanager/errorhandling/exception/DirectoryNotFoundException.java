package vut.fit.gja2023.systemmanager.errorhandling.exception;

public class DirectoryNotFoundException extends BaseNotFoundException {

    public DirectoryNotFoundException(String path) {
        super(path);
    }

    @Override
    public String getCode() {
        return super.getCode() + ".directory";
    }
}
