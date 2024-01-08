package vut.fit.gja2023.systemmanager.errorhandling.exception;

public class FileAlreadyExistsException extends BaseConflictException {

    public FileAlreadyExistsException(String path) {
        super(path);
    }

    @Override
    public String getCode() {
        return super.getCode() + ".file.already.exists";
    }
}
