package vut.fit.gja2023.systemmanager.errorhandling.exception;

public class InvalidIPAddressException extends BaseBadRequestException {

    @Override
    public String getCode() {
        return super.getCode() + ".ip.address.invalid";
    }
}
