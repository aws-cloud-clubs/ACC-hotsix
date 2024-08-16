package acc.hotsix.file_share.global.error.exception;

public class InvalidQueryParamException extends Exception{
    public InvalidQueryParamException() {
        super("Name, time parameter must be 'asc', 'desc'.");
    }
}
