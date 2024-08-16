package acc.hotsix.file_share.global.error.exception;

public class MissingSearchParamException extends Exception{
    public MissingSearchParamException() {
        super("Search criteria does not exist.");
    }
}
