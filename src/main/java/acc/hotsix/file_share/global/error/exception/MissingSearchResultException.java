package acc.hotsix.file_share.global.error.exception;

public class MissingSearchResultException extends Exception{
    public MissingSearchResultException() {
        super("Search results do not exist.");
    }
}
