package acc.hotsix.file_share.global.error.exception;

public class InvalidShareLinkException extends Exception {
    public InvalidShareLinkException() {
        super("The share link is invalid.");
    }
}
