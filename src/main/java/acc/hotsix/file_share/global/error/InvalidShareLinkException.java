package acc.hotsix.file_share.global.error;

public class InvalidShareLinkException extends Exception {
    public InvalidShareLinkException() {
        super("The share link is invalid.");
    }
}
