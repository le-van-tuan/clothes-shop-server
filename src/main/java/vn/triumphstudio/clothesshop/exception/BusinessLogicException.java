package vn.triumphstudio.clothesshop.exception;

public class BusinessLogicException extends RuntimeException {

    private int errorCode;

    private String errorMessage;

    public BusinessLogicException() {
    }

    public BusinessLogicException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BusinessLogicException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
