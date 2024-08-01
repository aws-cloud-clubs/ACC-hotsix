package acc.hotsix.file_share.global.error;

public class InvalidQueryReqParamException extends Exception{
    public InvalidQueryReqParamException() {
        super("Name, time parameter must be 'asc', 'desc'.");
    }
}
