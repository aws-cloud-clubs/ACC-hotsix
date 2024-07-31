package acc.hotsix.file_share.global.error;

public class MissingSearchRequestParameterException extends Exception{
    public MissingSearchRequestParameterException(String message) {
        super("Search criteria does not exist.");
    }
}
