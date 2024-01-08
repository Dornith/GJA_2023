package vut.fit.gja2023.systemmanager.errorhandling.exception;

public class PathNotUnderBasePathException extends BaseBadRequestException {

    public PathNotUnderBasePathException(String basePath) {
        super(basePath);
    }

    @Override
    public String getCode() {
        return super.getCode() + ".path.not.under.base.path";
    }
}
