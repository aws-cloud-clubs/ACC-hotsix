package acc.hotsix.file_share.global.error;

public class NoQueryFilesException extends Exception{
    public NoQueryFilesException() {
        super("Search results do not exist.");
    }
}
