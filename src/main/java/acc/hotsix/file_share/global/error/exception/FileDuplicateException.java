package acc.hotsix.file_share.global.error.exception;

public class FileDuplicateException extends Exception {
    public FileDuplicateException(String fileName) {
        super("The file '" + fileName + "' already exists in the bucket. Please rename your file and try again.");
    }
}
